package de.msg.javatraining.donationmanager.service.user;


import de.msg.javatraining.donationmanager.controller.email.EmailRequest;
import de.msg.javatraining.donationmanager.persistence.model.ERole;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.service.email.EmailService;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.role.RoleService;
import de.msg.javatraining.donationmanager.service.role.RoleServiceException;
import de.msg.javatraining.donationmanager.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.*;


@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserValidation userValidation;


    @Override
    public List<User> findAll() throws UserServiceException {
        List <User> users=userRepository.findAll();
        if(users.isEmpty()){
            throw new UserServiceException("No Users Found","NO_USERS_FOUND");
        }
        return users;
    }

    @Override
    public User findById(Long id) throws UserServiceException {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new UserServiceException("User not found","USER_NOT_FOUND");
        }
        return optionalUser.get();
    }

    @Override
    public void deleteById(Long id) throws UserServiceException {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new UserServiceException("User not found","USER_NOT_FOUND");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User createUser(User user) throws UserServiceException {
        if(!user.getRoles().isEmpty() && user.getRoles().iterator().next().getName() == null){
            Set<Role> roles = user.getRoles();
            Set<Role> newRoles = new HashSet<>();
            for (Role role : roles) {
                try {
                    newRoles.add(this.roleService.findById(Math.toIntExact(role.getId())));
                } catch (RoleServiceException rse) {
                    System.out.println(rse.getMessage());
                }
            }
            user.setRoles(newRoles);
        }
        UserValidation.validateUser(userRepository,user);

        user.setUsername(generateUniqueUsername(user.getFirstname(),user.getLastname()));
        String randomPasswordUUID= UUID.randomUUID().toString().substring(0,8);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(randomPasswordUUID);
        user.setPassword(encryptedPassword);


        user.setActive(true);

        user = userRepository.save(user);

        sendPasswordToEmail(user.getEmail(),user.getUsername(),randomPasswordUUID,user.getLastname());

        return user;
    }

    @Transient
    @Override
    public User updateUserById(Long id, User updatedUser) throws UserServiceException, RoleServiceException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {

            UserValidation.validateUser(userRepository,updatedUser);

            User user = optionalUser.get();
            user.setFirstname(updatedUser.getFirstname());
            user.setLastname(updatedUser.getLastname());
            user.setEmail(updatedUser.getEmail());
            user.setMobilenumber(updatedUser.getMobilenumber());
            user.setActive(updatedUser.isActive());



            // Update pe roluri
            Set<Role> selectedRoles = new HashSet<>();
            for (Role selectedRole : updatedUser.getRoles()) {
                Role existingRole = roleService.findById(Math.toIntExact(selectedRole.getId()));
                selectedRoles.add(existingRole);
                if (existingRole.getName() == ERole.ROLE_REP)
                {
                    user.setCampaigns(updatedUser.getCampaigns());
                }
            }
            user.getRoles().clear();
            user.getRoles().addAll(updatedUser.getRoles());

            userRepository.save(user);

            return user;
        }
        else
            throw new UserServiceException("User not found","USER_NOT_FOUND");
    }

    public String generateUniqueUsername(String firstName, String lastName) {
        String familyNamePart = lastName.length() >= 5
                ? lastName.substring(0, 5).toLowerCase()
                : lastName.toLowerCase();

        String personalNamePart = firstName.substring(0, 1).toLowerCase();

        String username = familyNamePart + personalNamePart;

        int counter = 1;
        String newUsername = username;

        while (userRepository.existsByUsername(newUsername)) {
            newUsername = username + counter;
            counter++;
        }

        return newUsername;
    }

    public void sendPasswordToEmail(String email,String username,String password,String name){
        String subject = "Registration Credentials";
        String formattedMessage = "Dear " + name + "\n\n"
                + "Thank you for using our service. Below are your account details:\n\n"
                + "Username: " + username + "\n"
                + "Password: " + password + "\n\n"
                + "Please keep this information confidential and do not share it with anyone.\n\n"
                + "Best regards,\n"
                + "Your Donation Manager Team";

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setDestination(email);
        emailRequest.setSubject(subject);
        emailRequest.setMessage(formattedMessage);

        emailService.sendSimpleMessage(emailRequest);
    }

    public User activateUser(Long userId) throws UserServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserServiceException("User not found", "USER_NOT_FOUND"));

        if (user.isActive()) {
            throw new UserServiceException("User is already active", "USER_ALREADY_ACTIVE");
        }

        user.setActive(true);
        user.setConsecutiveUnsuccessfulAttempts(0);
        return userRepository.save(user);
    }

    public User deactivateUser(Long userId) throws UserServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserServiceException("User not found", "USER_NOT_FOUND"));

        if (!user.isActive()) {
            throw new UserServiceException("User is already inactive", "USER_ALREADY_INACTIVE");
        }

        user.setActive(false);
        user.setConsecutiveUnsuccessfulAttempts(0);
        userRepository.save(user);
        return user;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
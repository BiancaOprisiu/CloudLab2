package de.msg.javatraining.donationmanager.validation;

import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.user.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserValidation {

    @Autowired
    private UserRepository userRepository;


    public static boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        return email.matches(emailPattern);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phonePattern = "(0040|\\+?40|0)7\\d{8}";
        return phoneNumber.matches(phonePattern);
    }

    public static void validateCampaignsForRoleRep(User user) throws UserServiceException {
        boolean isRoleRep = user.getRoles().stream()
                .anyMatch(role -> "ROLE_REP".equalsIgnoreCase(role.getName().toString()));

        if (isRoleRep && user.getCampaigns().isEmpty()) {
            throw new UserServiceException("User with ROLE_REP must have at least one campaign",
                    "NO_CAMPAIGN_FOR_ROLE_REP");
        }
        if (!isRoleRep && !user.getCampaigns().isEmpty()) {
            throw new UserServiceException("Only users with ROLE_REP can have campaigns",
                    "CAMPAIGN_NOT_ALLOWED");
        }
    }
    public static void validateUser(UserRepository userRepository,User user) throws UserServiceException{


        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());


        if (user.getFirstname() == null || user.getFirstname().isEmpty()) {
            throw new UserServiceException("First name cannot be empty", "EMPTY_FIRST_NAME");
        }

        if (user.getLastname() == null || user.getLastname().isEmpty()) {
            throw new UserServiceException("Last name cannot be empty", "EMPTY_LAST_NAME");
        }

        if (!isValidEmail(user.getEmail())) {
            throw new UserServiceException("Invalid email format", "INVALID_EMAIL_FORMAT");
        }

        if (!isValidPhoneNumber(user.getMobilenumber())) {
            throw new UserServiceException("Invalid phone number format", "INVALID_PHONE_FORMAT");
        }

        if (user.getRoles().isEmpty()) {
            throw new UserServiceException("User must have at least one role", "NO_ROLES");
        }

        if (user.getRoles().stream().anyMatch(role -> "ROLE_REP".equalsIgnoreCase(role.getName().toString()))
                && user.getCampaigns().isEmpty()) {
            throw new UserServiceException("User with ROLE_REP must have at least one campaign",
                    "NO_CAMPAIGN_FOR_ROLE_REP");
        }

        validateCampaignsForRoleRep(user);




    }


}

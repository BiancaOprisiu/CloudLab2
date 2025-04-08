package de.msg.javatraining.donationmanager.controller.auth;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.msg.javatraining.donationmanager.config.security.JwtUtils;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.RoleRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.userDetails.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            if (!user.isActive()) {
                return new ResponseEntity<>("Account is deactivated. Please contact support.", HttpStatus.UNAUTHORIZED);
            }

            if (user.getLoginCount() == 0 && !user.isPasswordChangeRequired()) {
                // pw change is required

                user.setLoginCount(user.getLoginCount() + 1);
                userRepository.save(user);

                String jwt = jwtUtils.generateJwtToken(userDetails);

                return ResponseEntity.ok(new SignInResponse(userDetails.getId(),
                        userDetails.getUsername(), userDetails.getEmail(), roles, userDetails.getLoginCount(), user.isPasswordChangeRequired()));
            }

            if (!user.isPasswordChangeRequired()) {
                // Password change is required, user must have changed the password already
//        return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                .body("Password change required. Please change your password.");
                String jwt = jwtUtils.generateJwtToken(userDetails);
                return ResponseEntity.ok(new SignInResponse(userDetails.getId(),
                        userDetails.getUsername(), userDetails.getEmail(), roles, userDetails.getLoginCount(), user.isPasswordChangeRequired()));
            }

            user.setConsecutiveUnsuccessfulAttempts(0);
            userRepository.save(user);

            String jwt = jwtUtils.generateJwtToken(userDetails);


            return ResponseEntity.ok(new SignInResponse(jwt, userDetails.getId(),
                    userDetails.getUsername(), userDetails.getEmail(), roles, userDetails.getLoginCount(), user.isPasswordChangeRequired()));
        } catch (BadCredentialsException e) {
            // consecutive unsuccessful login attempts and account deactivation

            try{
                Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();

                    user.setConsecutiveUnsuccessfulAttempts(user.getConsecutiveUnsuccessfulAttempts() + 1);

                    if (user.getConsecutiveUnsuccessfulAttempts() >= 5) {
                        user.setActive(false);
                        user.setConsecutiveUnsuccessfulAttempts(0);
                    }

                    userRepository.save(user);
                }

            }catch (BadCredentialsException ex){
                return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
            }


            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {

        String username = passwordChangeRequest.getUsername();
        String oldPassword = passwordChangeRequest.getOldPassword();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));

            // Successfully authenticated now change pw
            User user = userRepository.findByUsername(username).orElseThrow();

            if (user.getLoginCount() > 1) {
                return new ResponseEntity<>("Password change not allowed.", HttpStatus.FORBIDDEN);
            }

            if (user.isPasswordChangeRequired()) {
                return new ResponseEntity<>("Password change not allowed.", HttpStatus.FORBIDDEN);
            }

            if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
                return new ResponseEntity<>("New passwords do not match.", HttpStatus.BAD_REQUEST);
            }

            user.setPassword(encoder.encode(passwordChangeRequest.getNewPassword()));
            user.setPasswordChangeRequired(true); // Set the flag
            userRepository.save(user);

            return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid username or password.", HttpStatus.UNAUTHORIZED);
        }
    }
}



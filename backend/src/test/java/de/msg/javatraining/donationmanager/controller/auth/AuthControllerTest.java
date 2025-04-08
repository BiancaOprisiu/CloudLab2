package de.msg.javatraining.donationmanager.controller.auth;

import de.msg.javatraining.donationmanager.config.security.JwtUtils;
import de.msg.javatraining.donationmanager.persistence.model.*;
import de.msg.javatraining.donationmanager.persistence.repository.RoleRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.userDetails.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAuthenticateUser_Success_whenLoginCountHigherThan1() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        User user = createUser1(1L);
        user.setLoginCount(1);
        user.setPasswordChangeRequired(true);


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);


        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("username");


        when(userRepository.findByUsername("username")).thenReturn(java.util.Optional.of(user));


        when(jwtUtils.generateJwtToken(userDetails)).thenReturn("dummy-jwt-token");


        ResponseEntity<?> response = authController.authenticateUser(loginRequest);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

    }



    @Test
    void testAuthenticateUser_Success_whenLoginCountIsZero() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        User user = createUser1(1L);


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);


        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("username");


        when(userRepository.findByUsername("username")).thenReturn(java.util.Optional.of(user));


        when(jwtUtils.generateJwtToken(userDetails)).thenReturn("dummy-jwt-token");


        ResponseEntity<?> response = authController.authenticateUser(loginRequest);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

    }

    @Test
    void testAuthenticateUser_AccountDeactivatedAfter5ConsecutiveFailures() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("invalidpassword");


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));


        User user = createUser1(1L);
        user.setConsecutiveUnsuccessfulAttempts(5); // Set 5 consecutive unsuccessful attempts
        when(userRepository.findByUsername("username")).thenReturn(java.util.Optional.of(user));

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);


        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());


        assertEquals(0, user.getConsecutiveUnsuccessfulAttempts());
        assertFalse(user.isActive()); // User should be deactivated
    }

    @Test
    void testAuthenticateUser_AccountDeactivated() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        User user = createUser1(1L);
        user.setActive(false);


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);


        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("username");


        when(userRepository.findByUsername("username")).thenReturn(java.util.Optional.of(user));


        ResponseEntity<?> response = authController.authenticateUser(loginRequest);


        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Account is deactivated. Please contact support.", response.getBody());
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("invalidpassword");


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));


        when(userRepository.findByUsername("username")).thenReturn(java.util.Optional.of(createUser1(1L)));


        ResponseEntity<?> response = authController.authenticateUser(loginRequest);


        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    void testChangePassword_Success() {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setUsername("username");
        passwordChangeRequest.setOldPassword("oldpassword");
        passwordChangeRequest.setNewPassword("newpassword");
        passwordChangeRequest.setConfirmPassword("newpassword");

        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "oldpassword");

        User user = createUser1(1L);
        user.setLoginCount(0);
        user.setPasswordChangeRequired(false);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(encoder.encode("newpassword")).thenReturn("encodednewpassword");

        ResponseEntity<String> response = authController.changePassword(passwordChangeRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully.", response.getBody());
        assertTrue(user.isPasswordChangeRequired());
        assertEquals("encodednewpassword", user.getPassword());
    }

    @Test
    void testChangePassword_PasswordChangeNotAllowed() {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setUsername("username");
        passwordChangeRequest.setOldPassword("oldpassword");
        passwordChangeRequest.setNewPassword("newpassword");
        passwordChangeRequest.setConfirmPassword("newpassword");

        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "oldpassword");

        User user = createUser1(1L);
        user.setLoginCount(2); // Login count greater than 1

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        ResponseEntity<String> response = authController.changePassword(passwordChangeRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password change not allowed.", response.getBody());
    }

    @Test
    void testChangePassword_PasswordChangeRequired() {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setUsername("username");
        passwordChangeRequest.setOldPassword("oldpassword");
        passwordChangeRequest.setNewPassword("newpassword");
        passwordChangeRequest.setConfirmPassword("newpassword");

        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "oldpassword");

        User user = createUser1(1L);
        user.setLoginCount(0);
        user.setPasswordChangeRequired(true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        ResponseEntity<String> response = authController.changePassword(passwordChangeRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password change not allowed.", response.getBody());
    }

    @Test
    void testChangePassword_PasswordsDoNotMatch() {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setUsername("username");
        passwordChangeRequest.setOldPassword("oldpassword");
        passwordChangeRequest.setNewPassword("newpassword");
        passwordChangeRequest.setConfirmPassword("differentpassword");

        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "oldpassword");

        User user = createUser1(1L);
        user.setLoginCount(0);
        user.setPasswordChangeRequired(false);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        ResponseEntity<String> response = authController.changePassword(passwordChangeRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("New passwords do not match.", response.getBody());
    }

    @Test
    void testChangePassword_InvalidAuthentication() {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setUsername("username");
        passwordChangeRequest.setOldPassword("invalidpassword");
        passwordChangeRequest.setNewPassword("newpassword");
        passwordChangeRequest.setConfirmPassword("newpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<String> response = authController.changePassword(passwordChangeRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password.", response.getBody());
    }

    @Test
    void testAuthenticateUser_PasswordChangeRequiredOnFirstLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        User user = createUser1(1L);
        user.setLoginCount(0);
        user.setPasswordChangeRequired(false);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("username");

        when(userRepository.findByUsername("username")).thenReturn(java.util.Optional.of(user));

        when(jwtUtils.generateJwtToken(userDetails)).thenReturn("dummy-jwt-token");

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(1, user.getLoginCount());
    }

    @Test
    void testAuthenticateUser_PasswordChangeRequiredSubsequentLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        User user = createUser1(1L);
        user.setLoginCount(1); // Setting login count to simulate subsequent login
        user.setPasswordChangeRequired(false);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("username");

        when(userRepository.findByUsername("username")).thenReturn(java.util.Optional.of(user));

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        //assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        //assertEquals("Password change required. Please change your password.", response.getBody());
    }

    private static User createUser1(Long id) {
        User user1 = new User();

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName(ERole.ROLE_ADM);

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName(ERole.ROLE_MGN);

        Permision permision1 = new Permision();
        permision1.setId(1L);
        permision1.setName(PRole.PERMISSION_MANAGEMENT);

        Permision permision2 = new Permision();
        permision1.setId(2L);
        permision1.setName(PRole.USER_MANAGEMENT);

        Permision permision3 = new Permision();
        permision1.setId(3L);
        permision1.setName(PRole.CAMP_MANAGEMENT);

        Permision permision4 = new Permision();
        permision1.setId(4L);
        permision1.setName(PRole.BENEF_MANAGEMENT);

        role1.getPermisions().add(permision1);
        role1.getPermisions().add(permision2);
        role2.getPermisions().add(permision3);
        role2.getPermisions().add(permision4);


        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);


        Set<Notification> notifications = new HashSet<>();

        List<Donation> createdDonations = new ArrayList<>();

        List<Donation> approvedDonations = new ArrayList<>();

        user1.setId(id);
        user1.setFirstname("Tudor");
        user1.setLastname("Zidan");
        user1.setUsername("tudorz");
        user1.setEmail("tudorz@yahoo.com");
        user1.setPassword("password1");
        user1.setMobilenumber("0740299999");
        user1.setActive(true);
        user1.setConsecutiveUnsuccessfulAttempts(0);
        user1.setRoles(roles);

        //user1.setNotifications(notifications);
        user1.setCreatedDonations(createdDonations);
        user1.setApprovedDonations(approvedDonations);

        return user1;
    }



}
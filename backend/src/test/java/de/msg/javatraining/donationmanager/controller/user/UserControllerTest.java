package de.msg.javatraining.donationmanager.controller.user;

import de.msg.javatraining.donationmanager.persistence.model.*;
import de.msg.javatraining.donationmanager.service.user.UserService;
import de.msg.javatraining.donationmanager.service.user.UserServiceException;
import de.msg.javatraining.donationmanager.service.role.RoleServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private static final String USER_NOT_FOUND_MESSAGE="User not found";
    private static final String USER_NOT_FOUND_ERROR_CODE="USER_NOT_FOUND";
    private static final String ACCES_DENIED_MESSAGE="Access denied";
    private static final String ACCES_DENIED_ERROR_CODE="ACCESS_DENIED";

    private static final String INVALID_USER_MESSAGE="Invalid user data";
    private static final String INVALID_USER_ERROR_CODE="INVALID_USER";

    private static final String USER_ALREADY_ACTIVE_MESSAGE="User is already active";
    private static final String USER_ALREADY_ACTIVE_ERROR_CODE="USER_ALREADY_ACTIVE";
    private static final String USER_ALREADY_INACTIVE_MESSAGE="User is already inactive";
    private static final String USER_ALREADY_INACTIVE_ERROR_CODE="USER_ALREADY_INACTIVE";


    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testFindAll() throws UserServiceException {
        List <User> userList= new ArrayList<>();
        userList.add(createUser1(1L));
        userList.add(createUser2(2L));

        when(userService.findAll()).thenReturn(userList);

        ResponseEntity <?> response = userController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList,response.getBody());
    }

    @Test
    void testFindAll_HandleForbiddenError() throws UserServiceException {
        when(userService.findAll()).thenThrow(new UserServiceException(ACCES_DENIED_MESSAGE, ACCES_DENIED_ERROR_CODE));

        ResponseEntity<?> response = userController.findAll();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testFindById_Success() throws UserServiceException {
        User user = createUser1(1L);
        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<?> response = userController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testFindById_NotFound() throws UserServiceException {
        when(userService.findById(1L)).thenThrow(new UserServiceException(USER_NOT_FOUND_MESSAGE, USER_NOT_FOUND_ERROR_CODE));

        ResponseEntity<?> response = userController.findById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(USER_NOT_FOUND_MESSAGE, response.getBody());
    }

    @Test
    void testDeleteById_Success() throws UserServiceException {
        doNothing().when(userService).deleteById(1L); // Mocking the void method to do nothing

        ResponseEntity<?> response = userController.deleteById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OK", response.getBody());
    }

    @Test
    void testDeleteById_NotFound() throws UserServiceException {
        doThrow(new UserServiceException(USER_NOT_FOUND_MESSAGE, USER_NOT_FOUND_ERROR_CODE)).when(userService).deleteById(1L);

        ResponseEntity<?> response = userController.deleteById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(USER_NOT_FOUND_MESSAGE, response.getBody());
    }

    @Test
    void testCreateUser_Success() throws UserServiceException {
        User newUser = createUser1(1L); // Create a new user object

        when(userService.createUser(any(User.class))).thenReturn(newUser);

        ResponseEntity<?> response = userController.createUser(newUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newUser, response.getBody());
    }

    @Test
    void testCreateUser_BadRequest() throws UserServiceException {
        User newUser = createUser1(1L); // Create a new user object

        when(userService.createUser(any(User.class))).thenThrow(new UserServiceException(INVALID_USER_MESSAGE, INVALID_USER_ERROR_CODE));

        ResponseEntity<?> response = userController.createUser(newUser);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateUserById_Success() throws UserServiceException, RoleServiceException {
        Long userId = 1L;
        User originalUser = createUser1(userId);
        User updatedUser = createUser2(userId);

        when(userService.updateUserById(eq(userId), any(User.class))).thenReturn(updatedUser);
        when(userService.findById(eq(userId))).thenReturn(originalUser);

        ResponseEntity<?> response = userController.updateUserById(userId, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void testUpdateUserById_NotFound() throws UserServiceException, RoleServiceException, RoleServiceException {
        Long userId = 1L;
        User updatedUser = createUser2(userId);

        when(userService.updateUserById(eq(userId), any(User.class))).thenThrow(new UserServiceException(USER_NOT_FOUND_MESSAGE, USER_NOT_FOUND_ERROR_CODE));

        ResponseEntity<?> response = userController.updateUserById(userId, updatedUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testActivateUser_Success() throws UserServiceException {
        Long userId = 1L;
        User activeUser = createUser1(userId);
        activeUser.setActive(true);

        when(userService.activateUser(userId)).thenReturn(activeUser);

        ResponseEntity<?> response = userController.activateUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeUser, response.getBody());
    }

    @Test
    void testActivateUser_AlreadyActive() throws UserServiceException {
        Long userId = 1L;
        User activeUser = createUser1(userId);
        activeUser.setActive(true);

        when(userService.activateUser(userId)).thenThrow(new UserServiceException(USER_ALREADY_ACTIVE_MESSAGE, USER_ALREADY_ACTIVE_ERROR_CODE));

        ResponseEntity<?> response = userController.activateUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testActivateUser_NotFound() throws UserServiceException {
        Long userId = 1L;

        when(userService.activateUser(userId)).thenThrow(new UserServiceException(USER_NOT_FOUND_MESSAGE, USER_NOT_FOUND_ERROR_CODE));

        ResponseEntity<?> response = userController.activateUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeactivateUser_Success() throws UserServiceException {
        Long userId = 1L;
        User deactivatedUser = createUser1(userId);
        deactivatedUser.setActive(false);

        when(userService.deactivateUser(userId)).thenReturn(deactivatedUser);

        ResponseEntity<?> response = userController.deactivateUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deactivatedUser, response.getBody());
    }

    @Test
    void testDeactivateUser_AlreadyInactive() throws UserServiceException {
        Long userId = 1L;
        User deactivatedUser = createUser1(userId);
        deactivatedUser.setActive(false);

        when(userService.deactivateUser(userId)).thenThrow(new UserServiceException(USER_ALREADY_INACTIVE_MESSAGE, USER_ALREADY_INACTIVE_ERROR_CODE));

        ResponseEntity<?> response = userController.deactivateUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeactivateUser_NotFound() throws UserServiceException {
        Long userId = 1L;

        when(userService.deactivateUser(userId)).thenThrow(new UserServiceException(USER_NOT_FOUND_MESSAGE, USER_NOT_FOUND_ERROR_CODE));

        ResponseEntity<?> response = userController.deactivateUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
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

        user1.setNotifications(notifications);
        user1.setCreatedDonations(createdDonations);
        user1.setApprovedDonations(approvedDonations);

        return user1;
    }

    private static User createUser2(Long id) {
        User user2 = new User();

        Role role3 = new Role();
        role3.setId(3L);
        role3.setName(ERole.ROLE_REP);


        Permision permision5 = new Permision();
        permision5.setId(5L);
        permision5.setName(PRole.CAMP_REPORT_RESTRICTED);

        Campaign campaign1 = new Campaign();
        campaign1.setName("Feed a smile");
        campaign1.setPurpose("Child Support");

        Campaign campaign2 = new Campaign();
        campaign2.setName("Santa is back");
        campaign2.setPurpose("Christmas Presents");

        role3.getPermisions().add(permision5);


        Set<Role> roles = new HashSet<>();
        roles.add(role3);

        Set<Role> rolesRep = new HashSet<>();
        rolesRep.add(role3);

        Set<Campaign> campaigns = new HashSet<>();
        campaigns.add(campaign1);
        campaigns.add(campaign2);

        Set<Notification> notifications = new HashSet<>();

        List<Donation> createdDonations = new ArrayList<>();

        List<Donation> approvedDonations = new ArrayList<>();

        user2.setId(id);
        user2.setFirstname("Peter");
        user2.setLastname("Kovacs");
        user2.setUsername("peterk");
        user2.setEmail("peterk@yahoo.com");
        user2.setPassword("password2");
        user2.setMobilenumber("0740299999");
        user2.setActive(true);
        user2.setConsecutiveUnsuccessfulAttempts(0);
        user2.setRoles(rolesRep);
        user2.setCampaigns(campaigns);
        user2.setNotifications(notifications);
        user2.setCreatedDonations(createdDonations);
        user2.setApprovedDonations(approvedDonations);

        return user2;
    }

}
package de.msg.javatraining.donationmanager.service.user;

import de.msg.javatraining.donationmanager.controller.email.EmailRequest;
import de.msg.javatraining.donationmanager.persistence.model.*;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.email.EmailService;
import de.msg.javatraining.donationmanager.service.role.RoleService;
import de.msg.javatraining.donationmanager.service.role.RoleServiceException;
import de.msg.javatraining.donationmanager.validation.UserValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceTest {

    private static final String USER_ALREADY_FOUND_MESSAGE="User already found";
    private static final String USER_ALREADY_FOUND_ERROR_CODE="USER_ALREADY_FOUND";

    private static final String FIRST_NAME_NOT_EMPTY_MESSAGE= "First name cannot be empty";
    private static final String FIRST_NAME_NOT_EMPTY_ERROR_CODE="EMPTY_FIRST_NAME";
    private static final String LAST_NAME_NOT_EMPTY_MESSAGE= "Last name cannot be empty";
    private static final String LAST_NAME_NOT_EMPTY_ERROR_CODE="EMPTY_LAST_NAME";

    private static final String INVALID_EMAIL_FORMAT_MESSAGE="Invalid email format";
    private static final String INVALID_EMAIL_FORMAT_ERROR_CODE="INVALID_EMAIL_FORMAT";

    private static final String INVALID_PHONE_FORMAT_MESSAGE="Invalid phone number format";
    private static final String INVALID_PHONE_FORMAT_ERROR_CODE="INVALID_PHONE_FORMAT";

    private static final String NO_ROLE_FORMAT_MESSAGE="User must have at least one role";
    private static final String NO_ROLE_FORMAT_ERROR_CODE="NO_ROLES";

    private static final String NO_CAMPAIGN_FOR_REP_ROLE_MESSAGE= "User with ROLE_REP must have at least one campaign";
    private static final String NO_CAMPAIGN_FOR_REP_ROLE_ERROR_CODE="NO_CAMPAIGN_FOR_ROLE_REP";
    private static final String USER_ALREADY_ACTIVE_MESSAGE="User is already active";
    private static final String USER_ALREADY_ACTIVE_ERROR_CODE="USER_ALREADY_ACTIVE";
    private static final String USER_ALREADY_INACTIVE_MESSAGE="User is already inactive";
    private static final String USER_ALREADY_INACTIVE_ERROR_CODE="USER_ALREADY_INACTIVE";

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private RoleService roleService;

    @Mock
    UserValidation userValidation;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAll_returnsExpectedList_WhenServiceReturnsValidList() throws UserServiceException {
        List<User> mockUsers = new ArrayList<>();
        User user1 = createUser1(1L); // Create test user 1
        User user2 = createUser2(2L); // Create test user 2


        mockUsers.add(user1);
        mockUsers.add(user2);


        when(userRepository.findAll()).thenReturn(mockUsers);

        assertEquals(mockUsers.size(), 2);
        List<User> users = userService.findAll();


        assertEquals(mockUsers.size(), users.size());

        assertEquals(mockUsers.get(0).getFirstname(), users.get(0).getFirstname());
        assertEquals(mockUsers.get(1).getLastname(), users.get(1).getLastname());

    }

    @Test
    void findAll_returnsException_WhenServiceReturnsEmptyList() throws UserServiceException {
        List<User> mockUsers = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(mockUsers);

        assertEquals(mockUsers.size(), 0);

        assertThrows(UserServiceException.class, () -> userService.findAll());


    }

    @Test
    void findById_returnsExpectedUser_whenServiceReturnsValidUser() throws UserServiceException {
        User user = createUser1(1L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));


        User result = userService.findById(user.getId());

        assertEquals(user, result);
    }

    @Test
    void findById_returnsUserServiceException_whenNoSuchUserIsFound() {
        User user = createUser1(1L);
        long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserServiceException.class, () -> userService.findById(userId));
    }

    @Test
    void deleteById_SuccesCase_whenSuchUserExists() {
        User user = createUser1(1L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteById(user.getId()));

        verify(userRepository, times(1)).deleteById(user.getId());

    }

    @Test
    void deleteById_FailCase_whenNoSuchUserExists() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserServiceException.class, () -> userService.deleteById(userId));

        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void createUser_returnsExpectedUser_whenServiceReturnsValidUser() throws UserServiceException {
        User user = createUser1(1L); // Use your createUser1 method to create a user

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);


        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("Tudor", createdUser.getFirstname());
        assertEquals("Zidan", createdUser.getLastname());
        assertEquals("tudorz@yahoo.com", createdUser.getEmail());

        assertEquals("0740299999", createdUser.getMobilenumber());
        assertTrue(createdUser.isActive());
        assertEquals(0, createdUser.getConsecutiveUnsuccessfulAttempts());

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName(ERole.ROLE_ADM);

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName(ERole.ROLE_MGN);

        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);

        assertEquals(roles, user.getRoles());

        assertEquals(new HashSet<>(), user.getNotifications());
        assertEquals(new ArrayList<>(), user.getCreatedDonations());
        assertEquals(new ArrayList<>(), user.getApprovedDonations());
    }

    @Test
    public void createUser_returnsExpectedErrors_whenEmptyFirstName() {
        User user = new User();
        user.setUsername("newUsername");
        user.setFirstname("");

        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userValidation.validateUser(userRepository, user));

        assertEquals(FIRST_NAME_NOT_EMPTY_MESSAGE, exception.getMessage());
        assertEquals(FIRST_NAME_NOT_EMPTY_ERROR_CODE, exception.getErrorCode());

        verify(userRepository, never()).save(any(User.class)); // or any other method interactions you want to verify
    }

    @Test
    public void createUser_returnsExpectedErrors_whenNullFirstName() {
        User user = createUser1(1L);
        user.setUsername("newUsername");
        user.setFirstname(null);

        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userValidation.validateUser(userRepository, user));

        assertEquals(FIRST_NAME_NOT_EMPTY_MESSAGE, exception.getMessage());
        assertEquals(FIRST_NAME_NOT_EMPTY_ERROR_CODE, exception.getErrorCode());

        verify(userRepository, never()).save(any(User.class)); // or any other method interactions you want to verify
    }

    @Test
    public void createUser_returnsExpectedErrors_whenEmptyLastName() {
        User user = createUser1(1L);
        user.setUsername("newUsername");
        user.setLastname("");

        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userValidation.validateUser(userRepository, user));

        assertEquals(LAST_NAME_NOT_EMPTY_MESSAGE, exception.getMessage());
        assertEquals(LAST_NAME_NOT_EMPTY_ERROR_CODE, exception.getErrorCode());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void createUser_returnsExpectedErrors_whenNullLastName() {
        User user = createUser1(1L);
        user.setUsername("newUsername");
        user.setLastname(null);

        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userValidation.validateUser(userRepository, user));

        assertEquals(LAST_NAME_NOT_EMPTY_MESSAGE, exception.getMessage());
        assertEquals(LAST_NAME_NOT_EMPTY_ERROR_CODE, exception.getErrorCode());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void createUser_returnsExpectedErrors_whenInvalidEmailFormat() {
        User user = createUser1(1L);
        user.setEmail("invalid-email");

        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userValidation.validateUser(userRepository, user));

        assertEquals(INVALID_EMAIL_FORMAT_MESSAGE, exception.getMessage());
        assertEquals(INVALID_EMAIL_FORMAT_ERROR_CODE, exception.getErrorCode());

        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    public void createUser_returnsExpectedErrors_whenInvalidPhoneNumberFormat() {
        User user = createUser1(1L);
        user.setMobilenumber("1234567890");

        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userValidation.validateUser(userRepository, user));

        assertEquals(INVALID_PHONE_FORMAT_MESSAGE, exception.getMessage());
        assertEquals(INVALID_PHONE_FORMAT_ERROR_CODE, exception.getErrorCode());

        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    public void createUser_returnsExpectedErrors_whenNoRolesAssigned() {
        User user = createUser1(1L);
        user.setEmail("john.doe@example.com");
        user.setRoles(Collections.emptySet());

        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userValidation.validateUser(userRepository, user));

        assertEquals(NO_ROLE_FORMAT_MESSAGE, exception.getMessage());
        assertEquals(NO_ROLE_FORMAT_ERROR_CODE, exception.getErrorCode());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void createUser_returnsExpectedErrors_whenNoCampaignsAssignedForRoleRep() {
        User user = createUser1(1L);

        Role roleRep = new Role();
        roleRep.setId(1L);
        roleRep.setName(ERole.ROLE_REP);

        Set<Role> roles = new HashSet<>();
        roles.add(roleRep);

        user.setRoles(roles);
        user.setCampaigns(Collections.emptySet());

        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userValidation.validateUser(userRepository, user));

        assertEquals(NO_CAMPAIGN_FOR_REP_ROLE_MESSAGE, exception.getMessage());
        assertEquals(NO_CAMPAIGN_FOR_REP_ROLE_ERROR_CODE, exception.getErrorCode());

        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void updateUserById_returnsExpectedUser_whenServiceReturnsValidUser() throws UserServiceException, RoleServiceException {
        User user = createUser1(1L);
        User updatedUser = createUser2(2L);

        Role roleRep = new Role();
        roleRep.setId(3L);
        roleRep.setName(ERole.ROLE_REP);
        when(roleService.findById(3)).thenReturn(roleRep);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.updateUserById(1L, updatedUser);

        assertNotNull(result);
        assertEquals(updatedUser.getFirstname(), result.getFirstname());
        assertEquals(updatedUser.getLastname(), result.getLastname());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals(updatedUser.getMobilenumber(), result.getMobilenumber());
        assertEquals(updatedUser.getRoles().size(), result.getRoles().size());
        assertEquals(updatedUser.getCampaigns().size(), result.getCampaigns().size());

    }

    @Test
    void updateUserById_returnsExpectedErrors_whenUserNotFound() throws UserServiceException, RoleServiceException {

        User updatedUser = createUser2(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserServiceException.class, () -> {
            userService.updateUserById(1L, updatedUser);
        });


        verify(userRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(roleService);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUserById_returnsExpectedErrors_whenEmptyFirstName() throws UserServiceException, RoleServiceException {

        User originalUser = createUser1(1L);


        User updatedUser = createUser2(2L);
        updatedUser.setFirstname("");


        when(userRepository.findById(1L)).thenReturn(Optional.of(originalUser));


        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userService.updateUserById(1L, updatedUser));


        assertEquals(FIRST_NAME_NOT_EMPTY_MESSAGE, exception.getMessage());
        assertEquals(FIRST_NAME_NOT_EMPTY_ERROR_CODE, exception.getErrorCode());


        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserById_returnsExpectedErrors_whenNullFirstName() throws UserServiceException, RoleServiceException {

        User originalUser = createUser1(1L);


        User updatedUser = createUser2(2L);
        updatedUser.setFirstname(null);


        when(userRepository.findById(1L)).thenReturn(Optional.of(originalUser));


        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userService.updateUserById(1L, updatedUser));


        assertEquals(FIRST_NAME_NOT_EMPTY_MESSAGE, exception.getMessage());
        assertEquals(FIRST_NAME_NOT_EMPTY_ERROR_CODE, exception.getErrorCode());


        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserById_returnsExpectedErrors_whenEmptyLastName() throws UserServiceException, RoleServiceException {

        User originalUser = createUser1(1L);


        User updatedUser = createUser2(2L);
        updatedUser.setLastname("");


        when(userRepository.findById(1L)).thenReturn(Optional.of(originalUser));


        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userService.updateUserById(1L, updatedUser));


        assertEquals(LAST_NAME_NOT_EMPTY_MESSAGE, exception.getMessage());
        assertEquals(LAST_NAME_NOT_EMPTY_ERROR_CODE, exception.getErrorCode());


        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserById_returnsExpectedErrors_whenNullLastName() throws UserServiceException, RoleServiceException {

        User originalUser = createUser1(1L);


        User updatedUser = createUser2(2L);
        updatedUser.setLastname(null);


        when(userRepository.findById(1L)).thenReturn(Optional.of(originalUser));


        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userService.updateUserById(1L, updatedUser));


        assertEquals(LAST_NAME_NOT_EMPTY_MESSAGE, exception.getMessage());
        assertEquals(LAST_NAME_NOT_EMPTY_ERROR_CODE, exception.getErrorCode());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserById_returnsExpectedErrors_whenInvalidEmail() throws UserServiceException, RoleServiceException {

        User originalUser = createUser1(1L);


        User updatedUser = createUser2(2L);
        updatedUser.setEmail("invalidEmail");


        when(userRepository.findById(1L)).thenReturn(Optional.of(originalUser));


        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userService.updateUserById(1L, updatedUser));


        assertEquals(INVALID_EMAIL_FORMAT_MESSAGE, exception.getMessage());
        assertEquals(INVALID_EMAIL_FORMAT_ERROR_CODE, exception.getErrorCode());


        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserById_returnsExpectedErrors_whenInvalidPhoneNumber() throws UserServiceException, RoleServiceException {

        User originalUser = createUser1(1L);


        User updatedUser = createUser2(2L);
        updatedUser.setMobilenumber("07402222222222");


        when(userRepository.findById(1L)).thenReturn(Optional.of(originalUser));


        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userService.updateUserById(1L, updatedUser));


        assertEquals(INVALID_PHONE_FORMAT_MESSAGE, exception.getMessage());
        assertEquals(INVALID_PHONE_FORMAT_ERROR_CODE, exception.getErrorCode());


        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserById_returnsExpectedErrors_whenNoRolesAssigned() throws UserServiceException, RoleServiceException {

        User originalUser = createUser1(1L);


        User updatedUser = createUser2(2L);

        Set<Role> emptyRoles = new HashSet<>();
        updatedUser.setRoles(emptyRoles);


        when(userRepository.findById(1L)).thenReturn(Optional.of(originalUser));


        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userService.updateUserById(1L, updatedUser));


        assertEquals(NO_ROLE_FORMAT_MESSAGE, exception.getMessage());
        assertEquals(NO_ROLE_FORMAT_ERROR_CODE, exception.getErrorCode());


        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserById_returnsExpectedErrors_whenNoCampaignsAssignedForRepRole() throws UserServiceException, RoleServiceException {

        User originalUser = createUser1(1L);


        User updatedUser = createUser2(2L);

        Set<Campaign> emptyCampaigns = new HashSet<>();
        updatedUser.setCampaigns(emptyCampaigns);


        when(userRepository.findById(1L)).thenReturn(Optional.of(originalUser));


        UserServiceException exception = assertThrows(UserServiceException.class, () ->
                userService.updateUserById(1L, updatedUser));


        assertEquals(NO_CAMPAIGN_FOR_REP_ROLE_MESSAGE, exception.getMessage());
        assertEquals(NO_CAMPAIGN_FOR_REP_ROLE_ERROR_CODE, exception.getErrorCode());


        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void SendPasswordToEmail() {
        String email = "john.doe@example.com";
        String username = "johndoe";
        String password = "pass123";
        String name = "John Doe";

        userService.sendPasswordToEmail(email, username, password, name);

        ArgumentCaptor<EmailRequest> emailRequestCaptor = ArgumentCaptor.forClass(EmailRequest.class);
        verify(emailService, times(1)).sendSimpleMessage(emailRequestCaptor.capture());

        EmailRequest capturedEmailRequest = emailRequestCaptor.getValue();
        assertNotNull(capturedEmailRequest);
        assertEquals(email, capturedEmailRequest.getDestination());
        assertTrue(capturedEmailRequest.getMessage().contains(username));
        assertTrue(capturedEmailRequest.getMessage().contains(password));
        assertTrue(capturedEmailRequest.getMessage().contains(name));
    }

    @Test
    void testGenerateUniqueUsername() {
        String firstName = "John";
        String lastName = "Doe";

        when(userRepository.existsByUsername(anyString())).thenReturn(true, true, false);

        String generatedUsername = userService.generateUniqueUsername(firstName, lastName);

        assertNotNull(generatedUsername);
        assertTrue(generatedUsername.startsWith("doej"));
        assertTrue(generatedUsername.length() <= 10);

        verify(userRepository, times(3)).existsByUsername(anyString());
    }

    @Test
    void activateUser_activatesUser_whenUserIsDeactivated() throws UserServiceException {
        long userId = 1L;
        User deactivatedUser = createUser1(userId);
        deactivatedUser.setActive(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(deactivatedUser));
        when(userRepository.save(deactivatedUser)).thenReturn(deactivatedUser);

        User activatedUser = userService.activateUser(userId);

        assertTrue(activatedUser.isActive());
        verify(userRepository, times(1)).save(deactivatedUser);

    }

    @Test
    void testActivateUser_cantActivate_whenUserAlreadyActive() {
        long userId = 1L;
        User activeUser = createUser1(userId);
        activeUser.setActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));

        UserServiceException exception = assertThrows(UserServiceException.class, () -> userService.activateUser(userId));

        assertEquals(USER_ALREADY_ACTIVE_MESSAGE, exception.getMessage());
        assertEquals(USER_ALREADY_ACTIVE_ERROR_CODE, exception.getErrorCode());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deactivateUser_deactivatesUser_WhenUserIsActivated() throws UserServiceException {
        long userId = 1L;
        User deactivatedUser = createUser1(userId);
        deactivatedUser.setActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(deactivatedUser));
        when(userRepository.save(deactivatedUser)).thenReturn(deactivatedUser);

        User activatedUser = userService.deactivateUser(userId);

        assertFalse(activatedUser.isActive());
        verify(userRepository, times(1)).save(deactivatedUser);

    }

    @Test
    void deactivateUser_cantDeactivate_whenUserAlreadyDeactivated() {
        long userId = 1L;
        User activeUser = createUser1(userId);
        activeUser.setActive(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));

        UserServiceException exception = assertThrows(UserServiceException.class, () -> userService.deactivateUser(userId));

        assertEquals(USER_ALREADY_INACTIVE_MESSAGE, exception.getMessage());
        assertEquals(USER_ALREADY_INACTIVE_ERROR_CODE, exception.getErrorCode());

        verify(userRepository, never()).save(any(User.class));
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
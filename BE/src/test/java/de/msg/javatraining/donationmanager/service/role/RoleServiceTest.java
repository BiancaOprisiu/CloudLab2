package de.msg.javatraining.donationmanager.service.role;

import de.msg.javatraining.donationmanager.persistence.model.PRole;
import de.msg.javatraining.donationmanager.persistence.repository.PermisionRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import de.msg.javatraining.donationmanager.persistence.model.Permision;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.repository.RoleRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Mock
    private PermisionRepository permisionRepository;

    @Test
    void findAll_returnsExpectedList_whenRolesExist() throws RoleServiceException {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role());

        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.findAll();

        assertEquals(roles, result);
    }

    @Test
    void findAll_throwsRoleServiceException_whenNoRolesExist() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(RoleServiceException.class, () -> roleService.findAll());
    }

    @Test
    void addPermissionToRole_addsPermissionToRole_whenValidInputGiven() throws RoleServiceException {
        Role role = new Role();
        role.setId(1L);
        Permision permission = new Permision();
        permission.setName(PRole.PERMISSION_MANAGEMENT);

        when(permisionRepository.findByName(PRole.PERMISSION_MANAGEMENT)).thenReturn(permission);
        when(roleRepository.save(role)).thenReturn(role);

        boolean added = roleService.addPermission(role, permission);

        assertTrue(added);
        assertTrue(role.getPermisions().contains(permission));
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void addPermissionToRole_throwsRoleServiceException_whenRoleIsNull() {
        Role role = null;
        Permision permission = new Permision();

        assertThrows(RoleServiceException.class, () -> roleService.addPermission(role, permission));
    }

    @Test
    void deletePermissionFromRole_deletesPermissionFromRole_whenValidInputGiven() throws RoleServiceException {
        Role role = new Role();
        role.setId(1L);
        Permision permission = new Permision();
        role.getPermisions().add(permission);

        when(roleRepository.save(role)).thenReturn(role);

        boolean deleted = roleService.deletePermission(role, permission);

        assertTrue(deleted);
        assertFalse(role.getPermisions().contains(permission));
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void deletePermissionFromRole_throwsRoleServiceException_whenRoleIsNull() {
        Permision permission = new Permision();

        assertThrows(RoleServiceException.class, () -> roleService.deletePermission(null, permission));
        verify(roleRepository, times(0)).save(any(Role.class));
    }

    @Test
    void deletePermissionFromRole_returnsFalse_whenPermissionNotInRole() throws RoleServiceException {
        Role role = new Role();
        role.setId(1L);
        Permision permission = new Permision();
        permission.setId(1L);

        boolean deleted = roleService.deletePermission(role, permission);

        assertFalse(deleted);
        assertFalse(role.getPermisions().contains(permission)); // Making sure the permission is not added
        verify(roleRepository, times(0)).save(role); // Verify that the save method was not invoked
    }


    @Test
    void getPermissionById_returnsPermission_whenValidInputGiven() throws RoleServiceException {
        Role role = new Role();
        role.setId(1L);
        Permision permission = new Permision();
        permission.setId(1L);
        role.getPermisions().add(permission);

        Optional<Permision> result = roleService.getPermissionById(role, 1L);

        assertTrue(result.isPresent());
        assertEquals(permission, result.get());
    }

    @Test
    void getPermissionById_returnsEmptyOptional_whenPermissionNotFound() throws RoleServiceException {
        Role role = new Role();
        role.setId(1L);
        Permision permission = new Permision();
        permission.setId(1L);

        Optional<Permision> result = roleService.getPermissionById(role, 2L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getPermissionById_throwsRoleServiceException_whenRoleIsNull() {
        assertThrows(RoleServiceException.class, () -> roleService.getPermissionById(null, 1L));
    }

    @Test
    void findById_returnsRole_whenValidRoleIdGiven() throws RoleServiceException {
        Role role = new Role();
        role.setId(1L);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Role result = roleService.findById(1);

        assertEquals(role, result);
    }

    @Test
    void findById_throwsRoleServiceException_whenRoleNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoleServiceException.class, () -> roleService.findById(1));
    }

    @Test
    void findById_throwsRoleServiceException_whenInvalidRoleIdGiven() {
        assertThrows(RoleServiceException.class, () -> roleService.findById(-1));
    }

}

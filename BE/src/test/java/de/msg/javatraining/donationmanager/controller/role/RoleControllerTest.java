package de.msg.javatraining.donationmanager.controller.role;

import de.msg.javatraining.donationmanager.persistence.model.ERole;
import de.msg.javatraining.donationmanager.persistence.model.PRole;
import de.msg.javatraining.donationmanager.persistence.model.Permision;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.service.role.IRoleService;
import de.msg.javatraining.donationmanager.service.role.RoleServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @Mock
    private IRoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @Test
    void addPermissionToRole() throws RoleServiceException {
        int roleId = 1;
        Permision permission = createTestPermission();

        Role role = createTestRole(roleId);

        when(roleService.findById(roleId)).thenReturn(role);
        when(roleService.addPermission(role, permission)).thenReturn(true);

        ResponseEntity<?> response = roleController.addPermissionToRole(roleId, permission);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Permission added successfully", response.getBody());
    }

    @Test
    void addPermissionToRole_InvalidPermission() throws RoleServiceException {
        int roleId = 1;

        Role role = createTestRole(roleId);

        when(roleService.findById(roleId)).thenReturn(role);
        when(roleService.addPermission(role, null)).thenThrow(new RoleServiceException("Permission not found.", "NO_PERMISSION_FOUND"));

        ResponseEntity<?> response = roleController.addPermissionToRole(roleId, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Permission not found.", response.getBody());
    }


    @Test
    void addPermissionToRole_RoleNotFound() throws RoleServiceException {
        int roleId = 1;
        Permision permission = createTestPermission();

        when(roleService.findById(roleId)).thenThrow(new RoleServiceException("Role not found", "ROLE_NOT_FOUND"));

        ResponseEntity<?> response = roleController.addPermissionToRole(roleId, permission);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletePermissionFromRole() throws RoleServiceException {
        int roleId = 1;
        long permissionId = 1L;

        Permision permission = createTestPermission();
        Role role = createTestRole(roleId);

        when(roleService.findById(roleId)).thenReturn(role);
        when(roleService.getPermissionById(role, permissionId)).thenReturn(Optional.of(permission));
        when(roleService.deletePermission(role, permission)).thenReturn(true);

        ResponseEntity<?> response = roleController.deletePermissionFromRole(roleId, permissionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Permission deleted successfully", response.getBody());
    }

    @Test
    void deletePermissionFromRole_PermissionNotFound() throws RoleServiceException {
        int roleId = 1;
        long permissionId = 1L;

        Role role = createTestRole(roleId);

        when(roleService.findById(roleId)).thenReturn(role);
        when(roleService.getPermissionById(role, permissionId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = roleController.deletePermissionFromRole(roleId, permissionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Permission not found.", response.getBody());
    }

    @Test
    void deletePermissionFromRole_RoleNotFound() throws RoleServiceException {
        int roleId = 1;
        Long permissionId = 1L;

        when(roleService.findById(roleId)).thenThrow(new RoleServiceException("Role not found", "ROLE_NOT_FOUND"));

        ResponseEntity<?> response = roleController.deletePermissionFromRole(roleId, permissionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getPermissionById() throws RoleServiceException {
        int roleId = 1;
        long permissionId = 1L;

        Permision permission = createTestPermission();
        Role role = createTestRole(roleId);

        when(roleService.findById(roleId)).thenReturn(role);
        when(roleService.getPermissionById(role, permissionId)).thenReturn(Optional.of(permission));

        ResponseEntity<?> response = roleController.getPermissionById(roleId, permissionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(permission, response.getBody());
    }

    @Test
    void getPermissionById_PermissionNotFound() throws RoleServiceException {
        int roleId = 1;
        long permissionId = 1L;

        Role role = createTestRole(roleId);

        when(roleService.findById(roleId)).thenReturn(role);
        when(roleService.getPermissionById(role, permissionId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = roleController.getPermissionById(roleId, permissionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Permission not found.", response.getBody());
    }

    @Test
    void getPermissionById_RoleNotFound() throws RoleServiceException {
        int roleId = 1;
        Long permissionId = 1L;

        when(roleService.findById(roleId)).thenThrow(new RoleServiceException("Role not found", "ROLE_NOT_FOUND"));

        ResponseEntity<?> response = roleController.getPermissionById(roleId, permissionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findById() throws RoleServiceException {
        int roleId = 1;
        Role role = createTestRole(roleId);

        when(roleService.findById(roleId)).thenReturn(role);

        ResponseEntity<?> response = roleController.findById(roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role, response.getBody());
    }

    @Test
    void findById_RoleNotFound() throws RoleServiceException {
        int roleId = 1;

        when(roleService.findById(roleId)).thenThrow(new RoleServiceException("Role not found", "ROLE_NOT_FOUND"));

        ResponseEntity<?> response = roleController.findById(roleId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private Role createTestRole(int id) {
        Role role = new Role();
        role.setId((long) id);
        role.setName(ERole.ROLE_ADM);
        return role;
    }

    private Permision createTestPermission() {
        Permision permission = new Permision();
        permission.setId(1L);
        permission.setName(PRole.PERMISSION_MANAGEMENT);
        return permission;
    }
}

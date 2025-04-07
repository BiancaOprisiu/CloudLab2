package de.msg.javatraining.donationmanager.controller.role;

import de.msg.javatraining.donationmanager.persistence.model.Permision;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.service.permision.PermisionServiceException;
import de.msg.javatraining.donationmanager.service.role.IRoleService;
import de.msg.javatraining.donationmanager.service.role.RoleServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping("/roles")
    public ResponseEntity<?> findAll() {
        try {
            List<Role> roles = roleService.findAll();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/roles/{roleId}/permissions")
    @PreAuthorize("hasAuthority('PERMISSION_MANAGEMENT')")
    public ResponseEntity<?> addPermissionToRole(
            @PathVariable Integer roleId,
            @RequestBody Permision permission) {
        try {
            Role role = roleService.findById(roleId);
            roleService.addPermission(role, permission);
            return ResponseEntity.status(HttpStatus.CREATED).body("Permission added successfully");
        } catch (RoleServiceException e) {
            if ("NO_PERMISSION_FOUND".equals(e.getErrorCode())) {
                return new ResponseEntity<>("Permission not found.", HttpStatus.NOT_FOUND);
            } else if ("PERMISSION_ALREADY_ASSOCIATED".equals(e.getErrorCode())) {
                return new ResponseEntity<>("Permission is already associated with the role.", HttpStatus.FORBIDDEN);
            } else {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @DeleteMapping("/roles/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('PERMISSION_MANAGEMENT')")
    public ResponseEntity<?> deletePermissionFromRole(@PathVariable Integer roleId, @PathVariable Long permissionId) {
        try {
            Role role = roleService.findById(roleId);
            Optional<Permision> permission = roleService.getPermissionById(role, permissionId);
            if (permission.isPresent()) {
                boolean deleted = roleService.deletePermission(role, permission.get());
                if (deleted) {
                    return ResponseEntity.ok("Permission deleted successfully");
                }
                return new ResponseEntity<>("Permission could not be deleted", HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>("Permission not found.", HttpStatus.NOT_FOUND);
        } catch (RoleServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/roles/{roleId}/permissions/{permissionId}")
    public ResponseEntity<?> getPermissionById(@PathVariable Integer roleId, @PathVariable Long permissionId) {
        try {
            Role role = roleService.findById(roleId);
            Optional<Permision> permission = roleService.getPermissionById(role, permissionId);
            if (permission.isPresent()) {
                return ResponseEntity.ok(permission.get());
            }
            return new ResponseEntity<>("Permission not found.", HttpStatus.NOT_FOUND);
        } catch (RoleServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/roles/{roleId}")
    public ResponseEntity<?> findById(@PathVariable Integer roleId) {
        try {
            Role role = roleService.findById(roleId);
            return ResponseEntity.ok(role);
        } catch (RoleServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

package de.msg.javatraining.donationmanager.validation;

import de.msg.javatraining.donationmanager.persistence.model.Permision;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.service.role.RoleServiceException;

public class RoleValidation {

    public static void validateRoleNotNull(Role role) throws RoleServiceException {
        if (role == null) {
            throw new RoleServiceException("Role is null", "ROLE_NULL");
        }
    }

    public static void validatePermissionNotNull(Permision permission) throws RoleServiceException {
        if (permission == null) {
            throw new RoleServiceException("Permission is null", "PERMISSION_NULL");
        }
    }

    public static void validatePermissionNotAssociated(Role role, Permision permission) throws RoleServiceException {
        if (role.getPermisions().contains(permission)) {
            throw new RoleServiceException("Permission is already associated with the role", "PERMISSION_ALREADY_ASSOCIATED");
        }
    }
}


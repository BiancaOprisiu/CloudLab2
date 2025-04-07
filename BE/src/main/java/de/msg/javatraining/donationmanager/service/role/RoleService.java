package de.msg.javatraining.donationmanager.service.role;

import de.msg.javatraining.donationmanager.persistence.model.Permision;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.persistence.repository.PermisionRepository;
import de.msg.javatraining.donationmanager.persistence.repository.RoleRepository;
import de.msg.javatraining.donationmanager.validation.RoleValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class RoleService implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermisionRepository permisionRepository;

    @Override
    public List<Role> findAll() throws RoleServiceException {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            throw new RoleServiceException("No roles found", "NO_ROLES_FOUND");
        }
        return roles;
    }

    @Override
    public boolean addPermission(Role role, Permision permission) throws RoleServiceException {
        RoleValidation.validateRoleNotNull(role);
        RoleValidation.validatePermissionNotNull(permission);

        Permision existingPermission = permisionRepository.findByName(permission.getName());

        if (existingPermission == null) {
            throw new RoleServiceException("Permission not found", "PERMISSION_NOT_FOUND");
        }

        RoleValidation.validatePermissionNotAssociated(role, existingPermission);

        role.getPermisions().add(existingPermission);
        roleRepository.save(role);
        return true;
    }

    @Override
    public boolean deletePermission(Role role, Permision permission) throws RoleServiceException {
        RoleValidation.validateRoleNotNull(role);
        RoleValidation.validatePermissionNotNull(permission);

        boolean removed = role.getPermisions().remove(permission);
        if (removed) {
            roleRepository.save(role);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Permision> getPermissionById(Role role, long id) throws RoleServiceException {
        RoleValidation.validateRoleNotNull(role);

        return role.getPermisions().stream()
                .filter(permission -> permission.getId() == id)
                .findFirst();
    }

    @Override
    public Role findById(Integer roleId) throws RoleServiceException {
        Optional<Role> optionalRole = roleRepository.findById(Long.valueOf(roleId));
        if (optionalRole.isEmpty()) {
            throw new RoleServiceException("Role not found", "ROLE_NOT_FOUND");
        }
        return optionalRole.get();
    }
}


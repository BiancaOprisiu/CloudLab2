package de.msg.javatraining.donationmanager.service.role;

import de.msg.javatraining.donationmanager.persistence.model.Permision;
import de.msg.javatraining.donationmanager.persistence.model.Role;
import de.msg.javatraining.donationmanager.service.donation.DonationServiceException;

import java.util.List;
import java.util.Optional;


public interface IRoleService {

    List<Role> findAll() throws RoleServiceException;

    /**
     * Adds a permission to a role.
     *
     * @param role       The role to which the permission will be added.
     * @param permission The permission to add.
     * @return True if the permission is successfully added, false otherwise.
     * @throws RoleServiceException If the role is null, the permission is null,
     *                             the permission is already associated with the role,
     *                             or the permission does not exist.
     */
    boolean addPermission(Role role, Permision permission) throws RoleServiceException;

    /**
     * Deletes a permission from a role.
     *
     * @param role       The role from which to delete the permission.
     * @param permission The permission to delete.
     * @return True if the permission is successfully deleted, false otherwise.
     * @throws RoleServiceException If the role is null, the permission is null,
     *                             or the role does not have the permission.
     */
    boolean deletePermission(Role role, Permision permission) throws RoleServiceException;

    /**
     * Retrieves a permission by its ID from a role.
     *
     * @param role The role from which to retrieve the permission.
     * @param id   The ID of the permission to retrieve.
     * @return The retrieved permission, if found.
     * @throws RoleServiceException If the role is null, the permission is not found,
     *                             or the ID is invalid.
     */
    Optional<Permision> getPermissionById(Role role, long id) throws RoleServiceException;

    /**
     * Retrieves a role by its ID.
     *
     * @param roleId The ID of the role to retrieve.
     * @return The retrieved role.
     * @throws RoleServiceException If the role is not found or the ID is invalid.
     */
    Role findById(Integer roleId) throws RoleServiceException;
}

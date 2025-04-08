package de.msg.javatraining.donationmanager.service.user;

import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.service.role.RoleServiceException;

import java.util.List;

/**
 * Service interface for managing user-related operations.
 */
public interface IUserService {
    /**
     * Retrieves a list of all users.
     *
     * @return List of User objects.
     * @throws UserServiceException if no users are found.
     */
    List<User> findAll() throws UserServiceException;

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The User object with the given ID.
     * @throws UserServiceException if the user is not found.
     */
    User findById(Long id) throws UserServiceException;

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @throws UserServiceException if the user is not found.
     */
    void deleteById(Long id) throws UserServiceException;

    /**
     * Creates a new user.
     *
     * @param user The User object to create.
     * @return The created User object.
     * @throws UserServiceException if the user already exists.
     */
    User createUser(User user) throws UserServiceException;

    /**
     * Updates a user by their ID.
     *
     * @param id           The ID of the user to update.
     * @param updatedUser  The updated User object.
     * @return The updated User object.
     * @throws UserServiceException if the user is not found.
     */
    User updateUserById(Long id, User updatedUser) throws UserServiceException, RoleServiceException;

}


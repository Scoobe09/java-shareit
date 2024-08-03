package ru.practicum.shareit.user;

import java.util.List;

/**
 * The UserService interface defines the operations that can be performed on users within the application.
 * It provides methods for creating, updating, retrieving, and deleting users, as well as listing all users
 * and checking if a user exists by their ID.
 */
public interface UserService {

    /**
     * Adds a new user to the system.
     *
     * @param userDTO the user to be added
     * @return the added user
     */
    UserDTO addUser(UserDTO userDTO);

    /**
     * Updates an existing user.
     *
     * @param id   the ID of the user to be updated
     * @param user the updated user information
     * @return the updated user
     */
    UserDTO updateUser(final Long id, UserDTO user);

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the retrieved user, represented as a {@link UserDTO} object
     */
    UserDTO getById(final Long id);

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     */
    void deleteUser(final Long id);

    /**
     * Retrieves a list of all users in the application.
     *
     * @return a list of all users, each represented as a {@link UserDTO}
     */
    List<UserDTO> getAll(Integer from, Integer size);

    /**
     * Checks if a user exists by their ID.
     *
     * @param id The ID of the user to check.
     * @return True if the user exists, false otherwise.
     */
    boolean isExistUser(final Long id);
}

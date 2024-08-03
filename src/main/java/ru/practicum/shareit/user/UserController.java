package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

import static ru.practicum.shareit.utils.Constant.LIMIT;
import static ru.practicum.shareit.utils.Constant.INITIAL_X;
import static ru.practicum.shareit.utils.Marker.OnCreate;
import static ru.practicum.shareit.utils.Marker.OnUpdate;


/**
 * The UserController class handles HTTP requests related to user operations.
 * It serves as a RESTful API controller for managing users.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    /**
     * Creates a new user.
     *
     * @param userDTO the user to be created
     * @return the created user
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@Validated(OnCreate.class) @RequestBody UserDTO userDTO) {
        return userService.addUser(userDTO);
    }

    /**
     * Updates an existing user's information.
     *
     * @param userId  the ID of the user to be updated
     * @param userDTO the updated user information
     * @return the updated user
     */
    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUser(@PathVariable Long userId,
                              @Validated(OnUpdate.class) @RequestBody UserDTO userDTO) {
        return userService.updateUser(userId, userDTO);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the retrieved user, represented as a {@link UserDTO}
     */
    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    /**
     * Retrieves a list of all users in the application.
     *
     * @return a list of all users, each represented as a {@link UserDTO}
     */
    @GetMapping
    public List<UserDTO> getAll(@Positive @RequestParam(defaultValue = INITIAL_X) Integer from,
                                @Positive @RequestParam(defaultValue = LIMIT) Integer size) {
        return userService.getAll(from, size);
    }
}
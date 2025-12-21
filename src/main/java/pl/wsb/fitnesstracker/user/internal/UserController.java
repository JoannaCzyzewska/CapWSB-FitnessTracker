package pl.wsb.fitnesstracker.user.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wsb.fitnesstracker.user.api.UserDto;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * UserController is responsible for handling HTTP requests related to user operations.
 * It provides endpoints for retrieving and creating users.
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    /**
     * Retrieves a list of all users.
     *
     * @return a list of {@link UserDto} representing all users
     */
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a list of simple user representations containing only ID and name.
     *
     * @return a list of {@link UserSimpleDto} with minimal user info
     */
    @GetMapping("/simple")
    public List<UserSimpleDto> getAllUsersIDNames() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toSimpleDto)
                .toList();
    }

    /**
     * Retrieves a specific user by their unique ID.
     *
     * @param id the ID of the user to retrieve
     * @return a {@link UserDto} representing the found user
     * @throws UserNotFoundException if no user exists with the given ID
     */
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the ID of the user to delete
     */
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    /**
     * Creates a new user from the provided request data.
     *
     * @param userRequest the DTO containing user creation data
     * @return a {@link UserDto} representing the newly created user
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserRequestDto userRequest) {
        return userService.createUser(userRequest);
    }

    /**
     * Searches for users whose email contains the given fragment (case-insensitive).
     *
     * @param emailFragment partial email string to search for
     * @return a list of {@link UserIdEmailDto} matching the email fragment
     */
    @GetMapping("/email/{emailFragment}")
    public List<UserIdEmailDto> getUsersByEmailFragment(@PathVariable String emailFragment) {
        return userService.getUsersByEmailFragment(emailFragment)
                .stream()
                .map(userMapper::toUserIdEmailDto)
                .toList();
    }

    /**
     * Retrieves all users older than the specified age.
     *
     * @param age the minimum age (exclusive) for user selection
     * @return a list of {@link UserDto} for users older than {@code age}
     */
    @GetMapping("/older/{age}")
    public List<UserDto> getUsersOlderThan(@PathVariable int age) {
        return userService.getUsersOlderThan(age)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Updates the name of an existing user.
     *
     * @param id the ID of the user to update
     * @param name the new name to assign
     * @return a {@link UserDto} representing the updated user
     */
    @PutMapping("/{id}")
    public UserDto updateUserName(@PathVariable Long id,
                                  @RequestParam String name) {
        User updatedUser = userService.updateUserName(id, name);

        return userMapper.toDto(updatedUser);
    }

}


package pl.wsb.fitnesstracker.user.internal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.user.api.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    UserServiceImpl(final UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User createUser(final User user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the ID of the user to retrieve
     * @return an {@link Optional} containing the found {@link User}, or empty if not found
     */
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @throws UserNotFoundException if no user exists with the given ID
     */
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Creates a new user from the provided request data.
     *
     * @param userRequest the DTO containing user creation details
     * @return a {@link UserDto} representing the newly created user
     */
    public UserDto createUser(UserRequestDto userRequest) {
        User entity = userMapper.toEntity(userRequest);
        User savedEntity = userRepository.save(entity);
        return userMapper.toDto(savedEntity);
    }

    /**
     * Finds users whose email addresses contain the given fragment (case-insensitive).
     *
     * @param fragment the partial email string to search for
     * @return a list of {@link User} entities matching the email fragment
     */
    public List<User> getUsersByEmailFragment(String fragment) {
        return userRepository.findByEmailContainingIgnoreCase(fragment);
    }

    /**
     * Retrieves all users older than the specified age.
     *
     * @param age the minimum age (exclusive) for user selection
     * @return a list of {@link User} entities whose birthdate is before {@code LocalDate.now().minusYears(age)}
     */
    public List<User> getUsersOlderThan(int age) {
        LocalDate date = LocalDate.now().minusYears(age);
        return userRepository.findByBirthdateBefore(date);
    }

    /**
     * Updates the first name of an existing user.
     *
     * @param id the ID of the user to update
     * @param name the new first name
     * @return the updated {@link User} entity
     * @throws UserNotFoundException if no user exists with the given ID
     */
    public User updateUserName(Long id, String name) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setFirstName(name);
        return userRepository.save(user);
    }

}
package pl.wsb.fitnesstracker.user.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wsb.fitnesstracker.user.api.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Query searching users by email address. It matches by exact match.
     *
     * @param email email of the user to search
     * @return {@link Optional} containing found user or {@link Optional#empty()} if none matched
     */
    default Optional<User> findByEmail(String email) {
        return findAll().stream()
                .filter(user -> Objects.equals(user.getEmail(), email))
                .findFirst();
    }

    /**
     * Finds users whose email addresses contain the specified fragment, ignoring case.
     *
     * @param fragment the partial email string to search for
     * @return a list of {@link User} entities whose email contains the fragment (case-insensitive)
     */
    List<User> findByEmailContainingIgnoreCase(String fragment);

    /**
     * Finds users whose birthdate is strictly before the given date.
     *
     * @param date the cutoff date (exclusive)
     * @return a list of {@link User} entities born before the specified date
     */
    List<User> findByBirthdateBefore(LocalDate date);



}

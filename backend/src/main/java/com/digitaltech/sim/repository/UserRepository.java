package com.digitaltech.sim.repository;

import com.digitaltech.sim.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username Username to search for.
     * @return Optional containing the user if found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks whether a user with the given username already exists.
     *
     * @param username Username to check.
     * @return {@code true} if the username is taken, {@code false} otherwise.
     */
    boolean existsByUsername(String username);
}

package com.digitaltech.sim.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing an application user used for authentication
 * and authorization purposes within the system.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    /**
     * Unique identifier of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique username used to log into the system.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Encrypted password used for authentication.
     */
    @Column(nullable = false)
    private String password;
}

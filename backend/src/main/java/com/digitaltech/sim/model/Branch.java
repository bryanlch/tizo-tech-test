package com.digitaltech.sim.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Entity representing a physical branch or subsidiary in the system.
 */
@Entity
@Table(name = "branch")
@Getter
@Setter
@NoArgsConstructor
public class Branch {

    /**
     * Unique identifier for the branch.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Display name of the branch.
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Physical address of the branch.
     */
    @Column(nullable = false, length = 255)
    private String address;
}

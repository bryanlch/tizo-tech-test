package com.digitaltech.sim.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Entity representing a physical branch or store location
 * where products can be stored and sold.
 */
@Entity
@Table(name = "branch")
@Getter
@Setter
@NoArgsConstructor
public class Branch {

    /**
     * Unique identifier of the branch.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the branch.
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Physical address where the branch is located.
     */
    @Column(nullable = false, length = 255)
    private String address;
}

package com.digitaltech.sim.repository;

import com.digitaltech.sim.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository for the Branch entity.
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    /**
     * Checks if a branch exists by its exact name.
     * 
     * @param name Name to check.
     * @return true if it exists, false otherwise.
     */
    boolean existsByName(String name);
}

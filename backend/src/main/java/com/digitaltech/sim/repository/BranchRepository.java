package com.digitaltech.sim.repository;

import com.digitaltech.sim.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for the Branch entity.
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    /**
     *
     * @param name Name to check.
     * @return true if it exists, false otherwise.
     */
    boolean existsByName(String name);
    
    /**
     * Checks if an active branch exists by its exact name.
     *
     * @param name Name to check.
     * @return true if it exists and is active, false otherwise.
     */
    boolean existsByNameAndStatusTrue(String name);

    /**
     * Returns only branches that have not been soft-deleted.
     *
     * @return List of active branches.
     */
    List<Branch> findAllByStatusTrue();

    /**
     * Finds a branch by its ID only if it is currently active.
     * Returns empty when the branch is soft-deleted or does not exist.
     *
     * @param id Branch identifier.
     * @return Optional containing the active branch, or empty.
     */
    Optional<Branch> findByIdAndStatusTrue(Long id);
}

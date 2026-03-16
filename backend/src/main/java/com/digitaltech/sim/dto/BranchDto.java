package com.digitaltech.sim.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for branch operations.
 */
@Data
public class BranchDto {

    /**
     * Unique branch identifier. Null when creating a new branch.
     */
    private Long id;

    /**
     * Branch name.
     */
    @NotBlank(message = "Branch name is required")
    @Size(min = 2, max = 100, message = "Branch name must be between 2 and 100 characters")
    private String name;

    /**
     * Branch address.
     */
    @NotBlank(message = "Branch address is required")
    @Size(min = 5, max = 255, message = "Branch address must be between 5 and 255 characters")
    private String address;

    /**
     * Indicates whether the branch is currently active.
     * Will be {@code false} if the branch has been soft-deleted.
     */
    private Boolean active;
}
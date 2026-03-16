package com.digitaltech.sim.service;

import com.digitaltech.sim.dto.ApiResponse;
import com.digitaltech.sim.dto.BranchDto;
import com.digitaltech.sim.model.Branch;

import java.util.List;

public interface BranchService {

    /** Returns all active (non-deleted) branches. */
    ApiResponse< List<BranchDto>> getAllBranches();

    /** Creates a new branch. */
    ApiResponse<BranchDto> createBranch(BranchDto branchDto);

    /**
     * Performs a soft delete on the branch with the given ID
     * by setting its {@code active} flag to {@code false}.
     *
     * @param id Branch identifier.
     * @throws RuntimeException if the branch does not exist or is already deleted.
     */
    void changeStatusBranch(Long id);

    /**
     * Updates the {@code name} and {@code address} of an existing active branch.
     *
     * @param id  Branch identifier.
     * @param dto DTO holding the new name and address values.
     * @return Updated BranchDto.
     * @throws RuntimeException if the branch does not exist or has been soft-deleted.
     */
    ApiResponse<BranchDto> updateBranch(Long id, BranchDto dto);
}


package com.digitaltech.sim.service.impl;

import com.digitaltech.sim.dto.ApiResponse;
import com.digitaltech.sim.dto.BranchDto;
import com.digitaltech.sim.model.Branch;
import com.digitaltech.sim.repository.BranchRepository;
import com.digitaltech.sim.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the BranchService for managing branch business logic.
 */
@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    /**
     * Fetches all registered branches that have not been soft-deleted.
     *
     * @return List of active BranchDtos.
     */
    @Override
    public ApiResponse<List<BranchDto>> getAllBranches() {
        var result = branchRepository.findAllByStatusTrue()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return ApiResponse.success(result, "Branches retrieved successfully");
    }

    /**
     * Registers a new branch in the system.
     *
     * @param branchDto Data for the new branch.
     * @return The created BranchDto.
     * @throws IllegalArgumentException if a branch with the same name exists.
     */
    @Override
    public ApiResponse<BranchDto> createBranch(BranchDto branchDto) {
        if (branchRepository.existsByName(branchDto.getName())) {
            throw new IllegalArgumentException(
                    "A branch with this name already exists: " + branchDto.getName()
            );
        }
        var branch = Branch.builder()
                .name(branchDto.getName())
                .address(branchDto.getAddress())
                .status(true)
                .build();

        Branch savedBranch = branchRepository.save(branch);
        return ApiResponse.success( toDto(savedBranch) , "Branch created successfully");
    }

    /**
     * Performs a soft delete on the branch with the given ID.
     * Sets {@code active = false} without removing the database record.
     *
     * @param id Branch identifier.
     * @throws RuntimeException if the branch does not exist or is already deleted.
     */
    @Async
    @Override
    public void changeStatusBranch(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found or already deleted: " + id));

        branch.setStatus(!branch.getStatus());
        branchRepository.save(branch);
    }

    /**
     * Updates the {@code name} and {@code address} of an existing active branch.
     *
     * @param id  Branch identifier.
     * @param dto DTO holding the new name and address values.
     * @return Updated BranchDto.
     * @throws RuntimeException if the branch does not exist or has been soft-deleted.
     */
    @Override
    public ApiResponse<BranchDto> updateBranch(Long id, BranchDto dto) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found or inactive: " + id));

        branch.setName(dto.getName());
        branch.setAddress(dto.getAddress());

        Branch updatedBranch = branchRepository.save(branch);
        return ApiResponse.success(toDto(updatedBranch), "Branch updated successfully");
    }

    private BranchDto toDto(Branch branch) {
        BranchDto dto = new BranchDto();
        dto.setId(branch.getId());
        dto.setName(branch.getName());
        dto.setAddress(branch.getAddress());
        dto.setActive(branch.getStatus());
        return dto;
    }
}


package com.digitaltech.sim.controller;

import com.digitaltech.sim.dto.ApiResponse;
import com.digitaltech.sim.dto.BranchDto;
import com.digitaltech.sim.service.impl.BranchServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing branches.
 */
@RestController
@RequestMapping("/api/branch")
@RequiredArgsConstructor
public class BranchController {

    private final BranchServiceImpl branchService;

    /**
     * Retrieves all active branches.
     *
     * @return List of active branches within ApiResponse.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BranchDto>>> getAllBranches() {
        return this.branchService.getAllBranches().toHttp();
    }

    /**
     * Creates a new branch.
     *
     * @param dto Branch data.
     * @return Created branch within ApiResponse.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BranchDto>> createBranch(@Valid @RequestBody BranchDto dto) {
        return this.branchService.createBranch(dto).toHttp();
    }

    /**
     * Performs a logical (soft) delete on the branch with the given ID.
     * The branch record is NOT removed from the database; its {@code active}
     * flag is set to {@code false} instead.
     *
     * @param id Branch identifier.
     * @return Empty ApiResponse confirming the deletion.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> changeStatusBranch(@PathVariable Long id) {
        this.branchService.changeStatusBranch(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Branch deleted successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the name and address of an existing active branch.
     *
     * @param id  Branch identifier.
     * @param dto DTO containing the new name and address.
     * @return Updated branch within ApiResponse.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchDto>> updateBranch(
            @PathVariable Long id,
            @Valid @RequestBody BranchDto dto) {
        return this.branchService.updateBranch(id, dto).toHttp();
    }
}


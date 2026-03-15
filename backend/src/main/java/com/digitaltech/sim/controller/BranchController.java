package com.digitaltech.sim.controller;

import com.digitaltech.sim.dto.ApiResponse;
import com.digitaltech.sim.dto.BranchDto;
import com.digitaltech.sim.service.impl.BranchServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
     * Retrieves all branches.
     * @return List of branches within ApiResponse.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BranchDto>>> getAllBranches() {
        List<BranchDto> data = this.branchService.getAllBranches();
        ApiResponse<List<BranchDto>> response = ApiResponse.success(data, "Branches retrieved successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new branch.
     * @param dto Branch data.
     * @return Created branch within ApiResponse.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BranchDto>> createBranch(@Valid @RequestBody BranchDto dto) {
        BranchDto created = this.branchService.createBranch(dto);
        ApiResponse<BranchDto> response = ApiResponse.success(created, "Branch created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

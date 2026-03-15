package com.digitaltech.sim.service.impl;

import com.digitaltech.sim.dto.BranchDto;
import com.digitaltech.sim.model.Branch;
import com.digitaltech.sim.repository.BranchRepository;
import com.digitaltech.sim.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the BranchService for managing branch business logic.
 */
@Service
@RequiredArgsConstructor
public class BranchServiceImpl  implements BranchService {

    private final BranchRepository branchRepository;

    /**
     * Fetches all registered branches.
     * @return List of BranchDto.
     */
    @Override
    public List<BranchDto> getAllBranches() {
        return branchRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Registers a new branch in the system.
     * @param branchDto Data for the new branch.
     * @return The created BranchDto.
     * @throws IllegalArgumentException if a branch with the same name exists.
     */
    @Override
    public BranchDto createBranch(BranchDto branchDto) {
        if (branchRepository.existsByName(branchDto.getName())) {
            throw new IllegalArgumentException(
                    "A branch with this name already exists: " + branchDto.getName()
            );
        }

        Branch branch = new Branch();
        branch.setName(branchDto.getName());
        branch.setAddress(branchDto.getAddress());

        Branch savedBranch = branchRepository.save(branch);

        return toDto(savedBranch);
    }

    private BranchDto toDto(Branch branch) {
        BranchDto dto = new BranchDto();
        dto.setId(branch.getId());
        dto.setName(branch.getName());
        dto.setAddress(branch.getAddress());
        return dto;
    }
}

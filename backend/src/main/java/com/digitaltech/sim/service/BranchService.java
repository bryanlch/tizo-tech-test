package com.digitaltech.sim.service;

import com.digitaltech.sim.dto.BranchDto;
import com.digitaltech.sim.model.Branch;

import java.util.List;

public interface BranchService {
    List<BranchDto> getAllBranches();
    BranchDto createBranch(BranchDto branchDto);
}

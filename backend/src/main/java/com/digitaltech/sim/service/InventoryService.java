package com.digitaltech.sim.service;

import com.digitaltech.sim.dto.ApiResponse;
import com.digitaltech.sim.dto.InventoryDto;
import com.digitaltech.sim.dto.SettingInventoryDto;

import java.util.List;

public interface InventoryService {
    ApiResponse<List<InventoryDto>> getInventoryByBranch(Long branchId);
    ApiResponse<InventoryDto> settingStock(SettingInventoryDto dto);
}

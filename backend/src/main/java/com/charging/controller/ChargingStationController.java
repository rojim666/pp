package com.charging.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.charging.common.result.Result;
import com.charging.dto.request.StationRequest;
import com.charging.dto.response.StationResponse;
import com.charging.service.ChargingStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 充电桩控制器
 */
@Tag(name = "充电桩接口")
@RestController
@RequestMapping("/stations")
@RequiredArgsConstructor
public class ChargingStationController {

    private final ChargingStationService stationService;

    @Operation(summary = "获取充电桩列表")
    @GetMapping
    public Result<IPage<StationResponse>> listStations(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status
    ) {
        IPage<StationResponse> pageResult = stationService.listStations(page, size, keyword, type, status);
        
        // 权限控制：非管理员隐藏敏感数据
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Long) {
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    
            if (!isAdmin) {
                pageResult.getRecords().forEach(station -> {
                    station.setTotalRevenue(null);
                    // 可以根据需求隐藏其他字段
                });
            }
        }
        
        return Result.success(pageResult);
    }

    @Operation(summary = "获取充电桩详情")
    @GetMapping("/{id}")
    public Result<StationResponse> getStation(@PathVariable Long id) {
        return Result.success(stationService.getStationById(id));
    }

    @Operation(summary = "创建充电桩")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public Result<StationResponse> createStation(@RequestBody @Validated StationRequest request) {
        return Result.success(stationService.createStation(request));
    }

    @Operation(summary = "更新充电桩")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public Result<StationResponse> updateStation(
            @PathVariable Long id,
            @RequestBody @Validated StationRequest request
    ) {
        return Result.success(stationService.updateStation(id, request));
    }

    @Operation(summary = "删除充电桩")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStation(id);
        return Result.success();
    }

    @Operation(summary = "更新充电桩状态")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public Result<Void> updateStationStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        stationService.updateStationStatus(id, status);
        return Result.success();
    }
}

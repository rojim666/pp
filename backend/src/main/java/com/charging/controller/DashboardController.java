package com.charging.controller;

import com.charging.common.result.Result;
import com.charging.dto.response.DashboardResponse;
import com.charging.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘控制器
 */
@Tag(name = "仪表盘接口")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "获取仪表盘统计数据")
    @GetMapping("/stats")
    public Result<DashboardResponse> getDashboardStats() {
        return Result.success(dashboardService.getDashboardStats());
    }
}

package com.charging.controller;

import com.charging.common.result.Result;
import com.charging.entity.TariffConfig;
import com.charging.service.TariffConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 费率配置控制器
 */
@Tag(name = "费率配置接口")
@RestController
@RequestMapping("/tariff")
@RequiredArgsConstructor
public class TariffConfigController {

    private final TariffConfigService tariffConfigService;

    @Operation(summary = "获取当前费率配置")
    @GetMapping("/current")
    public Result<TariffConfig> getCurrentConfig() {
        return Result.success(tariffConfigService.getCurrentConfig());
    }

    @Operation(summary = "更新费率配置")
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public Result<TariffConfig> updateConfig(@RequestBody TariffConfig config) {
        return Result.success(tariffConfigService.updateConfig(config));
    }
}

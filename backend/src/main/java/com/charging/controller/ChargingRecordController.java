package com.charging.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.charging.common.result.Result;
import com.charging.dto.request.RecordRequest;
import com.charging.dto.response.RecordResponse;
import com.charging.service.ChargingRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 充电记录控制器
 */
@Tag(name = "充电记录接口")
@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class ChargingRecordController {

    private final ChargingRecordService recordService;

    @Operation(summary = "获取充电记录列表")
    @GetMapping
    public Result<IPage<RecordResponse>> listRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long userId
    ) {
        // 获取当前用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // 只有当Principal是Long类型时才进行权限控制（避免匿名用户等情况报错）
        if (auth != null && auth.getPrincipal() instanceof Long) {
            Long currentUserId = (Long) auth.getPrincipal();
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            
            // 非管理员只能查看自己的记录
            if (!isAdmin) {
                userId = currentUserId;
            }
        }
        
        return Result.success(recordService.listRecords(page, size, keyword, status, userId));
    }

    @Operation(summary = "获取充电记录详情")
    @GetMapping("/{id}")
    public Result<RecordResponse> getRecord(@PathVariable Long id) {
        return Result.success(recordService.getRecordById(id));
    }

    @Operation(summary = "开始充电")
    @PostMapping("/start")
    public Result<RecordResponse> startCharging(@RequestBody @Validated RecordRequest request) {
        return Result.success(recordService.startCharging(request));
    }

    @Operation(summary = "结束充电")
    @PostMapping("/{id}/end")
    public Result<RecordResponse> endCharging(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam Integer endSoc
    ) {
        return Result.success(recordService.endCharging(id, endTime, endSoc));
    }
}

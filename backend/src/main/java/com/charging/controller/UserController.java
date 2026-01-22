package com.charging.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.charging.common.result.Result;
import com.charging.dto.request.UserRequest;
import com.charging.dto.response.UserResponse;
import com.charging.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Tag(name = "用户接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "获取用户列表")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public Result<IPage<UserResponse>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role
    ) {
        return Result.success(userService.listUsers(page, size, keyword, role));
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    public Result<UserResponse> getUser(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/{id}")
    public Result<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody @Validated(UserRequest.Update.class) UserRequest request
    ) {
        return Result.success(userService.updateUser(id, request));
    }

    @Operation(summary = "用户充值")
    @PostMapping("/{id}/recharge")
    public Result<UserResponse> recharge(
            @PathVariable Long id,
            @RequestParam java.math.BigDecimal amount
    ) {
        return Result.success(userService.recharge(id, amount));
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
}

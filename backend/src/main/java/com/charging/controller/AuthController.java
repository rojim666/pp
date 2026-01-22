package com.charging.controller;

import com.charging.common.result.Result;
import com.charging.dto.request.LoginRequest;
import com.charging.dto.request.UserRequest;
import com.charging.dto.response.LoginResponse;
import com.charging.dto.response.UserResponse;
import com.charging.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Tag(name = "认证接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Validated LoginRequest request) {
        return Result.success(userService.login(request));
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<UserResponse> register(@RequestBody @Validated(UserRequest.Create.class) UserRequest request) {
        return Result.success(userService.register(request));
    }
}

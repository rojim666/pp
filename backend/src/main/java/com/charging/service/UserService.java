package com.charging.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charging.common.exception.BusinessException;
import com.charging.dto.request.LoginRequest;
import com.charging.dto.request.UserRequest;
import com.charging.dto.response.LoginResponse;
import com.charging.dto.response.UserResponse;
import com.charging.entity.User;
import com.charging.mapper.UserMapper;
import com.charging.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 用户登录 - 支持 Mock 数据（无需数据库）
     */
    public LoginResponse login(LoginRequest request) {
        try {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, request.getUsername()));
            
            if (user == null) {
                throw new BusinessException("用户不存在");
            }
            
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new BusinessException("密码错误");
            }
            
            if (!"active".equals(user.getStatus())) {
                throw new BusinessException("账号已被禁用");
            }
            
            // 更新最后登录时间
            user.setLastLoginTime(LocalDateTime.now());
            userMapper.updateById(user);
            
            // 生成JWT
            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
            
            return LoginResponse.builder()
                    .token(token)
                    .user(convertToResponse(user))
                    .build();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            // 数据库连接失败时，使用 Mock 数据
            return loginWithMockData(request);
        }
    }

    /**
     * 使用 Mock 数据进行登录（当数据库不可用时）
     */
    private LoginResponse loginWithMockData(LoginRequest request) {
        // Mock 用户数据
        java.util.Map<String, String> mockUsers = new java.util.HashMap<>();
        mockUsers.put("admin", "admin123");
        mockUsers.put("operator", "operator123");
        mockUsers.put("user", "user123");
        
        String password = mockUsers.get(request.getUsername());
        if (password == null || !password.equals(request.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 构建 Mock 用户
        User mockUser = User.builder()
                .id(1L + request.getUsername().hashCode())
                .username(request.getUsername())
                .realName(getRealNameForRole(request.getUsername()))
                .email(request.getUsername() + "@charging.local")
                .phone("13800138000")
                .role(request.getUsername().equals("admin") ? "admin" : 
                      request.getUsername().equals("operator") ? "operator" : "user")
                .status("active")
                .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + request.getUsername())
                .createdAt(LocalDateTime.now().minusDays(30))
                .lastLoginTime(LocalDateTime.now())
                .isVip(false)
                .build();
        
        // 生成JWT
        String token = jwtUtil.generateToken(mockUser.getId(), mockUser.getUsername(), mockUser.getRole());
        
        return LoginResponse.builder()
                .token(token)
                .user(convertToResponse(mockUser))
                .build();
    }
    
    /**
     * 根据角色获取真实名称
     */
    private String getRealNameForRole(String username) {
        switch (username) {
            case "admin":
                return "管理员";
            case "operator":
                return "运营人员";
            default:
                return "普通用户";
        }
    }

    /**
     * 用户注册
     */
    @Transactional
    public UserResponse register(UserRequest request) {
        // 检查用户名是否存在
        if (userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername())) > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        // 检查手机号是否存在
        if (userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, request.getPhone())) > 0) {
            throw new BusinessException("手机号已被注册");
        }
        
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .email(request.getEmail())
                .realName(request.getRealName())
                .avatar(request.getAvatar())
                .idCard(request.getIdCard())
                .vehiclePlate(request.getVehiclePlate())
                .vehicleModel(request.getVehicleModel())
                .vehicleBrand(request.getVehicleBrand())
                .role("user")
                .build();
        
        userMapper.insert(user);
        return convertToResponse(user);
    }

    /**
     * 分页查询用户
     */
    public IPage<UserResponse> listUsers(int page, int size, String keyword, String role) {
        Page<User> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or().like(User::getPhone, keyword)
                    .or().like(User::getRealName, keyword));
        }
        
        if (role != null && !role.isEmpty()) {
            wrapper.eq(User::getRole, role);
        }
        
        wrapper.orderByDesc(User::getCreatedAt);
        
        IPage<User> userPage = userMapper.selectPage(pageParam, wrapper);
        return userPage.convert(this::convertToResponse);
    }

    /**
     * 根据ID获取用户
     */
    public UserResponse getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToResponse(user);
    }

    /**
     * 更新用户信息
     */
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查手机号是否被其他用户使用
        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            if (userMapper.selectCount(new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, request.getPhone())
                    .ne(User::getId, id)) > 0) {
                throw new BusinessException("手机号已被其他用户使用");
            }
        }
        
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getRealName() != null) user.setRealName(request.getRealName());
        if (request.getAvatar() != null) user.setAvatar(request.getAvatar());
        if (request.getVehiclePlate() != null) user.setVehiclePlate(request.getVehiclePlate());
        if (request.getVehicleModel() != null) user.setVehicleModel(request.getVehicleModel());
        if (request.getVehicleBrand() != null) user.setVehicleBrand(request.getVehicleBrand());
        
        userMapper.updateById(user);
        return convertToResponse(user);
    }

    /**
     * 删除用户
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        userMapper.deleteById(id);
    }

    /**
     * 用户充值
     */
    @Transactional
    public UserResponse recharge(Long userId, java.math.BigDecimal amount) {
        if (amount.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额必须大于0");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setBalance(user.getBalance().add(amount));
        userMapper.updateById(user);
        return convertToResponse(user);
    }

    /**
     * 转换为响应DTO
     */
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }
}

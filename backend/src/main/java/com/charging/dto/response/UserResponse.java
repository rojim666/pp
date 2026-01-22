package com.charging.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String phone;
    private String email;
    private String realName;
    private String avatar;
    private String role;
    private BigDecimal balance;
    private Integer totalChargingCount;
    private BigDecimal totalChargingEnergy;
    private BigDecimal totalChargingFee;
    private String vehiclePlate;
    private String vehicleModel;
    private String vehicleBrand;
    private Boolean isVip;
    private LocalDate vipExpireDate;
    private String status;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createdAt;
}

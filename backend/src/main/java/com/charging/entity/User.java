package com.charging.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String phone;

    private String email;

    private String realName;

    private String avatar;

    private String idCard;

    private String role;

    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Builder.Default
    private Integer totalChargingCount = 0;

    @Builder.Default
    private BigDecimal totalChargingEnergy = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal totalChargingFee = BigDecimal.ZERO;

    private String vehiclePlate;

    private String vehicleModel;

    private String vehicleBrand;

    @Builder.Default
    private Boolean isVip = false;

    private LocalDate vipExpireDate;

    @Builder.Default
    private String status = "active";

    private LocalDateTime lastLoginTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    @Builder.Default
    private Boolean deleted = false;
}

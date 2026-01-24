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
 * 费率配置实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tariff_config")
public class TariffConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private BigDecimal peakPrice;

    private BigDecimal valleyPrice;

    private BigDecimal normalPrice;

    private BigDecimal serviceFee;

    private BigDecimal parkingFee;

    private Integer maxChargingHours;

    private Integer freeParkingMinutes;

    private BigDecimal minChargeFee;

    private BigDecimal maxPower;

    private String peakHours;

    private String valleyHours;

    @Builder.Default
    private Boolean isDefault = false;

    private LocalDate effectiveDate;

    private LocalDate expiryDate;

    private String description;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    @Builder.Default
    private Boolean deleted = false;
}

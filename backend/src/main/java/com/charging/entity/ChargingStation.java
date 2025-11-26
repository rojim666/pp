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
 * 充电桩实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("charging_station")
public class ChargingStation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String code;

    private String type;

    @Builder.Default
    private String status = "online";

    private BigDecimal power;

    private String location;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private BigDecimal voltage;

    private BigDecimal current;

    private BigDecimal temperature;

    @Builder.Default
    private Integer usageCount = 0;

    @Builder.Default
    private BigDecimal totalEnergy = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    private LocalDate lastMaintenanceDate;

    private LocalDate installDate;

    private String manufacturer;

    private String model;

    private String serialNumber;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    @Builder.Default
    private Boolean deleted = false;
}

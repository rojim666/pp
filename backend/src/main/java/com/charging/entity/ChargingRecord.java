package com.charging.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充电记录实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("charging_record")
public class ChargingRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long stationId;

    private String stationName;

    private Long userId;

    private String userName;

    private String userPhone;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer duration;

    private BigDecimal energy;

    private Integer startSoc;

    private Integer endSoc;

    @Builder.Default
    private BigDecimal peakEnergy = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal valleyEnergy = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal normalEnergy = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal electricityFee = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal serviceFee = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal totalFee = BigDecimal.ZERO;

    @Builder.Default
    private String status = "charging";

    @Builder.Default
    private String paymentStatus = "unpaid";

    private String paymentMethod;

    private LocalDateTime paymentTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    @Builder.Default
    private Boolean deleted = false;
}

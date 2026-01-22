package com.charging.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 充电桩响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationResponse {

    private Long id;
    private String name;
    private String code;
    private String type;
    private String status;
    private BigDecimal power;
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal voltage;
    private BigDecimal current;
    private BigDecimal temperature;
    private Integer usageCount;
    private BigDecimal totalEnergy;
    private BigDecimal totalRevenue;
    private LocalDate lastMaintenanceDate;
    private LocalDate installDate;
    private String manufacturer;
    private String model;
    private String serialNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * 当前正在进行的充电记录ID（仅当状态为charging时有值）
     */
    private Long activeRecordId;
    
    /**
     * 当前正在充电的用户ID
     */
    private Long activeUserId;
}

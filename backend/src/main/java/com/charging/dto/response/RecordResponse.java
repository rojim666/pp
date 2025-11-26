package com.charging.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充电记录响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordResponse {

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
    private BigDecimal peakEnergy;
    private BigDecimal valleyEnergy;
    private BigDecimal normalEnergy;
    private BigDecimal electricityFee;
    private BigDecimal serviceFee;
    private BigDecimal totalFee;
    private String status;
    private String paymentStatus;
    private String paymentMethod;
    private LocalDateTime paymentTime;
    private LocalDateTime createdAt;
}

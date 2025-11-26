package com.charging.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 仪表盘统计响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private Long totalStations;
    private Long onlineStations;
    private Long chargingStations;
    private Long offlineStations;
    
    private Long todayRecords;
    private BigDecimal todayEnergy;
    private BigDecimal todayRevenue;
    
    private Long totalUsers;
    private Long activeUsers;
    private Long vipUsers;
    
    private BigDecimal totalRevenue;
    private BigDecimal totalEnergy;
    private Long totalRecords;
}

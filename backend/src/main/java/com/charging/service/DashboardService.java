package com.charging.service;

import com.charging.dto.response.DashboardResponse;
import com.charging.mapper.ChargingRecordMapper;
import com.charging.mapper.ChargingStationMapper;
import com.charging.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 仪表盘服务
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ChargingStationMapper stationMapper;
    private final ChargingRecordMapper recordMapper;
    private final UserMapper userMapper;

    /**
     * 获取仪表盘统计数据
     */
    public DashboardResponse getDashboardStats() {
        // TODO: 实现统计查询(需要自定义SQL)
        return DashboardResponse.builder()
                .totalStations(0L)
                .onlineStations(0L)
                .chargingStations(0L)
                .offlineStations(0L)
                .todayRecords(0L)
                .totalUsers(0L)
                .activeUsers(0L)
                .vipUsers(0L)
                .build();
    }
}

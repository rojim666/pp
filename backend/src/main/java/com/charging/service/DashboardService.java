package com.charging.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.charging.dto.response.DashboardResponse;
import com.charging.entity.ChargingRecord;
import com.charging.entity.ChargingStation;
import com.charging.entity.User;
import com.charging.mapper.ChargingRecordMapper;
import com.charging.mapper.ChargingStationMapper;
import com.charging.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

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
        // 1. 充电桩统计
        Long totalStations = stationMapper.selectCount(null);
        Long onlineStations = stationMapper.selectCount(new LambdaQueryWrapper<ChargingStation>()
                .eq(ChargingStation::getStatus, "idle")
                .or().eq(ChargingStation::getStatus, "charging"));
        Long chargingStations = stationMapper.selectCount(new LambdaQueryWrapper<ChargingStation>()
                .eq(ChargingStation::getStatus, "charging"));
        Long offlineStations = stationMapper.selectCount(new LambdaQueryWrapper<ChargingStation>()
                .eq(ChargingStation::getStatus, "offline"));

        // 2. 用户统计
        Long totalUsers = userMapper.selectCount(null);
        Long activeUsers = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getStatus, "active"));
        Long vipUsers = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getIsVip, true));

        // 3. 今日数据
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        
        Long todayRecords = recordMapper.selectCount(new LambdaQueryWrapper<ChargingRecord>()
                .ge(ChargingRecord::getCreatedAt, startOfDay)
                .lt(ChargingRecord::getCreatedAt, endOfDay));

        // 计算今日收入和电量 (这里简单遍历，生产环境建议用SQL聚合)
        // 为了演示，我们先获取今日所有记录
        var todayRecordList = recordMapper.selectList(new LambdaQueryWrapper<ChargingRecord>()
                .ge(ChargingRecord::getCreatedAt, startOfDay)
                .lt(ChargingRecord::getCreatedAt, endOfDay));
        
        BigDecimal todayRevenue = todayRecordList.stream()
                .map(ChargingRecord::getTotalFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        BigDecimal todayEnergy = todayRecordList.stream()
                .map(ChargingRecord::getEnergy)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 计算平均充电时长 (分钟)
        double averageChargingTime = 0.0;
        if (!todayRecordList.isEmpty()) {
             averageChargingTime = todayRecordList.stream()
                    .mapToLong(r -> {
                        if (r.getStartTime() != null && r.getEndTime() != null) {
                            return java.time.Duration.between(r.getStartTime(), r.getEndTime()).toMinutes();
                        }
                        return 0;
                    })
                    .average()
                    .orElse(0.0);
        }

        return DashboardResponse.builder()
                .totalStations(totalStations)
                .onlineStations(onlineStations)
                .chargingStations(chargingStations)
                .offlineStations(offlineStations)
                .todayRecords(todayRecords)
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .vipUsers(vipUsers)
                .todayRevenue(todayRevenue)
                .todayEnergy(todayEnergy)
                // .averageChargingTime(averageChargingTime)
                .build();
    }
}

package com.charging.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charging.common.exception.BusinessException;
import com.charging.dto.request.RecordRequest;
import com.charging.dto.response.RecordResponse;
import com.charging.entity.ChargingRecord;
import com.charging.entity.ChargingStation;
import com.charging.entity.TariffConfig;
import com.charging.entity.User;
import com.charging.mapper.ChargingRecordMapper;
import com.charging.mapper.ChargingStationMapper;
import com.charging.mapper.TariffConfigMapper;
import com.charging.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 充电记录服务
 */
@Service
@RequiredArgsConstructor
public class ChargingRecordService {

    private final ChargingRecordMapper recordMapper;
    private final ChargingStationMapper stationMapper;
    private final UserMapper userMapper;
    private final TariffConfigMapper tariffMapper;

    /**
     * 分页查询充电记录
     */
    public IPage<RecordResponse> listRecords(int page, int size, String keyword, String status, Long userId) {
        Page<ChargingRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ChargingRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(ChargingRecord::getStationName, keyword)
                    .or().like(ChargingRecord::getUserName, keyword)
                    .or().like(ChargingRecord::getUserPhone, keyword));
        }
        
        if (status != null && !status.isEmpty()) {
            wrapper.eq(ChargingRecord::getStatus, status);
        }
        
        if (userId != null) {
            wrapper.eq(ChargingRecord::getUserId, userId);
        }
        
        wrapper.orderByDesc(ChargingRecord::getCreatedAt);
        
        IPage<ChargingRecord> recordPage = recordMapper.selectPage(pageParam, wrapper);
        return recordPage.convert(this::convertToResponse);
    }

    /**
     * 根据ID获取充电记录
     */
    public RecordResponse getRecordById(Long id) {
        ChargingRecord record = recordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("充电记录不存在");
        }
        return convertToResponse(record);
    }

    /**
     * 开始充电
     */
    @Transactional
    public RecordResponse startCharging(RecordRequest request) {
        // 验证充电桩
        ChargingStation station = stationMapper.selectById(request.getStationId());
        if (station == null) {
            throw new BusinessException("充电桩不存在");
        }
        if (!"online".equals(station.getStatus())) {
            throw new BusinessException("充电桩不可用");
        }
        
        // 验证用户
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 创建充电记录
        ChargingRecord record = ChargingRecord.builder()
                .stationId(station.getId())
                .stationName(station.getName())
                .userId(user.getId())
                .userName(user.getUsername())
                .userPhone(user.getPhone())
                .startTime(request.getStartTime())
                .startSoc(request.getStartSoc())
                .status("charging")
                .paymentStatus("unpaid")
                .build();
        
        recordMapper.insert(record);
        
        // 更新充电桩状态
        station.setStatus("charging");
        stationMapper.updateById(station);
        
        return convertToResponse(record);
    }

    /**
     * 结束充电
     */
    @Transactional
    public RecordResponse endCharging(Long id, LocalDateTime endTime, Integer endSoc) {
        ChargingRecord record = recordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("充电记录不存在");
        }
        
        if (!"charging".equals(record.getStatus())) {
            throw new BusinessException("充电已结束");
        }
        
        // 获取默认费率
        TariffConfig tariff = tariffMapper.selectOne(new LambdaQueryWrapper<TariffConfig>()
                .eq(TariffConfig::getIsDefault, true));
        if (tariff == null) {
            throw new BusinessException("未配置费率");
        }
        
        // 计算充电时长
        Duration duration = Duration.between(record.getStartTime(), endTime);
        int minutes = (int) duration.toMinutes();
        
        // 计算充电量(简化计算：按SOC差值和平均功率估算)
        ChargingStation station = stationMapper.selectById(record.getStationId());
        int socDiff = endSoc - record.getStartSoc();
        BigDecimal energy = BigDecimal.valueOf(socDiff * 0.6); // 假设60度电池容量
        
        // 简化费用计算(实际应按时段分峰谷平)
        BigDecimal electricityFee = energy.multiply(tariff.getNormalPrice());
        BigDecimal serviceFee = energy.multiply(tariff.getServiceFee());
        BigDecimal totalFee = electricityFee.add(serviceFee);
        
        // 更新记录
        record.setEndTime(endTime);
        record.setEndSoc(endSoc);
        record.setDuration(minutes);
        record.setEnergy(energy);
        record.setNormalEnergy(energy);
        record.setElectricityFee(electricityFee);
        record.setServiceFee(serviceFee);
        record.setTotalFee(totalFee);
        record.setStatus("completed");
        
        recordMapper.updateById(record);
        
        // 更新充电桩统计
        station.setUsageCount(station.getUsageCount() + 1);
        station.setTotalEnergy(station.getTotalEnergy().add(energy));
        station.setTotalRevenue(station.getTotalRevenue().add(totalFee));
        station.setStatus("online");
        stationMapper.updateById(station);
        
        // 更新用户统计
        User user = userMapper.selectById(record.getUserId());
        user.setTotalChargingCount(user.getTotalChargingCount() + 1);
        user.setTotalChargingEnergy(user.getTotalChargingEnergy().add(energy));
        user.setTotalChargingFee(user.getTotalChargingFee().add(totalFee));
        userMapper.updateById(user);
        
        return convertToResponse(record);
    }

    /**
     * 转换为响应DTO
     */
    private RecordResponse convertToResponse(ChargingRecord record) {
        RecordResponse response = new RecordResponse();
        BeanUtils.copyProperties(record, response);
        return response;
    }
}

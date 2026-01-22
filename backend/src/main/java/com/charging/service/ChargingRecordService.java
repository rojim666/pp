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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import java.math.RoundingMode;

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
        
        // 检查余额
        if (user.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("余额不足，请先充值");
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
        long minutes = duration.toMinutes();
        if (minutes < 1) minutes = 1; // 不足1分钟按1分钟计算
        
        // 获取充电桩功率
        ChargingStation station = stationMapper.selectById(record.getStationId());
        BigDecimal power = station.getPower();
        if (power == null || power.compareTo(BigDecimal.ZERO) <= 0) {
            power = new BigDecimal("7.0"); // 默认7kW
        }

        // 计算充电量 = 功率(kW) * 时间(h)
        // 时间转换为小时，保留4位小数
        BigDecimal hours = new BigDecimal(minutes).divide(new BigDecimal("60"), 4, RoundingMode.HALF_UP);
        BigDecimal energy = power.multiply(hours).setScale(2, RoundingMode.HALF_UP);
        
        // 根据充电量反推结束SOC (假设电池容量60kWh)
        // SOC增加量 = (充电量 / 60) * 100
        int socIncrease = energy.divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100")).intValue();
        int calculatedEndSoc = record.getStartSoc() + socIncrease;
        if (calculatedEndSoc > 100) calculatedEndSoc = 100;
        
        // 使用计算出的SOC，或者如果前端传来的SOC更合理（比如模拟器），也可以考虑保留逻辑
        // 这里为了解决"费用与时间不匹配"的问题，我们优先使用基于时间的计算
        endSoc = calculatedEndSoc;
        
        // 费用计算
        BigDecimal electricityFee = energy.multiply(tariff.getNormalPrice());
        BigDecimal serviceFee = energy.multiply(tariff.getServiceFee());
        BigDecimal totalFee = electricityFee.add(serviceFee);
        
        // 更新记录
        record.setEndTime(endTime);
        record.setEndSoc(endSoc);
        record.setDuration((int) minutes);
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
        
        // 扣除余额
        if (user.getBalance().compareTo(totalFee) < 0) {
            // 如果余额不足，扣除剩余所有余额（实际场景可能允许透支或强制停止逻辑已处理）
            // 这里简单处理为扣除费用，允许变成负数，或者在强制停止逻辑中保证不会超额太多
            user.setBalance(user.getBalance().subtract(totalFee));
        } else {
            user.setBalance(user.getBalance().subtract(totalFee));
        }
        
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

    /**
     * 定时检查正在充电的用户余额，如果余额不足则强制停止
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60000)
    public void checkBalanceAndStopCharging() {
        // 查询所有正在充电的记录
        List<ChargingRecord> chargingRecords = recordMapper.selectList(new LambdaQueryWrapper<ChargingRecord>()
                .eq(ChargingRecord::getStatus, "charging"));

        for (ChargingRecord record : chargingRecords) {
            try {
                checkSingleRecord(record);
            } catch (Exception e) {
                // 记录日志，继续处理下一条
                e.printStackTrace();
            }
        }
    }

    private void checkSingleRecord(ChargingRecord record) {
        User user = userMapper.selectById(record.getUserId());
        if (user == null) return;

        // 计算当前费用
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(record.getStartTime(), now);
        long minutes = duration.toMinutes();
        if (minutes < 1) minutes = 1;

        ChargingStation station = stationMapper.selectById(record.getStationId());
        BigDecimal power = station.getPower();
        if (power == null || power.compareTo(BigDecimal.ZERO) <= 0) {
            power = new BigDecimal("7.0");
        }
        
        BigDecimal hours = new BigDecimal(minutes).divide(new BigDecimal("60"), 4, RoundingMode.HALF_UP);
        BigDecimal energy = power.multiply(hours).setScale(2, RoundingMode.HALF_UP);

        TariffConfig tariff = tariffMapper.selectOne(new LambdaQueryWrapper<TariffConfig>()
                .eq(TariffConfig::getIsDefault, true));
        if (tariff == null) return;

        BigDecimal price = tariff.getNormalPrice();
        BigDecimal servicePrice = tariff.getServiceFee();
        BigDecimal totalFee = energy.multiply(price.add(servicePrice)).setScale(2, RoundingMode.HALF_UP);

        // 如果费用超过余额，强制停止
        if (totalFee.compareTo(user.getBalance()) >= 0) {
            // 估算SOC: 假设电池60kWh
            BigDecimal estimatedAddedSoc = energy.divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
            BigDecimal startSoc = new BigDecimal(record.getStartSoc());
            BigDecimal endSoc = startSoc.add(estimatedAddedSoc);
            if (endSoc.compareTo(new BigDecimal("100")) > 0) endSoc = new BigDecimal("100");
            
            endCharging(record.getId(), now, endSoc.intValue());
            System.out.println("User " + user.getId() + " balance insufficient. Forced stop charging record " + record.getId());
        }
    }
}

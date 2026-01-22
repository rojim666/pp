package com.charging.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charging.common.exception.BusinessException;
import com.charging.dto.request.StationRequest;
import com.charging.dto.response.StationResponse;
import com.charging.entity.ChargingRecord;
import com.charging.entity.ChargingStation;
import com.charging.mapper.ChargingRecordMapper;
import com.charging.mapper.ChargingStationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 充电桩服务
 */
@Service
@RequiredArgsConstructor
public class ChargingStationService {

    private final ChargingStationMapper stationMapper;
    private final ChargingRecordMapper recordMapper;

    /**
     * 分页查询充电桩
     */
    public IPage<StationResponse> listStations(int page, int size, String keyword, String type, String status) {
        Page<ChargingStation> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ChargingStation> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(ChargingStation::getName, keyword)
                    .or().like(ChargingStation::getCode, keyword)
                    .or().like(ChargingStation::getLocation, keyword));
        }
        
        if (type != null && !type.isEmpty()) {
            wrapper.eq(ChargingStation::getType, type);
        }
        
        if (status != null && !status.isEmpty()) {
            wrapper.eq(ChargingStation::getStatus, status);
        }
        
        wrapper.orderByDesc(ChargingStation::getCreatedAt);
        
        IPage<ChargingStation> stationPage = stationMapper.selectPage(pageParam, wrapper);
        System.out.println("Found " + stationPage.getTotal() + " stations in DB");
        return stationPage.convert(this::convertToResponse);
    }

    /**
     * 根据ID获取充电桩
     */
    public StationResponse getStationById(Long id) {
        ChargingStation station = stationMapper.selectById(id);
        if (station == null) {
            throw new BusinessException("充电桩不存在");
        }
        return convertToResponse(station);
    }

    /**
     * 创建充电桩
     */
    @Transactional
    public StationResponse createStation(StationRequest request) {
        // 检查编号是否存在
        if (stationMapper.selectCount(new LambdaQueryWrapper<ChargingStation>()
                .eq(ChargingStation::getCode, request.getCode())) > 0) {
            throw new BusinessException("充电桩编号已存在");
        }
        
        ChargingStation station = new ChargingStation();
        BeanUtils.copyProperties(request, station);
        station.setStatus("online");
        
        stationMapper.insert(station);
        return convertToResponse(station);
    }

    /**
     * 更新充电桩
     */
    @Transactional
    public StationResponse updateStation(Long id, StationRequest request) {
        ChargingStation station = stationMapper.selectById(id);
        if (station == null) {
            throw new BusinessException("充电桩不存在");
        }
        
        // 检查编号是否被其他充电桩使用
        if (request.getCode() != null && !request.getCode().equals(station.getCode())) {
            if (stationMapper.selectCount(new LambdaQueryWrapper<ChargingStation>()
                    .eq(ChargingStation::getCode, request.getCode())
                    .ne(ChargingStation::getId, id)) > 0) {
                throw new BusinessException("充电桩编号已被使用");
            }
        }
        
        BeanUtils.copyProperties(request, station);
        stationMapper.updateById(station);
        return convertToResponse(station);
    }

    /**
     * 删除充电桩
     */
    @Transactional
    public void deleteStation(Long id) {
        ChargingStation station = stationMapper.selectById(id);
        if (station == null) {
            throw new BusinessException("充电桩不存在");
        }
        stationMapper.deleteById(id);
    }

    /**
     * 更新充电桩状态
     */
    @Transactional
    public void updateStationStatus(Long id, String status) {
        ChargingStation station = stationMapper.selectById(id);
        if (station == null) {
            throw new BusinessException("充电桩不存在");
        }
        station.setStatus(status);
        stationMapper.updateById(station);
    }

    /**
     * 转换为响应DTO
     */
    private StationResponse convertToResponse(ChargingStation station) {
        StationResponse response = new StationResponse();
        BeanUtils.copyProperties(station, response);
        
        // 如果是充电中状态，查询当前正在进行的订单ID
        if ("charging".equals(station.getStatus())) {
            ChargingRecord activeRecord = recordMapper.selectOne(new LambdaQueryWrapper<ChargingRecord>()
                    .eq(ChargingRecord::getStationId, station.getId())
                    .eq(ChargingRecord::getStatus, "charging")
                    .orderByDesc(ChargingRecord::getId)
                    .last("LIMIT 1"));
            
            if (activeRecord != null) {
                response.setActiveRecordId(activeRecord.getId());
                response.setActiveUserId(activeRecord.getUserId());
            }
        }
        
        return response;
    }
}

package com.charging.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charging.entity.ChargingStation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 充电桩Mapper
 */
@Mapper
public interface ChargingStationMapper extends BaseMapper<ChargingStation> {
}

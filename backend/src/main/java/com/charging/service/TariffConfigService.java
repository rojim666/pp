package com.charging.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.charging.common.exception.BusinessException;
import com.charging.entity.TariffConfig;
import com.charging.mapper.TariffConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 费率配置服务
 */
@Service
@RequiredArgsConstructor
public class TariffConfigService {

    private final TariffConfigMapper tariffConfigMapper;

    /**
     * 获取当前生效的费率配置
     * 如果没有配置，则创建一个默认配置
     */
    public TariffConfig getCurrentConfig() {
        // 获取最新的一个配置
        TariffConfig config = tariffConfigMapper.selectOne(
                new LambdaQueryWrapper<TariffConfig>()
                        .orderByDesc(TariffConfig::getId)
                        .last("LIMIT 1")
        );

        if (config == null) {
            // 创建默认配置
            config = TariffConfig.builder()
                    .peakPrice(new BigDecimal("1.20"))
                    .valleyPrice(new BigDecimal("0.60"))
                    .serviceFee(new BigDecimal("0.80"))
                    .peakHours("8:00-22:00")
                    .valleyHours("22:00-次日8:00")
                    .isDefault(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            tariffConfigMapper.insert(config);
        }

        return config;
    }

    /**
     * 更新费率配置
     */
    @Transactional(rollbackFor = Exception.class)
    public TariffConfig updateConfig(TariffConfig config) {
        // 这里的逻辑可以是更新现有记录，或者插入新记录作为历史版本
        // 简单起见，我们直接更新ID为1的记录，或者如果传入了ID就更新该ID
        
        if (config.getId() == null) {
            // 如果没有ID，尝试获取当前配置的ID
            TariffConfig current = getCurrentConfig();
            config.setId(current.getId());
        }
        
        config.setUpdatedAt(LocalDateTime.now());
        int rows = tariffConfigMapper.updateById(config);
        if (rows == 0) {
            throw new BusinessException("更新费率配置失败");
        }
        
        return tariffConfigMapper.selectById(config.getId());
    }
}

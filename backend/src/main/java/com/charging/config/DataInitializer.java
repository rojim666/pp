package com.charging.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.charging.entity.TariffConfig;
import com.charging.entity.User;
import com.charging.mapper.TariffConfigMapper;
import com.charging.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 数据初始化
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final TariffConfigMapper tariffMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initUsers();
        initTariff();
    }

    private void initUsers() {
        if (userMapper.selectCount(null) == 0) {
            log.info("初始化用户数据...");
            
            // 管理员
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("13800138000")
                    .role("admin")
                    .realName("系统管理员")
                    .status("active")
                    .balance(new BigDecimal("8888.00"))
                    .build();
            userMapper.insert(admin);

            // 普通用户
            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .phone("13900139000")
                    .role("user")
                    .realName("测试用户")
                    .status("active")
                    .balance(new BigDecimal("1000.00"))
                    .build();
            userMapper.insert(user);
            
            log.info("用户数据初始化完成");
        }
    }

    private void initTariff() {
        if (tariffMapper.selectCount(null) == 0) {
            log.info("初始化费率数据...");
            
            TariffConfig config = TariffConfig.builder()
                    .name("默认费率")
                    .peakPrice(new BigDecimal("1.50"))
                    .normalPrice(new BigDecimal("1.00"))
                    .valleyPrice(new BigDecimal("0.50"))
                    .serviceFee(new BigDecimal("0.80"))
                    // .parkingFee(new BigDecimal("5.00"))
                    .isDefault(true)
                    .effectiveDate(LocalDate.now())
                    .description("系统默认费率配置")
                    .build();
            
            tariffMapper.insert(config);
            
            log.info("费率数据初始化完成");
        }
    }
}

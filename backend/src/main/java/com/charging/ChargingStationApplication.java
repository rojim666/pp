package com.charging;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 充电桩管理系统启动类
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.charging.mapper")
public class ChargingStationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChargingStationApplication.class, args);
        System.out.println("\n" +
                "   ______                      _                ____  __        __  _           \n" +
                "  / ____/___ ___  _____ ___  (_)___  ____ _   / __ \\/ /_  __  / /_(_)___  ____ \n" +
                " / /   / __ `__ \\/ __ `/ _ \\/ / __ \\/ __ `/  / /_/ / / / / / / __/ / __ \\/ __ \\\n" +
                "/ /___/ / / / / / /_/ /  __/ / / / / /_/ /  / _, _/ / /_/ / / /_/ / /_/ / / / /\n" +
                "\\____/_/ /_/ /_/\\__,_/\\___/_/_/ /_/\\__, /  /_/ |_/_/\\__,_/  \\__/_/\\____/_/ /_/ \n" +
                "                                   /____/                                       \n" +
                "\n充电桩管理系统启动成功！\n" +
                "访问地址: http://localhost:8080/api\n" +
                "API文档: http://localhost:8080/api/swagger-ui.html\n");
    }
}

package com.charging.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 充电桩请求DTO
 */
@Data
public class StationRequest {

    @NotBlank(message = "充电桩名称不能为空")
    @Size(max = 100, message = "充电桩名称不能超过100字符")
    private String name;

    @NotBlank(message = "充电桩编号不能为空")
    @Size(max = 50, message = "充电桩编号不能超过50字符")
    private String code;

    @NotBlank(message = "充电桩类型不能为空")
    @Pattern(regexp = "ac|dc|super", message = "充电桩类型只能是ac/dc/super")
    private String type;

    @NotNull(message = "功率不能为空")
    @DecimalMin(value = "0.01", message = "功率必须大于0")
    private BigDecimal power;

    @NotBlank(message = "位置不能为空")
    private String location;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @NotNull(message = "电压不能为空")
    private BigDecimal voltage;

    @NotNull(message = "电流不能为空")
    private BigDecimal current;

    private BigDecimal temperature;

    private LocalDate lastMaintenanceDate;

    @NotNull(message = "安装日期不能为空")
    private LocalDate installDate;

    private String manufacturer;

    private String model;

    private String serialNumber;
}

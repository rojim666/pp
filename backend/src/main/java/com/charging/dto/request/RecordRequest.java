package com.charging.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 充电记录请求DTO
 */
@Data
public class RecordRequest {

    @NotNull(message = "充电桩ID不能为空")
    private Long stationId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Min(value = 0, message = "开始电量不能小于0")
    @Max(value = 100, message = "开始电量不能大于100")
    private Integer startSoc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Min(value = 0, message = "结束电量不能小于0")
    @Max(value = 100, message = "结束电量不能大于100")
    private Integer endSoc;
}

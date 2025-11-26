package com.charging.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 用户注册/更新请求DTO
 */
@Data
public class UserRequest {

    @NotBlank(message = "用户名不能为空", groups = Create.class)
    @Size(min = 3, max = 50, message = "用户名长度为3-50字符")
    private String username;

    @NotBlank(message = "密码不能为空", groups = Create.class)
    @Size(min = 6, max = 20, message = "密码长度为6-20字符")
    private String password;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String realName;

    private String avatar;

    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$", message = "身份证号格式不正确")
    private String idCard;

    private String vehiclePlate;

    private String vehicleModel;

    private String vehicleBrand;

    public interface Create {}
    public interface Update {}
}

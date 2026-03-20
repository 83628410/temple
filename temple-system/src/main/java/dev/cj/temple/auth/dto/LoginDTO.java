package dev.cj.temple.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "登录请求参数")
public class LoginDTO {
    @Schema(description = "用户名",requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    @NotBlank
    private String username;
    @Schema(description = "密码",requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotBlank
    private String password;
}
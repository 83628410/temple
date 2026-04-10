package dev.cj.temple.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@Schema(description = "用户信息 VO")
public class UserVO {
    @Schema(description = "用户 ID", example = "1")
    private Long id;
    
    @Schema(description = "用户名", example = "admin")
    private String username;
    
    @Schema(description = "昵称", example = "管理员")
    private String nickname;
    
    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;
    
    @Schema(description = "手机号", example = "13800138000")
    private String phone;
    
    @Schema(description = "状态", example = "1")
    private Integer status;
    
    @Schema(description = "创建时间", example = "2023-01-01T12:00:00")
    private Date createTime;
    
    @Schema(description = "更新时间", example = "2023-01-01T12:30:00")
    private Date updateTime;
    
    @Schema(description = "角色 ID 列表", example = "[1, 2]")
    private Long[] roleIds;
}

package dev.cj.temple.vo;

import dev.cj.temple.domain.Menu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@Schema(description = "角色信息 VO")
public class RoleVO {
    @Schema(description = "角色 ID",example = "1")
    private Long id;
    
    @Schema(description = "角色名称",example = "管理员")
    private String name;
    
    @Schema(description = "角色描述",example = "系统管理员")
    private String description;
    
    @Schema(description = "状态",example = "1")
    private Integer status;
    
    @Schema(description = "创建时间",example = "2023-01-01T12:00:00")
    private Date createTime;
    
    @Schema(description = "更新时间",example = "2023-01-01T12:30:00")
    private Date updateTime;
    
    @Schema(description = "菜单列表")
    private List<Menu> menus;
}

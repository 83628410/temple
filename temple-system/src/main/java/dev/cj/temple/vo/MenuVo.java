package dev.cj.temple.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "菜单VO")
public class MenuVo {
    @Schema(description = "菜单ID")
    private Long id;
    @Schema(description = "菜单名称")
    private String name;
    @Schema(description = "菜单路径")
    private String path;
    @Schema(description = "菜单组件")
    private String component;
    @Schema(description = "菜单图标")
    private String icon;
    @Schema(description = "菜单排序")
    private Integer orderNum;
    @Schema(description = "菜单类型")
    private String type;
    @Schema(description = "菜单父ID")
    private Long parentId;
    @Schema(description = "菜单权限")
    private String permission;
    @Schema(description = "菜单状态")
    private Integer status;
    @Schema(description = "菜单子菜单")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MenuVo> children;
}

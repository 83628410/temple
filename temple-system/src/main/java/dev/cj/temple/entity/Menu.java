package dev.cj.temple.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Entity
@Table(name = "sys_menu")
@Schema(description = "菜单实体")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "菜单唯一ID", example = "1")
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    @Schema(description = "菜单名称", example = "系统设置", required = true)
    private String title;

    @Column(name = "type", nullable = false, length = 50)
    @Schema(description = "菜单类型(C-目录 M-菜单 B-按钮)", example = "M", required = true)
    private String type;

    @Column(name = "name", nullable = false, length = 50)
    @Schema(description = "路由的name", example = "Dashboard", required = true)
    private String name;

    @Column(name = "path", length = 100)
    @Schema(description = "路由路径", example = "/dashboard")
    private String path;

    @Column(name = "component", length = 100)
    @Schema(description = "前端组件路径", example = "views/dashboard/index")
    private String component;

    @Column(name = "icon", length = 50)
    @Schema(description = "菜单图标 (例如 element-ui 图标)", example = "el-icon-s-home")
    private String icon;

    @Column(name = "parent_id")
    @Schema(description = "父菜单ID (0 表示一级菜单)", example = "0")
    private Long parentId;

    @Column(name = "order_num")
    @Schema(description = "显示顺序 (数值越小越靠前)", example = "1")
    private Integer orderNum;

    @Column(name = "status")
    @Schema(description = "菜单状态 (0: 禁用, 1: 启用)", example = "1")
    private Integer status;

    @Column(name = "permission", length = 50)
    @Schema(description = "权限标识", example = "system:menu:view")
    private String permission;

    @Column(name = "create_time")
    @Schema(description = "创建时间", example = "2023-01-01T12:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private Date createTime;

    @Column(name = "update_time")
    @Schema(description = "最后更新时间", example = "2023-01-01T12:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private Date updateTime;

}

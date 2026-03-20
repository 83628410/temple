package dev.cj.temple.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Schema(description = "路由对象")
@Data
public class RouterVo {
    @Schema(description = "路由的name", example = "Dashboard", required = true)
    private String name;
    @Schema(description = "路由路径", example = "/dashboard")
    private String path;
    @Schema(description = "前端组件路径", example = "views/dashboard/index")
    private String component;
    @Schema(description = "路由元数据", example = "false")
    private Meta meta;
    @Schema(description = "子路由列表")
    private List<RouterVo> children;
    @Schema(description = "路由元数据类")
    @Data
    public static class Meta {
        @Schema(description = "路由标题", example = "系统设置", required = true)
        private String title;
        @Schema(description = "路由图标", example = "i-ep-BottomLeft")
        private String icon;
    }
}

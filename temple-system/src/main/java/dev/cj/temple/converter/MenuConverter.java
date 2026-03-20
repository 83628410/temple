package dev.cj.temple.converter;

import dev.cj.temple.entity.Menu;
import dev.cj.temple.vo.MenuVo;
import dev.cj.temple.vo.RouterVo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MenuConverter {


    @Mapping(source = "name", target = "name")
    @Mapping(source = "path", target = "path")
    @Mapping(source = "component", target = "component")
    @Mapping(source = "title", target = "meta.title")
    @Mapping(source = "icon", target = "meta.icon")
    @Mapping(target = "children", ignore = true)
    RouterVo toRouterVo(Menu menu);
    // 忽略children字段
    @Mapping(target = "children", ignore = true)
    MenuVo toMenuVo(Menu menu);
}

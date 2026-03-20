package dev.cj.temple.controller;

import dev.cj.temple.common.utils.Result;
import dev.cj.temple.dto.MenuDTO;
import dev.cj.temple.entity.Menu;
import dev.cj.temple.service.MenuService;
import dev.cj.temple.vo.MenuVo;
import dev.cj.temple.vo.RouterVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@Tag(name = "菜单管理", description = "菜单信息管理相关接口")
public class MenuController {
    
    private final MenuService menuService;
    @GetMapping("/routes")
    @Operation(summary = "获取当前用户路由列表", description = "获取系统中所有路由信息")
    public Result<List<RouterVo>> routes() {
        return Result.success(menuService.findCurrentRoutes());
    }
    @GetMapping("/list")
    @Operation(summary = "获取菜单列表", description = "获取系统中所有菜单信息")
    public Result<List<MenuVo>> list() {
        return Result.success(menuService.findAllVo());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取菜单详情", description = "根据菜单ID获取菜单详细信息")
    @Parameter(description = "菜单ID", name = "id", required = true)
    public Result<Menu> getById(@PathVariable Long id) {
        return Result.success(menuService.findById(id));
    }
    
    @PostMapping("/save")
    @Operation(summary = "创建菜单", description = "创建新的菜单信息")
    @Parameter(description = "菜单信息", name = "menuDTO", required = true)
    public Result<Menu> save(@RequestBody MenuDTO menuDTO) {
        return Result.success(menuService.save(menuDTO));
    }
    
    @PutMapping("/update")
    @Operation(summary = "更新菜单", description = "更新菜单信息")
    @Parameter(description = "菜单信息", name = "menuDTO", required = true)
    public Result<Menu> update(@RequestBody MenuDTO menuDTO) {
        return Result.success(menuService.update(menuDTO));
    }
    
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除菜单", description = "根据菜单ID删除菜单")
    @Parameter(description = "菜单ID", name = "id", required = true)
    public Result<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return Result.success();
    }

    @PutMapping("/update/status")
    @Operation(summary = "更新菜单状态", description = "根据菜单ID更新状态")
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        menuService.updateStatus(id, status);
        return Result.success();
    }
}
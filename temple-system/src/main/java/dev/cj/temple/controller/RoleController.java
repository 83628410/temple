package dev.cj.temple.controller;

import dev.cj.temple.common.utils.Result;
import dev.cj.temple.dto.RoleDTO;
import dev.cj.temple.entity.Role;
import dev.cj.temple.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色信息管理相关接口")
public class RoleController {
    
    private final RoleService roleService;
    
    @GetMapping("/list")
    @Operation(summary = "获取角色列表", description = "获取系统中所有角色信息")
    public Result<List<Role>> list() {
        return Result.success(roleService.findAll());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取角色详情", description = "根据角色ID获取角色详细信息")
    @Parameter(description = "角色ID", name = "id", required = true)
    public Result<Role> getById(@PathVariable Long id) {
        return Result.success(roleService.findById(id));
    }
    
    @PostMapping("/save")
    @Operation(summary = "创建角色", description = "创建新的角色信息")
    @Parameter(description = "角色信息", name = "roleDTO", required = true)
    public Result<Role> save(@RequestBody RoleDTO roleDTO) {
        return Result.success(roleService.save(roleDTO));
    }
    
    @PutMapping("/update")
    @Operation(summary = "更新角色", description = "更新角色信息")
    @Parameter(description = "角色信息", name = "roleDTO", required = true)
    public Result<Role> update(@RequestBody RoleDTO roleDTO) {
        return Result.success(roleService.update(roleDTO));
    }
    
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除角色", description = "根据角色ID删除角色")
    @Parameter(description = "角色ID", name = "id", required = true)
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success();
    }
}
package dev.cj.temple.controller;

import dev.cj.temple.common.utils.Result;
import dev.cj.temple.dto.UserDTO;
import dev.cj.temple.entity.User;
import dev.cj.temple.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户信息管理相关接口")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/list")
    @Operation(summary = "获取用户列表", description = "获取系统中所有用户信息")
    public Result<List<User>> list() {
        return Result.success(userService.findAll());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
    @Parameter(description = "用户ID", name = "id", required = true)
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.findById(id));
    }
    
    @PostMapping("/save")
    @Operation(summary = "创建用户", description = "创建新的用户信息")
    @Parameter(description = "用户信息", name = "userDTO", required = true)
    public Result<User> save(@RequestBody UserDTO userDTO) {
        return Result.success(userService.save(userDTO));
    }
    
    @PutMapping("/update")
    @Operation(summary = "更新用户", description = "更新用户信息")
    @Parameter(description = "用户信息", name = "userDTO", required = true)
    public Result<User> update(@RequestBody UserDTO userDTO) {
        return Result.success(userService.update(userDTO));
    }
    
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @Parameter(description = "用户ID", name = "id", required = true)
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }
}
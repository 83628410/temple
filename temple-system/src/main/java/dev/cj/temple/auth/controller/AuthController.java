package dev.cj.temple.auth.controller;

import dev.cj.temple.auth.service.AuthService;
import dev.cj.temple.auth.model.vo.AuthTokenVo;
import dev.cj.temple.common.utils.Result;

import dev.cj.temple.auth.model.dto.LoginDTO;
import dev.cj.temple.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "通过用户名和密码登录系统")
    public Result<AuthTokenVo> login(@Parameter(description = "登录信息") @RequestBody LoginDTO loginDTO) {
        AuthTokenVo tokenVo = authService.login(loginDTO);
        return Result.success(tokenVo);
    }

    @DeleteMapping("/logout")
    @Operation(summary = "退出登录")
    public Result logout(HttpServletRequest request) {
        authService.logout();
        return Result.success();
    }

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public Result<User> getUserInfo() {
        // User user = userService.findByUsername(
        //         org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName()
        // );
        return Result.success();
    }

}
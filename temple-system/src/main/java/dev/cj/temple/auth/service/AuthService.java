package dev.cj.temple.auth.service;

import dev.cj.temple.auth.dto.LoginDTO;
import dev.cj.temple.auth.vo.AuthTokenVo;

/**
 * 登录认证
 */
public interface AuthService {
    /**
     * @param loginDTO
     * @return
     */
    AuthTokenVo login(LoginDTO loginDTO);

    /**
     * 登出
     */
    void logout();
}

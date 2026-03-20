package dev.cj.temple.auth.service.impl;

import dev.cj.temple.auth.dto.LoginDTO;
import dev.cj.temple.auth.service.AuthService;
import dev.cj.temple.auth.vo.AuthTokenVo;
import dev.cj.temple.entity.User;
import dev.cj.temple.security.JwtUtil;
import dev.cj.temple.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    /**
     * 登录
     * @param loginDTO 登录DTO
     * @return 认证令牌VO
     */
    @Override
    public AuthTokenVo login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        User user = userService.findByUsername(loginDTO.getUsername());
        String token = jwtUtil.generateToken(user.getUsername(), user.getId());

        return AuthTokenVo.builder().token(token).build();
    }

    @Override
    public void logout() {
        // TODO: 实现登出逻辑
    }
}

package dev.cj.temple.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";
    
    private final StringRedisTemplate stringRedisTemplate;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    public String generateToken(String username, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }
    
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    public Boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
    
    public Boolean validateToken(String token, String username) {
        String tokenUsername = getUsernameFromToken(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }
    
    /**
     * 获取JWT Token的剩余有效期
     * 
     * @param token JWT Token字符串
     * @return 剩余有效时间（秒），如果token已过期则返回0
     */
    public Long getRemainingExpiration(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        long remainingMs = expiration.getTime() - new Date().getTime();
        return remainingMs > 0 ? remainingMs / 1000 : 0;
    }

    /**
     * 从当前HTTP请求中获取JWT Token
     * 通过RequestContextHolder自动获取当前请求的Authorization头，并解析出Bearer Token
     * 
     * @return JWT Token字符串，如果不存在或格式不正确则返回null
     */
    public String getCurrentToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
            return null;
        }
        return null;
    }
    
    /**
     * 从Authorization header中解析出JWT Token
     * 去除"Bearer "前缀，返回纯token字符串
     * 
     * @param authHeader HTTP请求的Authorization header值，格式为"Bearer {token}"
     * @return 纯JWT Token字符串，如果header为空或格式不正确则返回null
     */
    public String resolveToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    
    /**
     * 将token加入黑名单
     * 用于用户登出时使token失效，防止token被重复使用
     * 
     * @param token JWT Token字符串
     */
    public void addToBlacklist(String token) {
        String key = TOKEN_BLACKLIST_PREFIX + token;
        Long remainingExpiration = getRemainingExpiration(token);
        if (remainingExpiration > 0) {
            stringRedisTemplate.opsForValue().set(key, "1", remainingExpiration, TimeUnit.SECONDS);
        }
    }
    
    /**
     * 检查token是否在黑名单中
     * 
     * @param token JWT Token字符串
     * @return 如果token在黑名单中返回true，否则返回false
     */
    public boolean isBlacklisted(String token) {
        String key = TOKEN_BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }
}
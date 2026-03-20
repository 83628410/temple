package dev.cj.temple.auth.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthTokenVo {
    String token;
}

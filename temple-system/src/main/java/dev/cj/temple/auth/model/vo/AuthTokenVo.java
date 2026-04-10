package dev.cj.temple.auth.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthTokenVo {
    String token;
}

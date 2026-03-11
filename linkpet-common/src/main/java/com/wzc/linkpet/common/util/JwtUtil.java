package com.wzc.linkpet.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类
 * 封装 JWT 令牌的生成与解析逻辑，使用 HMAC-SHA256 签名算法。
 *
 * <p>JWT 结构：Header.Payload.Signature
 * <ul>
 *   <li>Payload 中固定存入 subject（用户ID字符串），可附加自定义 claims</li>
 *   <li>secretKey 应配置为足够复杂的字符串（至少 32 字节），生产环境从配置文件读取</li>
 * </ul>
 * </p>
 */
@Slf4j
public class JwtUtil {

    private JwtUtil() {
        // 工具类，禁止实例化
    }

    /**
     * 生成 JWT 令牌
     *
     * @param secretKey 签名密钥字符串
     * @param ttlMillis 令牌有效时长（毫秒）
     * @param subject   主题（通常为用户 ID 字符串）
     * @param claims    额外携带的 Payload 键值对（可为空）
     * @return JWT 字符串
     */
    public static String createJwt(String secretKey, long ttlMillis, String subject, Map<String, Object> claims) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + ttlMillis);

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256);

        if (claims != null && !claims.isEmpty()) {
            builder.addClaims(claims);
        }
        return builder.compact();
    }

    /**
     * 解析 JWT 令牌，获取 Claims（Payload）
     *
     * @param secretKey 签名密钥字符串
     * @param token     JWT 字符串
     * @return Claims 对象，包含所有 Payload 数据
     * @throws JwtException 令牌无效或已过期时抛出
     */
    public static Claims parseJwt(String secretKey, String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

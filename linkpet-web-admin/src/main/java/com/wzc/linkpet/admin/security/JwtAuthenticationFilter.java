package com.wzc.linkpet.admin.security;

import com.wzc.linkpet.common.context.BaseContext;
import com.wzc.linkpet.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 认证过滤器（Admin 端）
 *
 * <p>工作流程：
 * <ol>
 *   <li>从请求头 {@code Authorization: Bearer <token>} 中提取 JWT</li>
 *   <li>解析 JWT，获取用户 ID 和角色信息</li>
 *   <li>将用户信息存入 {@link BaseContext}（ThreadLocal）和 Spring Security Context</li>
 *   <li>请求处理完毕后，清除 ThreadLocal 防止内存泄漏</li>
 * </ol>
 * </p>
 *
 * <p>此过滤器继承 {@link OncePerRequestFilter}，确保每个请求只执行一次。</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${linkpet.jwt.secret-key}")
    private String jwtSecretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 提取 Token
        String token = resolveToken(request);
        if (StringUtils.hasText(token)) {
            try {
                // 2. 解析 Token
                Claims claims = JwtUtil.parseJwt(jwtSecretKey, token);
                Long userId = Long.parseLong(claims.getSubject());
                String role = (String) claims.get("role");

                // 3. 存入 ThreadLocal（供 Service 层无侵入获取当前用户 ID）
                BaseContext.setCurrentId(userId);

                // 4. 构建 Authentication 并存入 Spring Security Context
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userId, null,
                                    List.of(new SimpleGrantedAuthority(role))
                            );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException e) {
                log.warn("JWT 解析失败：{}", e.getMessage());
                // Token 无效，不设置认证信息，后续安全框架会拦截需要权限的请求
            }
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            // 5. 请求处理完毕后清理 ThreadLocal，防止线程池复用引发数据泄漏
            BaseContext.removeCurrentId();
        }
    }

    /**
     * 从请求头提取 Bearer Token
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

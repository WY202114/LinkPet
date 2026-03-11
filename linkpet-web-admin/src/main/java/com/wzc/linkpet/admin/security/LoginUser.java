package com.wzc.linkpet.admin.security;

import com.wzc.linkpet.model.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security 用户主体（Admin 端）
 * 封装从数据库查询的 User 对象，实现 UserDetails 接口，
 * 供 Spring Security 的认证框架进行权限校验。
 */
@Getter
public class LoginUser implements UserDetails {

    /** 数据库中的用户实体 */
    private final User user;

    public LoginUser(User user) {
        this.user = user;
    }

    /**
     * 返回用户拥有的权限集合
     * 角色字符串直接映射为 GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /** 账号是否未过期—本项目不做有效期控制，始终返回 true */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** 账号是否未被锁定 */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** 凭证是否未过期 */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账号是否可用
     * 对应 user.status 字段：1-启用，0-禁用
     */
    @Override
    public boolean isEnabled() {
        return user.getStatus() != null && user.getStatus() == 1;
    }
}

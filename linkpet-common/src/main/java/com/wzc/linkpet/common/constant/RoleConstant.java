package com.wzc.linkpet.common.constant;

/**
 * 角色常量
 * 定义系统中所有的用户角色标识，与数据库 user.role 字段值对应。
 * 使用字符串常量而非枚举，方便与 Spring Security 的 GrantedAuthority 直接集成。
 */
public class RoleConstant {

    private RoleConstant() {
        // 工具类，禁止实例化
    }

    /** 普通用户 */
    public static final String ROLE_USER = "ROLE_USER";

    /** 后台管理员 */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
}

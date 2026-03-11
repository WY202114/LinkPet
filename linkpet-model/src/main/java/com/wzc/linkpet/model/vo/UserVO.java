package com.wzc.linkpet.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息 VO（返回给前端，不含敏感字段如密码）
 */
@Data
public class UserVO {

    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String phone;
    private String role;
    private Integer status;
    private LocalDateTime createTime;

    /** 登录成功时额外返回 JWT 令牌（其余接口此字段为 null） */
    private String token;
}

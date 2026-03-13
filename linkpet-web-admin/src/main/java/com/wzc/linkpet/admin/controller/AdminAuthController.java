package com.wzc.linkpet.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzc.linkpet.common.constant.RoleConstant;
import com.wzc.linkpet.common.exception.BusinessException;
import com.wzc.linkpet.common.exception.ErrorCode;
import com.wzc.linkpet.common.result.Result;
import com.wzc.linkpet.common.util.JwtUtil;
import com.wzc.linkpet.model.dto.auth.LoginDTO;
import com.wzc.linkpet.model.entity.User;
import com.wzc.linkpet.model.vo.UserVO;
import com.wzc.linkpet.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理端 - 登录认证接口
 */
@Slf4j
@Tag(name = "登录认证", description = "后台管理系统专属登录接口")
@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${linkpet.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${linkpet.jwt.ttl-millis}")
    private long jwtTtlMillis;

    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        // 1. 根据用户名查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, loginDTO.getUsername()));

        // 2. 校验账号密码
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户名或密码错误");
        }
        
        // 3. 校验是不是管理员角色
        if (!RoleConstant.ROLE_ADMIN.equals(user.getRole())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "非管理员无权登录后台系统");
        }
        
        // 4. 判断账号是否被禁用
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "账号已被禁用，请联系超级管理员");
        }

        // 5. 生成 JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        String token = JwtUtil.createJwt(jwtSecretKey, jwtTtlMillis, String.valueOf(user.getId()), claims);

        // 6. 封装返回值
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", userVO);
        
        return Result.success(result);
    }
}

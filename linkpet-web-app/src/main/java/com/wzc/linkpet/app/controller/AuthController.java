package com.wzc.linkpet.app.controller;

import com.wzc.linkpet.common.result.Result;
import com.wzc.linkpet.model.dto.auth.LoginDTO;
import com.wzc.linkpet.model.dto.auth.RegisterDTO;
import com.wzc.linkpet.model.vo.UserVO;
import com.wzc.linkpet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户端 - 认证接口（注册/登录）
 * 此接口路径在 SecurityConfig 中配置为公开访问，无需 JWT。
 */
@Tag(name = "认证", description = "用户注册与登录")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Valid RegisterDTO dto) {
        userService.register(dto);
        return Result.success();
    }

    @Operation(summary = "用户登录，返回JWT Token")
    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody @Valid LoginDTO dto) {
        return Result.success(userService.login(dto));
    }
}

package com.wzc.linkpet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzc.linkpet.common.constant.RoleConstant;
import com.wzc.linkpet.common.constant.StatusConstant;
import com.wzc.linkpet.common.exception.BusinessException;
import com.wzc.linkpet.common.exception.ErrorCode;
import com.wzc.linkpet.common.util.JwtUtil;
import com.wzc.linkpet.mapper.UserMapper;
import com.wzc.linkpet.model.dto.auth.LoginDTO;
import com.wzc.linkpet.model.dto.auth.RegisterDTO;
import com.wzc.linkpet.model.entity.User;
import com.wzc.linkpet.model.vo.UserVO;
import com.wzc.linkpet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${linkpet.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${linkpet.jwt.ttl-millis:86400000}")
    private long jwtTtlMillis;

    @Override
    public void register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, registerDTO.getUsername())
        );
        if (count > 0) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }
        // 构建用户实体
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(RoleConstant.ROLE_USER);
        user.setStatus(StatusConstant.ENABLE);
        userMapper.insert(user);
        log.info("新用户注册成功：username={}", user.getUsername());
    }

    @Override
    public UserVO login(LoginDTO loginDTO) {
        // 查询用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, loginDTO.getUsername())
        );
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        // 校验密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
        // 校验账号状态
        if (StatusConstant.DISABLE.equals(user.getStatus())) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }
        // 生成 JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        String token = JwtUtil.createJwt(jwtSecretKey, jwtTtlMillis, String.valueOf(user.getId()), claims);
        // 组装 VO
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        vo.setToken(token);
        log.info("用户登录成功：userId={}", user.getId());
        return vo;
    }

    @Override
    public UserVO getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @Override
    public void updateStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        user.setStatus(status);
        userMapper.updateById(user);
        log.info("用户状态更新：userId={}, status={}", userId, status);
    }
}

package com.wzc.linkpet.service;

import com.wzc.linkpet.model.dto.auth.LoginDTO;
import com.wzc.linkpet.model.dto.auth.RegisterDTO;
import com.wzc.linkpet.model.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     */
    void register(RegisterDTO registerDTO);

    /**
     * 用户登录，返回包含 JWT token 的用户信息
     *
     * @param loginDTO 登录信息
     * @return 用户VO（含token）
     */
    UserVO login(LoginDTO loginDTO);

    /**
     * 根据 ID 查询用户信息
     *
     * @param userId 用户ID
     * @return 用户VO
     */
    UserVO getUserById(Long userId);

    /**
     * 禁用/启用用户（管理端使用）
     *
     * @param userId 用户ID
     * @param status 目标状态：1-启用，0-禁用
     */
    void updateStatus(Long userId, Integer status);
}

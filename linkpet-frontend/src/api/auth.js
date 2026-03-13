/**
 * 认证相关 API
 * POST /auth/register — 注册
 * POST /auth/login    — 登录（返回包含 token 的用户信息）
 */
import http from '@/utils/http.js'

/**
 * 用户注册
 * @param {{ username, password, nickname, phone? }} data
 */
export const register = (data) => http.post('/auth/register', data)

/**
 * 用户登录
 * @param {{ username, password }} data
 * @returns {Promise<{ id, username, nickname, avatar, phone, role, token }>}
 */
export const login = (data) => http.post('/auth/login', data)

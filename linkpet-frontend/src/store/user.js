/**
 * LinkPet — 用户状态管理
 * 使用 Vue reactive 实现轻量级全局 store，持久化到 localStorage
 * 无需 Pinia 等额外依赖
 */
import { reactive, computed } from 'vue'
import { getImageUrl } from '@/utils/http.js'

const USER_KEY  = 'lp_user'
const TOKEN_KEY = 'lp_token'

// 全局单例状态
const state = reactive({
  token:    localStorage.getItem(TOKEN_KEY) || '',
  userInfo: (() => {
    try { return JSON.parse(localStorage.getItem(USER_KEY)) } catch { return null }
  })(),
})

export function useUserStore() {
  /** 是否已登录 */
  const isLoggedIn = computed(() => !!state.token)

  /** 当前用户头像完整 URL（无头像时返回 Gravatar 占位） */
  const avatarUrl = computed(() => {
    const av = state.userInfo?.avatar
    if (av) return getImageUrl(av)
    return `https://i.pravatar.cc/80?u=${state.userInfo?.id ?? 'guest'}`
  })

  /**
   * 登录成功后保存用户信息与 Token
   * @param {{ id, username, nickname, avatar, role, token }} data
   */
  const setUser = (data) => {
    state.token    = data.token
    state.userInfo = data
    localStorage.setItem(TOKEN_KEY, data.token)
    localStorage.setItem(USER_KEY, JSON.stringify(data))
  }

  /** 退出登录 */
  const logout = () => {
    state.token    = ''
    state.userInfo = null
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  }

  return { state, isLoggedIn, avatarUrl, setUser, logout }
}

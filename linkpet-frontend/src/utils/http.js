/**
 * LinkPet — Axios HTTP 实例
 * 统一处理：baseURL、JWT注入、响应解包、401跳转
 */
import axios from 'axios'

// 后端基础地址
export const BASE_URL   = 'http://localhost:8080'
// MinIO 图片地址前缀
export const MINIO_BASE = 'http://localhost:9000/linkpet'

/**
 * 将 MinIO 相对路径拼接为完整 URL
 * 若 path 已是完整 URL 则直接返回
 */
export const getImageUrl = (path) => {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return `${MINIO_BASE}/${path}`
}

// 创建 axios 实例
const http = axios.create({
  baseURL: BASE_URL,
  timeout: 10000,
})

// ── 请求拦截器：自动注入 JWT ──────────────────────────────────
http.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('lp_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// ── 响应拦截器：解包 { code, message, data } ─────────────────
http.interceptors.response.use(
  (response) => {
    const { code, message, data } = response.data
    if (code === 200) return data
    // 业务错误
    return Promise.reject(new Error(message || '请求失败'))
  },
  (error) => {
    if (error.response?.status === 401) {
      // Token 失效，清除本地存储
      localStorage.removeItem('lp_token')
      localStorage.removeItem('lp_user')
    }
    const msg = error.response?.data?.message || error.message || '网络错误'
    return Promise.reject(new Error(msg))
  }
)

export default http

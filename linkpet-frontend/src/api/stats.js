/**
 * 首页统计 API
 * GET /stats — 获取首页统计数据（访客可访问）
 */
import http from '@/utils/http.js'

/**
 * 获取首页统计数据
 * @returns {Promise<{ adoptionCount, petWaitingCount, userCount }>}
 */
export const getHomeStats = () => http.get('/stats')

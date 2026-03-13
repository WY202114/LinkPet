/**
 * 领养申请 API
 * POST /adoptions      — 提交申请（需登录）
 * GET  /adoptions/my   — 我的申请列表（需登录）
 */
import http from '@/utils/http.js'

/**
 * 提交领养申请
 * @param {{ petId, reason, contactPhone }} data
 */
export const applyAdoption = (data) => http.post('/adoptions', data)

/**
 * 获取当前用户的领养申请列表
 * @param {{ page?, pageSize? }} params
 * @returns {Promise<{ records, total, page, pageSize }>}
 */
export const getMyAdoptions = (params = {}) =>
  http.get('/adoptions/my', { params: { page: 1, pageSize: 10, ...params } })

/**
 * 宠物相关 API
 * GET    /pets       — 分页列表（支持 typeId / gender / keyword 过滤）
 * GET    /pets/{id}  — 详情
 * POST   /pets       — 发布宠物（需登录）
 * PUT    /pets/{id}  — 更新（需登录）
 * DELETE /pets/{id}  — 下架（需登录）
 */
import http from '@/utils/http.js'

/**
 * 获取宠物列表
 * @param {{ typeId?, gender?, keyword?, page?, pageSize? }} params
 * @returns {Promise<{ records, total, page, pageSize }>}
 */
export const getPets = (params = {}) =>
  http.get('/pets', { params: { page: 1, pageSize: 50, ...params } })

/**
 * 获取宠物详情
 * @param {number} id
 */
export const getPetById = (id) => http.get(`/pets/${id}`)

/**
 * 发布宠物
 * @param {{ name, typeId, ageMonth, gender, healthDesc, personalityDesc, images, address, location? }} data
 */
export const createPet = (data) => http.post('/pets', data)

/**
 * 更新宠物信息
 * @param {number} id
 * @param {object} data
 */
export const updatePet = (id, data) => http.put(`/pets/${id}`, data)

/**
 * 下架宠物
 * @param {number} id
 */
export const deletePet = (id) => http.delete(`/pets/${id}`)

/**
 * 获取当前用户发布的宠物列表
 * @param {{ page?, pageSize? }} params
 */
export const getMyPets = (params = {}) =>
  http.get('/pets/my', { params: { page: 1, pageSize: 10, ...params } })

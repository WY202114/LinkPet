/**
 * 宠物类型 API
 * GET /pet-types — 获取所有类型（猫、狗等）
 */
import http from '@/utils/http.js'

/**
 * 获取所有宠物类型
 * @returns {Promise<Array<{ id, name, icon, description, sort, status }>>}
 */
export const getPetTypes = () => http.get('/pet-types')

/**
 * 社区帖子 API
 * GET    /posts        — 分页列表
 * GET    /posts/{id}   — 详情（含评论）
 * POST   /posts        — 发帖（需登录）
 * PUT    /posts/{id}   — 编辑（需登录）
 * DELETE /posts/{id}   — 删除（需登录）
 * POST   /posts/{id}/like — 点赞/取消点赞（需登录）
 */
import http from '@/utils/http.js'

/**
 * 获取社区帖子列表
 * @param {{ keyword?, page?, pageSize? }} params
 * @returns {Promise<{ records, total, page, pageSize }>}
 */
export const getPosts = (params = {}) =>
  http.get('/posts', { params: { page: 1, pageSize: 6, ...params } })

/**
 * 获取帖子详情
 * @param {number} id
 */
export const getPostById = (id) => http.get(`/posts/${id}`)

/**
 * 发布帖子
 * @param {{ title, content, images?, tags? }} data
 */
export const createPost = (data) => http.post('/posts', data)

/**
 * 编辑帖子
 * @param {number} id
 * @param {object} data
 */
export const updatePost = (id, data) => http.put(`/posts/${id}`, data)

/**
 * 删除帖子
 * @param {number} id
 */
export const deletePost = (id) => http.delete(`/posts/${id}`)

/**
 * 点赞 / 取消点赞
 * @param {number} id
 */
export const likePost = (id) => http.post(`/posts/${id}/like`)

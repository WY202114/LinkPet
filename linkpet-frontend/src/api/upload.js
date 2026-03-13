/**
 * 文件上传 API
 * POST /upload/image — 上传图片到 MinIO（需登录）
 * 返回: MinIO 对象路径，如 "images/uuid-xxx.jpg"
 * 完整访问 URL = http://localhost:9000/linkpet/{返回值}
 */
import http from '@/utils/http.js'

/**
 * 上传图片
 * @param {File} file
 * @returns {Promise<string>} MinIO 相对路径
 */
export const uploadImage = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return http.post('/upload/image', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

/**
 * LinkPet — 数据格式化工具
 * 将后端返回的 pet / post 数据转换为前端组件所需格式
 */
import { getImageUrl } from './http.js'

// 占位图（API无图时使用）
const PET_FALLBACK = [
  'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=500&q=80',
  'https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=500&q=80',
  'https://images.unsplash.com/photo-1573865526739-10659fec78a5?w=500&q=80',
  'https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=500&q=80',
]
const POST_FALLBACK = [
  'https://images.unsplash.com/photo-1601758124510-52d02ddb7cbd?w=700&q=80',
  'https://images.unsplash.com/photo-1506555191898-aef7ad9d8d0c?w=500&q=80',
  'https://images.unsplash.com/photo-1583511655826-05700442b0ae?w=500&q=80',
  'https://images.unsplash.com/photo-1450778869180-41d0601e046e?w=500&q=80',
]

// 月龄 → 可读字符串
const formatAge = (months) => {
  if (months == null) return 'Unknown'
  if (months < 12) return `${months} month${months !== 1 ? 's' : ''}`
  const years = Math.floor(months / 12)
  const rem   = months % 12
  if (rem === 0) return `${years} year${years !== 1 ? 's' : ''}`
  return `${years}y ${rem}m`
}

// 逗号分隔的性格描述 → 标签数组（最多3个）
const formatTags = (desc) => {
  if (!desc) return []
  return desc.split(/[,，]/).map(t => t.trim()).filter(Boolean).slice(0, 3)
}

// 日期格式化
const formatDate = (isoStr) => {
  if (!isoStr) return ''
  const d = new Date(isoStr)
  return d.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' })
}

/**
 * 后端 Pet 对象 → PetCard.vue props 格式
 */
export const formatPet = (pet, index = 0) => ({
  id:       pet.id,
  name:     pet.name,
  type:     pet.typeName || 'Pet',
  typeKey:  (pet.typeName || '').toLowerCase(),
  age:      formatAge(pet.ageMonth),
  location: pet.address || pet.location || '',
  tags:     formatTags(pet.personalityDesc),
  desc:     pet.healthDesc || pet.personalityDesc || '',
  image:    pet.images?.length
              ? getImageUrl(pet.images[0])
              : PET_FALLBACK[index % PET_FALLBACK.length],
  urgent:   pet.status === 1 && (pet.ageMonth ?? 0) > 36,
})

/**
 * 后端 Post 对象 → CommunitySection story 格式
 */
export const formatPost = (post, index = 0) => ({
  id:       post.id,
  title:    post.title,
  tag:      post.tags?.split(',')[0]?.trim() || 'Story',
  excerpt:  post.content
              ? (post.content.length > 120
                  ? post.content.slice(0, 120) + '…'
                  : post.content)
              : '',
  author:   post.userNickname || 'Community Member',
  date:     formatDate(post.createTime),
  likes:    post.likeCount    ?? 0,
  comments: post.commentCount ?? 0,
  image:    post.images?.length
              ? getImageUrl(post.images[0])
              : POST_FALLBACK[index % POST_FALLBACK.length],
  avatar:   post.userAvatar
              ? getImageUrl(post.userAvatar)
              : `https://i.pravatar.cc/80?u=${post.userId}`,
})

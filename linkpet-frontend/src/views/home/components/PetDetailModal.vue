<template>
  <Teleport to="body">
    <div v-if="visible" class="detail-overlay" @click.self="close">
      <div class="detail-card">
        <!-- Close button -->
        <button class="detail-close" @click="close">&times;</button>

        <!-- Loading -->
        <div v-if="loading" class="detail-loading">
          <div class="spinner"></div>
          <p>加载中...</p>
        </div>

        <!-- Content -->
        <template v-else-if="pet">
          <!-- Image gallery -->
          <div class="detail-gallery">
            <div class="gallery-main">
              <img :src="activeImage" :alt="pet.name" class="gallery-img" @error="onImgError" />
              <!-- Nav arrows (only if multiple images) -->
              <template v-if="imageList.length > 1">
                <button class="gallery-arrow gallery-arrow--left" @click="prevImage">&lsaquo;</button>
                <button class="gallery-arrow gallery-arrow--right" @click="nextImage">&rsaquo;</button>
              </template>
              <!-- Image counter -->
              <span v-if="imageList.length > 1" class="gallery-counter">
                {{ activeIndex + 1 }} / {{ imageList.length }}
              </span>
            </div>
            <!-- Thumbnails -->
            <div v-if="imageList.length > 1" class="gallery-thumbs">
              <img
                v-for="(img, i) in imageList"
                :key="i"
                :src="img"
                :alt="`${pet.name} ${i + 1}`"
                class="gallery-thumb"
                :class="{ 'gallery-thumb--active': i === activeIndex }"
                @click="activeIndex = i"
                @error="onThumbError($event, i)"
              />
            </div>
          </div>

          <!-- Info section -->
          <div class="detail-info">
            <!-- Pet name & type badge -->
            <div class="detail-header">
              <h2 class="detail-name">{{ pet.name }}</h2>
              <span class="detail-type-badge">{{ pet.typeName || '宠物' }}</span>
            </div>

            <!-- Key attributes -->
            <div class="detail-attrs">
              <div class="attr-item">
                <span class="attr-label">年龄</span>
                <span class="attr-value">{{ formatAge(pet.ageMonth) }}</span>
              </div>
              <div class="attr-item">
                <span class="attr-label">性别</span>
                <span class="attr-value">{{ pet.genderDesc || '未知' }}</span>
              </div>
              <div class="attr-item">
                <span class="attr-label">状态</span>
                <span class="attr-value">{{ pet.statusDesc || '待领养' }}</span>
              </div>
              <div v-if="pet.address" class="attr-item">
                <span class="attr-label">地址</span>
                <span class="attr-value">{{ pet.address }}</span>
              </div>
            </div>

            <!-- Description -->
            <div v-if="pet.description" class="detail-desc">
              <h4 class="detail-section-title">描述</h4>
              <p class="detail-desc-text">{{ pet.description }}</p>
            </div>

            <!-- Publisher info -->
            <div class="detail-publisher">
              <h4 class="detail-section-title">发布者</h4>
              <div class="publisher-row">
                <img :src="publisherAvatar" alt="发布者头像" class="publisher-avatar" @error="onAvatarError" />
                <div class="publisher-info">
                  <span class="publisher-name">{{ pet.userNickname || '匿名用户' }}</span>
                  <span class="publisher-time">发布于 {{ formatDate(pet.createTime) }}</span>
                </div>
              </div>
            </div>

            <!-- Action -->
            <div class="detail-actions">
              <button class="btn-adopt" @click="handleApply">申请领养</button>
            </div>
          </div>
        </template>

        <!-- Error -->
        <div v-else class="detail-loading">
          <p>加载失败，请重试</p>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { getPetById } from '@/api/pets.js'
import { getImageUrl } from '@/utils/http.js'

const props = defineProps({
  visible: Boolean,
  petId: { type: Number, default: null },
})
const emit = defineEmits(['close'])

const pet = ref(null)
const loading = ref(false)
const activeIndex = ref(0)

const FALLBACK_IMG = 'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=600&q=80'

// Compute image list from pet.images
const imageList = computed(() => {
  if (!pet.value?.images?.length) return [FALLBACK_IMG]
  return pet.value.images.map(img => getImageUrl(img))
})

const activeImage = computed(() => imageList.value[activeIndex.value] || FALLBACK_IMG)

const publisherAvatar = computed(() => {
  if (pet.value?.userAvatar) return getImageUrl(pet.value.userAvatar)
  return `https://i.pravatar.cc/80?u=${pet.value?.userId || 0}`
})

// Gallery navigation
const prevImage = () => {
  activeIndex.value = (activeIndex.value - 1 + imageList.value.length) % imageList.value.length
}
const nextImage = () => {
  activeIndex.value = (activeIndex.value + 1) % imageList.value.length
}

// Formatters
const formatAge = (months) => {
  if (months == null) return '未知'
  if (months < 12) return `${months} 个月`
  const years = Math.floor(months / 12)
  const rem = months % 12
  if (rem === 0) return `${years} 岁`
  return `${years} 岁 ${rem} 个月`
}

const formatDate = (isoStr) => {
  if (!isoStr) return ''
  const d = new Date(isoStr)
  return d.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
}

// Image error handlers
const onImgError = (e) => { e.target.src = FALLBACK_IMG }
const onThumbError = (e) => { e.target.src = FALLBACK_IMG }
const onAvatarError = (e) => { e.target.src = `https://i.pravatar.cc/80?u=${pet.value?.userId || 0}` }

// Fetch pet detail when modal opens
watch(() => [props.visible, props.petId], async ([vis, id]) => {
  if (vis && id) {
    loading.value = true
    activeIndex.value = 0
    try {
      pet.value = await getPetById(id)
    } catch {
      pet.value = null
    } finally {
      loading.value = false
    }
  }
}, { immediate: true })

const close = () => {
  emit('close')
}

const handleApply = () => {
  const token = localStorage.getItem('lp_token')
  if (!token) {
    alert('请先登录后再申请领养')
    return
  }
  alert('领养申请功能即将上线，敬请期待！')
}
</script>

<style scoped>
/* ── Overlay ── */
.detail-overlay {
  position: fixed;
  inset: 0;
  z-index: 9000;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(61, 43, 31, 0.5);
  backdrop-filter: blur(8px);
  animation: fadeIn 0.2s ease;
}

/* ── Card ── */
.detail-card {
  position: relative;
  width: 780px;
  max-width: 94vw;
  max-height: 90vh;
  overflow-y: auto;
  background: var(--warm-white, #FBF5E6);
  border-radius: 20px;
  box-shadow: 0 24px 64px rgba(61, 43, 31, 0.4);
  animation: slideUp 0.3s ease;
  display: flex;
  flex-direction: column;
}

.detail-close {
  position: absolute;
  top: 12px;
  right: 16px;
  z-index: 10;
  background: rgba(247, 237, 216, 0.85);
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  font-size: 1.5rem;
  cursor: pointer;
  color: var(--brown, #3D2B1F);
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(6px);
  transition: all 0.2s;
}
.detail-close:hover {
  background: var(--amber, #C47F35);
  color: white;
}

/* ── Gallery ── */
.detail-gallery {
  flex-shrink: 0;
}

.gallery-main {
  position: relative;
  width: 100%;
  height: 360px;
  overflow: hidden;
  border-radius: 20px 20px 0 0;
  background: rgba(196, 127, 53, 0.08);
}

.gallery-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: opacity 0.3s ease;
}

.gallery-arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: none;
  background: rgba(247, 237, 216, 0.85);
  color: var(--brown, #3D2B1F);
  font-size: 1.5rem;
  cursor: pointer;
  backdrop-filter: blur(6px);
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}
.gallery-arrow:hover {
  background: var(--amber, #C47F35);
  color: white;
}
.gallery-arrow--left { left: 12px; }
.gallery-arrow--right { right: 12px; }

.gallery-counter {
  position: absolute;
  bottom: 12px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(61, 43, 31, 0.6);
  color: white;
  font-size: 0.75rem;
  padding: 0.2rem 0.8rem;
  border-radius: 12px;
  backdrop-filter: blur(4px);
}

.gallery-thumbs {
  display: flex;
  gap: 6px;
  padding: 10px 16px;
  overflow-x: auto;
  background: rgba(196, 127, 53, 0.04);
}

.gallery-thumb {
  width: 56px;
  height: 56px;
  border-radius: 8px;
  object-fit: cover;
  cursor: pointer;
  border: 2px solid transparent;
  opacity: 0.6;
  transition: all 0.2s;
  flex-shrink: 0;
}
.gallery-thumb:hover { opacity: 0.9; }
.gallery-thumb--active {
  border-color: var(--amber, #C47F35);
  opacity: 1;
}

/* ── Info ── */
.detail-info {
  padding: 1.4rem 1.8rem 1.6rem;
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
}

.detail-header {
  display: flex;
  align-items: center;
  gap: 0.8rem;
}

.detail-name {
  font-family: var(--font-serif, serif);
  font-size: 1.6rem;
  font-weight: 700;
  color: var(--brown, #3D2B1F);
  margin: 0;
}

.detail-type-badge {
  background: rgba(247, 237, 216, 0.9);
  color: var(--amber-dark, #8B5B1C);
  border: 1px solid rgba(196, 127, 53, 0.3);
  font-size: 0.72rem;
  font-weight: 700;
  letter-spacing: 0.05em;
  padding: 0.2rem 0.7rem;
  border-radius: 16px;
}

/* Key attributes */
.detail-attrs {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 0.8rem;
}

.attr-item {
  background: rgba(196, 127, 53, 0.06);
  border-radius: 12px;
  padding: 0.6rem 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.15rem;
}

.attr-label {
  font-size: 0.72rem;
  font-weight: 700;
  color: var(--brown-mid, #5C3D2E);
  opacity: 0.7;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.attr-value {
  font-size: 0.92rem;
  font-weight: 600;
  color: var(--brown, #3D2B1F);
}

/* Description */
.detail-section-title {
  font-family: var(--font-serif, serif);
  font-size: 0.95rem;
  font-weight: 700;
  color: var(--brown, #3D2B1F);
  margin: 0 0 0.4rem;
}

.detail-desc-text {
  font-size: 0.9rem;
  line-height: 1.65;
  color: var(--brown-mid, #5C3D2E);
  margin: 0;
}

/* Publisher */
.detail-publisher {
  border-top: 1px solid rgba(196, 127, 53, 0.12);
  padding-top: 1rem;
}

.publisher-row {
  display: flex;
  align-items: center;
  gap: 0.8rem;
}

.publisher-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(196, 127, 53, 0.2);
}

.publisher-info {
  display: flex;
  flex-direction: column;
  gap: 0.1rem;
}

.publisher-name {
  font-size: 0.95rem;
  font-weight: 700;
  color: var(--brown, #3D2B1F);
}

.publisher-time {
  font-size: 0.78rem;
  color: var(--brown-mid, #5C3D2E);
  opacity: 0.65;
}

/* Actions */
.detail-actions {
  display: flex;
  justify-content: flex-end;
  padding-top: 0.4rem;
}

.btn-adopt {
  padding: 0.7rem 2.2rem;
  border-radius: 30px;
  border: none;
  background: var(--amber, #C47F35);
  color: white;
  font-family: var(--font-sans, sans-serif);
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.25s;
  box-shadow: 0 4px 18px rgba(196, 127, 53, 0.35);
}
.btn-adopt:hover {
  background: var(--amber-dark, #8B5B1C);
  transform: translateY(-2px);
  box-shadow: 0 6px 24px rgba(196, 127, 53, 0.45);
}

/* Loading */
.detail-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 5rem 2rem;
  gap: 1rem;
  color: var(--brown-mid, #5C3D2E);
}

.spinner {
  width: 36px;
  height: 36px;
  border: 3px solid rgba(196, 127, 53, 0.2);
  border-top-color: var(--amber, #C47F35);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

/* Animations */
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
@keyframes slideUp { from { opacity: 0; transform: translateY(30px); } to { opacity: 1; transform: translateY(0); } }
@keyframes spin { to { transform: rotate(360deg); } }

/* Scrollbar */
.detail-card::-webkit-scrollbar { width: 6px; }
.detail-card::-webkit-scrollbar-thumb { background: rgba(196, 127, 53, 0.3); border-radius: 3px; }

/* Responsive */
@media (max-width: 600px) {
  .detail-card { max-width: 100vw; border-radius: 16px; }
  .gallery-main { height: 260px; }
  .detail-info { padding: 1rem 1.2rem 1.4rem; }
  .detail-name { font-size: 1.3rem; }
  .detail-attrs { grid-template-columns: 1fr 1fr; }
}
</style>

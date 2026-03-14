<template>
  <section class="section community-section">
    <!-- Section header -->
    <div class="section__header">
      <p class="section__tag">From our LinkPet family</p>
      <h2 class="section__title">温馨故事</h2>
      <p class="section__desc">
        真实瞬间，真爱 — 社区成员写下的日记，证明每一只流浪动物都值得第二次机会。
      </p>
      <div class="community-actions">
        <button class="btn-cta btn-cta--light" @click="openAddPost">✍️ 发布分享</button>
      </div>
    </div>

    <!-- Add Post Modal -->
    <AddPostModal :visible="showAddModal" @close="showAddModal = false" @success="onPostAdded" />

    <!-- Loading skeleton -->
    <div v-if="loading" class="posts-grid">
      <div v-for="n in 8" :key="n" class="skeleton-card">
        <div class="skeleton-img"></div>
        <div class="skeleton-body">
          <div class="skeleton-line skeleton-line--wide"></div>
          <div class="skeleton-line"></div>
        </div>
      </div>
    </div>

    <!-- Error state -->
    <div v-else-if="error" class="state-msg state-msg--error">
      <p>{{ error }}</p>
      <button class="btn-retry" @click="fetchPosts">Retry</button>
    </div>

    <!-- Empty state -->
    <div v-else-if="postList.length === 0" class="state-msg">
      <p>No community stories yet. Be the first to share!</p>
    </div>

    <!-- Post cards grid -->
    <div v-else class="posts-grid">
      <div v-for="post in postList" :key="post.id" class="post-card">
        <div class="post-card__img-wrap">
          <img :src="post.image" :alt="post.title" class="post-card__img" @error="onImgError" />
          <button
            v-if="canDelete(post)"
            class="post-card__delete"
            title="Delete post"
            @click.stop="handleDelete(post)"
          >
            &times;
          </button>
        </div>
        <div class="post-card__body">
          <h3 class="post-card__title">{{ post.title }}</h3>
          <div class="post-card__author">
            <img :src="post.avatar" :alt="post.author" class="post-card__avatar" />
            <span class="post-card__name">{{ post.author }}</span>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getPosts, deletePost } from '@/api/posts.js'
import { formatPost } from '@/utils/format.js'
import { stories as mockStories } from '@/data/mockData.js'
import AddPostModal from './AddPostModal.vue'
import { useUserStore } from '@/store/user.js'

const { state: userState } = useUserStore()
const isAdmin = computed(() => userState.userInfo?.role === 'ROLE_ADMIN')
const currentUserId = computed(() => userState.userInfo?.id)
const canDelete = (post) => isAdmin.value || String(post.userId) === String(currentUserId.value)

const postList = ref([])
const loading  = ref(false)
const error    = ref('')
const showAddModal = ref(false)

const handleDelete = async (post) => {
  if (!confirm(`确定删除「${post.title}」吗？`)) return
  try {
    await deletePost(post.id)
    postList.value = postList.value.filter(p => p.id !== post.id)
  } catch (e) {
    alert('删除失败：' + (e.message || '未知错误'))
  }
}

const onImgError = (e) => {
  e.target.src = 'https://images.unsplash.com/photo-1601758124510-52d02ddb7cbd?w=700&q=80'
}

const fetchPosts = async () => {
  loading.value = true
  error.value   = ''
  try {
    const result = await getPosts({ page: 1, pageSize: 8 })
    postList.value = result.records.map((p, i) => formatPost(p, i))
  } catch (e) {
    error.value = e.message || 'Failed to load stories. Showing sample data.'
    postList.value = mockStories
  } finally {
    loading.value = false
  }
}

onMounted(fetchPosts)

// ── Add Post ───────────────────────────────────────────────────
const openAddPost = () => {
  const token = localStorage.getItem('lp_token')
  if (!token) {
    alert('请先登录后再发布故事')
    return
  }
  showAddModal.value = true
}

const onPostAdded = () => {
  showAddModal.value = false
  fetchPosts()
}
</script>

<style scoped>
.community-actions {
  margin-top: 1.5rem;
}
/* Post cards grid — same layout as adoption */
.posts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 1rem;
  width: 100%;
  position: relative;
  z-index: 1;
}

/* Post card */
.post-card {
  background: var(--cream);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 16px var(--warm-shadow), 0 1px 4px rgba(196, 127, 53, 0.08);
  transition: transform 0.35s ease, box-shadow 0.35s ease;
  border: 1px solid rgba(196, 127, 53, 0.12);
  cursor: pointer;
}
.post-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 32px var(--warm-shadow-lg), 0 3px 10px rgba(196, 127, 53, 0.15);
}

.post-card__img-wrap {
  position: relative;
  height: 180px;
  overflow: hidden;
}
.post-card__img {
  width: 100%; height: 100%;
  object-fit: cover;
  transition: transform 0.55s ease;
  filter: sepia(18%) saturate(115%) contrast(108%) brightness(96%) hue-rotate(5deg);
}
.post-card:hover .post-card__img { transform: scale(1.07); }

/* Delete button (admin only) */
.post-card__delete {
  position: absolute;
  top: 6px; right: 6px;
  width: 24px; height: 24px;
  border-radius: 50%;
  border: none;
  background: rgba(180, 40, 40, 0.85);
  color: #fff;
  font-size: 1rem;
  line-height: 1;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s, transform 0.2s;
  z-index: 2;
}
.post-card:hover .post-card__delete { opacity: 1; }
.post-card__delete:hover { transform: scale(1.15); background: rgba(180, 40, 40, 1); }

.post-card__body {
  padding: 0.5rem 0.75rem 0.6rem;
}

.post-card__title {
  font-family: var(--font-serif);
  font-size: 0.88rem;
  font-weight: 700;
  color: var(--brown);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 0.3rem;
}

.post-card__author {
  display: flex;
  align-items: center;
  gap: 0.35rem;
}
.post-card__avatar {
  width: 20px; height: 20px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid rgba(196, 127, 53, 0.25);
}
.post-card__name {
  font-size: 0.7rem;
  color: var(--brown-mid);
  opacity: 0.75;
}

/* Loading skeleton */
.skeleton-card {
  background: var(--cream);
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid rgba(196, 127, 53, 0.1);
}
.skeleton-img {
  height: 180px;
  background: linear-gradient(90deg, #EDD9AB 25%, #F7EDD8 50%, #EDD9AB 75%);
  background-size: 200% 100%;
  animation: skeletonWave 1.5s infinite;
}
.skeleton-body { padding: 0.8rem; display: flex; flex-direction: column; gap: 0.5rem; }
.skeleton-line {
  height: 10px;
  border-radius: 5px;
  background: linear-gradient(90deg, #EDD9AB 25%, #F7EDD8 50%, #EDD9AB 75%);
  background-size: 200% 100%;
  animation: skeletonWave 1.5s infinite;
}
.skeleton-line--wide { width: 70%; }

@keyframes skeletonWave {
  0%   { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

/* State messages */
.state-msg {
  text-align: center;
  padding: 4rem 2rem;
  color: var(--brown-mid);
  font-size: 1rem;
  opacity: 0.7;
}
.state-msg--error { color: #b91c1c; opacity: 1; }
.btn-retry {
  margin-top: 1rem;
  padding: 0.5rem 1.5rem;
  background: var(--green-vintage);
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-family: var(--font-sans);
  font-weight: 700;
  transition: background 0.2s;
}
.btn-retry:hover { background: var(--green-light); }

@media (max-width: 768px) {
  .posts-grid { grid-template-columns: 1fr; max-width: 420px; margin: 0 auto; }
}
</style>

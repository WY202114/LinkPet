<template>
  <section class="section community-section">
    <!-- Section header -->
    <div class="section__header">
      <p class="section__tag">From our LinkPet family</p>
      <h2 class="section__title">Heartwarming Stories</h2>
      <p class="section__desc">
        Real moments, real love — diaries written by the community that proves
        every stray deserves a second chance.
      </p>
    </div>

    <!-- Loading skeleton -->
    <div v-if="loading" class="stories-layout">
      <div class="skeleton-featured"></div>
      <div class="stories-side">
        <div v-for="n in 3" :key="n" class="skeleton-story-card"></div>
      </div>
    </div>

    <!-- Error state -->
    <div v-else-if="error" class="state-msg state-msg--error">
      <p>⚠️ {{ error }}</p>
      <button class="btn-retry" @click="fetchPosts">Retry</button>
    </div>

    <!-- Stories layout: featured large + side card list -->
    <div v-else-if="postList.length" class="stories-layout">
      <!-- Featured story (first post) -->
      <div class="story-featured">
        <div class="story-featured__img-wrap">
          <img :src="postList[0].image" :alt="postList[0].title" class="story-featured__img" />
          <div class="story-featured__paint-overlay"></div>
        </div>
        <div class="story-featured__content">
          <span class="story-tag">{{ postList[0].tag }}</span>
          <h3 class="story-featured__title">{{ postList[0].title }}</h3>
          <p class="story-featured__excerpt">{{ postList[0].excerpt }}</p>
          <div class="story-featured__meta">
            <img :src="postList[0].avatar" :alt="postList[0].author" class="story-featured__avatar" />
            <div>
              <p class="story-featured__author">{{ postList[0].author }}</p>
              <p class="story-featured__date">{{ postList[0].date }}</p>
            </div>
          </div>
          <button class="btn-read-more">Read Full Story →</button>
        </div>
      </div>

      <!-- Side story cards -->
      <div class="stories-side">
        <StoryCard
          v-for="story in postList.slice(1)"
          :key="story.id"
          :story="story"
        />
      </div>
    </div>

    <!-- Empty state -->
    <div v-else class="state-msg">
      <p>🐾 No community stories yet. Be the first to share!</p>
    </div>

    <!-- Pagination dots -->
    <div class="carousel-dots">
      <span v-for="n in 4" :key="n" class="dot" :class="{ 'dot--active': n === 1 }"></span>
    </div>

    <div class="section__footer">
      <button class="btn-view-all btn-view-all--green">Explore Community</button>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import StoryCard from './StoryCard.vue'
import { getPosts } from '@/api/posts.js'
import { formatPost } from '@/utils/format.js'
import { stories as mockStories } from '@/data/mockData.js'

// ── State ─────────────────────────────────────────────────────
const postList = ref([])
const loading  = ref(false)
const error    = ref('')

// ── Fetch posts ───────────────────────────────────────────────
const fetchPosts = async () => {
  loading.value = true
  error.value   = ''
  try {
    const result = await getPosts({ page: 1, pageSize: 4 })
    postList.value = result.records.map((p, i) => formatPost(p, i))
  } catch (e) {
    error.value = e.message || 'Failed to load stories. Showing sample data.'
    // Graceful fallback to mock data
    postList.value = mockStories
  } finally {
    loading.value = false
  }
}

onMounted(fetchPosts)
</script>

<style scoped>
.community-section {
  background: linear-gradient(180deg, #F0E6CA 0%, #F7EDD8 100%);
}

/* Stories layout */
.stories-layout {
  display: grid;
  grid-template-columns: 1.45fr 1fr;
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

/* ── Featured story ── */
.story-featured {
  background: var(--cream);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 8px 36px var(--warm-shadow);
  display: flex;
  flex-direction: column;
  border: 1px solid rgba(196, 127, 53, 0.1);
}
.story-featured__img-wrap {
  position: relative;
  height: 300px;
  overflow: hidden;
  flex-shrink: 0;
}
.story-featured__img {
  width: 100%; height: 100%;
  object-fit: cover;
  filter: sepia(22%) saturate(110%) contrast(105%) brightness(95%);
  transition: transform 0.5s ease;
}
.story-featured:hover .story-featured__img { transform: scale(1.04); }
.story-featured__paint-overlay {
  position: absolute; inset: 0;
  background: linear-gradient(to bottom, rgba(196, 127, 53, 0.05), rgba(61, 43, 31, 0.4));
}
.story-featured__content { padding: 1.8rem 2rem 2rem; }
.story-featured__title {
  font-family: var(--font-serif);
  font-size: 1.45rem;
  font-weight: 700;
  color: var(--brown);
  margin-bottom: 0.9rem;
  line-height: 1.3;
}
.story-featured__excerpt {
  font-size: 0.92rem;
  line-height: 1.75;
  color: var(--brown-mid);
  margin-bottom: 1.4rem;
  opacity: 0.9;
}
.story-featured__meta {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  margin-bottom: 1.3rem;
}
.story-featured__avatar {
  width: 40px; height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(196, 127, 53, 0.3);
  filter: sepia(10%) saturate(110%);
}
.story-featured__author { font-size: 0.88rem; font-weight: 700; color: var(--brown); margin-bottom: 0.1rem; }
.story-featured__date   { font-size: 0.75rem; color: var(--brown-mid); opacity: 0.7; }

.btn-read-more {
  background: none;
  border: none;
  color: var(--amber-dark);
  font-family: var(--font-sans);
  font-size: 0.9rem;
  font-weight: 700;
  cursor: pointer;
  padding: 0;
  letter-spacing: 0.03em;
  transition: all 0.2s;
}
.btn-read-more:hover { color: var(--amber); letter-spacing: 0.06em; }

/* Side list */
.stories-side { display: flex; flex-direction: column; gap: 1.3rem; }

/* Pagination dots */
.carousel-dots { display: flex; justify-content: center; gap: 0.5rem; margin-top: 2.5rem; }
.dot { width: 8px; height: 8px; border-radius: 50%; background: rgba(196, 127, 53, 0.25); transition: all 0.3s; }
.dot--active { background: var(--amber); width: 24px; border-radius: 4px; }

/* Loading skeletons */
.skeleton-featured {
  height: 500px;
  border-radius: 20px;
  background: linear-gradient(90deg, #EDD9AB 25%, #F7EDD8 50%, #EDD9AB 75%);
  background-size: 200% 100%;
  animation: skeletonWave 1.5s infinite;
}
.skeleton-story-card {
  height: 100px;
  border-radius: 14px;
  background: linear-gradient(90deg, #EDD9AB 25%, #F7EDD8 50%, #EDD9AB 75%);
  background-size: 200% 100%;
  animation: skeletonWave 1.5s infinite;
}
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

/* Responsive */
@media (max-width: 1024px) {
  .stories-layout { grid-template-columns: 1fr; }
  .stories-side { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
}
@media (max-width: 640px) {
  .stories-side { grid-template-columns: 1fr; }
}
</style>

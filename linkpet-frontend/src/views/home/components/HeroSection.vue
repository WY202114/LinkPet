<template>
  <section class="hero">
    <!-- Main content -->
    <div class="hero__content">
      <p class="hero__eyebrow">
        <span class="eyebrow-line"></span>
        A Home for Every Wandering Soul
        <span class="eyebrow-line"></span>
      </p>

      <h1 class="hero__title">
        A warm canvas for<br />
        <em>wandering souls</em>
      </h1>

      <p class="hero__subtitle">
        LinkPet bridges the gap between stray animals and loving hearts —
        browse adoptable pets, share your story, and help write a new chapter
        for a furry friend in need.
      </p>

      <div class="hero__cta-group">
        <button class="btn-cta">
          <span>Find Your Furry Friend</span>
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <path d="M5 12h14M12 5l7 7-7 7"/>
          </svg>
        </button>
        <button class="btn-ghost">Watch Our Story</button>
      </div>

      <!-- Stats bar -->
      <div class="hero__stats">
        <div class="stat-item">
          <span class="stat-item__num">{{ formatNum(stats.adoptionCount) }}</span>
          <span class="stat-item__label">温馨领养</span>
        </div>
        <div class="stat-sep"></div>
        <div class="stat-item">
          <span class="stat-item__num">{{ formatNum(stats.petWaitingCount) }}</span>
          <span class="stat-item__label">宠物等待中</span>
        </div>
        <div class="stat-sep"></div>
        <div class="stat-item">
          <span class="stat-item__num">{{ formatNum(stats.userCount) }}</span>
          <span class="stat-item__label">社区成员</span>
        </div>
      </div>
    </div>

  </section>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { getHomeStats } from '@/api/stats.js'

const stats = reactive({ adoptionCount: 0, petWaitingCount: 0, userCount: 0 })

/** 格式化数字：>=1000 显示为 "1K+"，否则原数 */
const formatNum = (n) => {
  if (!n) return '0'
  if (n >= 1000) return Math.floor(n / 1000) + 'K+'
  return n.toLocaleString()
}

onMounted(async () => {
  try {
    const data = await getHomeStats()
    if (data) Object.assign(stats, data)
  } catch { /* 保留默认 0 */ }
})
</script>

<style scoped>
.hero {
  position: relative;
  /* Removed min-height: calc(100vh-70px) to let it wrap content and reduce overall height */
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 3rem 2rem 1.5rem;
}

/* ── Hero content ── */
.hero__content {
  position: relative;
  z-index: 2;
  text-align: center;
  max-width: 760px;
}

.hero__eyebrow {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  font-size: 0.82rem;
  font-weight: 700;
  letter-spacing: 0.18em;
  text-transform: uppercase;
  color: var(--amber-dark);
  margin-bottom: 0.8rem;
  opacity: 0.85;
}
.eyebrow-line {
  flex: 0 0 40px;
  height: 1px;
  background: currentColor;
  opacity: 0.5;
}

.hero__title {
  font-family: var(--font-serif);
  font-size: clamp(2.6rem, 6vw, 4.4rem);
  font-weight: 700;
  line-height: 1.18;
  color: var(--brown);
  margin-bottom: 0.8rem;
  text-shadow: 3px 4px 12px rgba(61, 43, 31, 0.22);
}
.hero__title em {
  font-style: italic;
  color: var(--amber-dark);
}

.hero__subtitle {
  font-size: 1.05rem;
  line-height: 1.75;
  color: var(--brown-mid);
  max-width: 560px;
  margin: 0 auto 1rem;
  opacity: 0.9;
}

.hero__cta-group {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
  margin-bottom: 1rem;
}

/* ── Stats bar ── */
.hero__stats {
  display: inline-flex;
  align-items: center;
  gap: 1.5rem;
  padding: 0.8rem 2rem;
  background: rgba(247, 237, 216, 0.55);
  backdrop-filter: blur(14px);
  border-radius: 60px;
  border: 1px solid rgba(196, 127, 53, 0.25);
  box-shadow: 0 4px 16px var(--warm-shadow);
}
.stat-item { text-align: center; }
.stat-item__num {
  display: block;
  font-family: var(--font-serif);
  font-size: 1.3rem;
  font-weight: 700;
  color: var(--amber-dark);
}
.stat-item__label {
  font-size: 0.72rem;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: var(--brown-mid);
  opacity: 0.8;
}
.stat-sep {
  width: 1px;
  height: 28px;
  background: rgba(196, 127, 53, 0.3);
}

/* Responsive */
@media (max-width: 480px) {
  .hero__cta-group { flex-direction: column; align-items: center; }
  .hero__stats { flex-direction: column; gap: 1rem; padding: 1.2rem 2rem; }
  .stat-sep { width: 60px; height: 1px; }
}
</style>

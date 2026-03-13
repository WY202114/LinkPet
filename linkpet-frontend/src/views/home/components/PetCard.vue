<template>
  <div
    class="pet-card"
    @mouseenter="hovered = true"
    @mouseleave="hovered = false"
  >
    <!-- Image -->
    <div class="pet-card__img-wrap">
      <img
        :src="pet.image"
        :alt="pet.name"
        class="pet-card__img"
        :class="{ 'pet-card__img--hovered': hovered }"
      />
      <div class="pet-card__paint-overlay"></div>

      <span class="badge badge--type">{{ pet.type }}</span>
      <span v-if="pet.urgent" class="badge badge--urgent">Urgent</span>

      <button class="pet-card__fav" @click.stop="isFav = !isFav" :title="isFav ? 'Remove favourite' : 'Add to favourites'">
        {{ isFav ? '❤️' : '🤍' }}
      </button>
    </div>

    <!-- Body -->
    <div class="pet-card__body">
      <div class="pet-card__row">
        <h3 class="pet-card__name">{{ pet.name }}</h3>
        <span class="pet-card__age">{{ pet.age }}</span>
      </div>

      <p class="pet-card__location">
        <svg width="12" height="12" viewBox="0 0 24 24" fill="currentColor">
          <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/>
        </svg>
        {{ pet.location }}
      </p>

      <div class="pet-card__tags">
        <span v-for="tag in pet.tags" :key="tag" class="pet-tag">{{ tag }}</span>
      </div>

      <p class="pet-card__desc">{{ pet.desc }}</p>

      <button class="btn-details">
        View Details
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
          <path d="M5 12h14M12 5l7 7-7 7"/>
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

defineProps({
  pet: {
    type: Object,
    required: true,
  },
})

const hovered = ref(false)
const isFav   = ref(false)
</script>

<style scoped>
.pet-card {
  background: var(--cream);
  border-radius: 16px 16px 14px 14px;
  overflow: hidden;
  box-shadow: 0 6px 28px var(--warm-shadow), 0 2px 8px rgba(196, 127, 53, 0.08);
  transition: transform 0.35s ease, box-shadow 0.35s ease;
  border: 1px solid rgba(196, 127, 53, 0.12);
}
.pet-card:hover {
  transform: translateY(-8px) rotate(-0.3deg);
  box-shadow: 0 18px 50px var(--warm-shadow-lg), 0 4px 16px rgba(196, 127, 53, 0.15);
}

/* ── Image ── */
.pet-card__img-wrap {
  position: relative;
  height: 220px;
  overflow: hidden;
}
.pet-card__img {
  width: 100%; height: 100%;
  object-fit: cover;
  transition: transform 0.55s ease;
  /* Oil painting filter */
  filter: sepia(18%) saturate(115%) contrast(108%) brightness(96%) hue-rotate(5deg);
}
.pet-card__img--hovered { transform: scale(1.07); }

/* Warm vignette overlay */
.pet-card__paint-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    to bottom,
    rgba(196, 127, 53, 0.04) 0%,
    transparent 40%,
    rgba(61, 43, 31, 0.35) 100%
  );
  pointer-events: none;
}

/* Badges */
.badge {
  position: absolute;
  top: 12px;
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  padding: 0.28rem 0.75rem;
  border-radius: 20px;
}
.badge--type {
  left: 12px;
  background: rgba(247, 237, 216, 0.88);
  color: var(--amber-dark);
  border: 1px solid rgba(196, 127, 53, 0.3);
  backdrop-filter: blur(6px);
}
.badge--urgent {
  right: 12px;
  background: rgba(180, 60, 40, 0.88);
  color: #fff;
  backdrop-filter: blur(6px);
}

/* Favourite button */
.pet-card__fav {
  position: absolute;
  bottom: 12px; right: 12px;
  background: rgba(247, 237, 216, 0.8);
  border: none;
  border-radius: 50%;
  width: 32px; height: 32px;
  font-size: 1rem;
  cursor: pointer;
  backdrop-filter: blur(6px);
  transition: transform 0.2s;
  display: flex; align-items: center; justify-content: center;
}
.pet-card__fav:hover { transform: scale(1.2); }

/* ── Card body ── */
.pet-card__body { padding: 1.2rem 1.3rem 1.4rem; }

.pet-card__row {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  margin-bottom: 0.35rem;
}
.pet-card__name {
  font-family: var(--font-serif);
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--brown);
}
.pet-card__age {
  font-size: 0.8rem;
  color: var(--brown-mid);
  opacity: 0.7;
  background: rgba(196, 127, 53, 0.12);
  padding: 0.15rem 0.55rem;
  border-radius: 10px;
}

.pet-card__location {
  display: flex;
  align-items: center;
  gap: 0.3rem;
  font-size: 0.78rem;
  color: var(--green-vintage);
  margin-bottom: 0.75rem;
  opacity: 0.85;
}

.pet-card__tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.4rem;
  margin-bottom: 0.85rem;
}
.pet-tag {
  font-size: 0.72rem;
  padding: 0.22rem 0.65rem;
  border-radius: 12px;
  background: rgba(74, 103, 65, 0.12);
  color: var(--green-vintage);
  border: 1px solid rgba(74, 103, 65, 0.2);
  font-weight: 700;
  letter-spacing: 0.04em;
}

.pet-card__desc {
  font-size: 0.85rem;
  line-height: 1.65;
  color: var(--brown-mid);
  margin-bottom: 1.1rem;
  opacity: 0.88;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.btn-details {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.5rem 1.2rem;
  font-family: var(--font-sans);
  font-size: 0.85rem;
  font-weight: 700;
  border: 1.5px solid var(--amber);
  background: transparent;
  color: var(--amber-dark);
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.25s;
  letter-spacing: 0.03em;
}
.btn-details:hover {
  background: var(--amber);
  color: white;
  transform: translateX(2px);
}
</style>

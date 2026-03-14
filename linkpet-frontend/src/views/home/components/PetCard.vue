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

    <!-- Name only -->
    <div class="pet-card__body">
      <h3 class="pet-card__name">{{ pet.name }}</h3>
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
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 16px var(--warm-shadow), 0 1px 4px rgba(196, 127, 53, 0.08);
  transition: transform 0.35s ease, box-shadow 0.35s ease;
  border: 1px solid rgba(196, 127, 53, 0.12);
  cursor: pointer;
}
.pet-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 32px var(--warm-shadow-lg), 0 3px 10px rgba(196, 127, 53, 0.15);
}

/* ── Image ── */
.pet-card__img-wrap {
  position: relative;
  height: 140px;
  overflow: hidden;
}
.pet-card__img {
  width: 100%; height: 100%;
  object-fit: cover;
  transition: transform 0.55s ease;
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
  top: 8px;
  font-size: 0.6rem;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  padding: 0.2rem 0.55rem;
  border-radius: 16px;
}
.badge--type {
  left: 8px;
  background: rgba(247, 237, 216, 0.88);
  color: var(--amber-dark);
  border: 1px solid rgba(196, 127, 53, 0.3);
  backdrop-filter: blur(6px);
}
.badge--urgent {
  right: 8px;
  background: rgba(180, 60, 40, 0.88);
  color: #fff;
  backdrop-filter: blur(6px);
}

/* Favourite button */
.pet-card__fav {
  position: absolute;
  bottom: 8px; right: 8px;
  background: rgba(247, 237, 216, 0.8);
  border: none;
  border-radius: 50%;
  width: 26px; height: 26px;
  font-size: 0.8rem;
  cursor: pointer;
  backdrop-filter: blur(6px);
  transition: transform 0.2s;
  display: flex; align-items: center; justify-content: center;
}
.pet-card__fav:hover { transform: scale(1.2); }

/* ── Card body ── */
.pet-card__body {
  padding: 0.5rem 0.75rem;
  text-align: center;
}

.pet-card__name {
  font-family: var(--font-serif);
  font-size: 0.95rem;
  font-weight: 700;
  color: var(--brown);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>

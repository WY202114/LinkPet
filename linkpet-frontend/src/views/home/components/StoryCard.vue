<template>
  <div class="story-card">
    <!-- Thumbnail -->
    <div class="story-card__img-wrap">
      <img :src="story.image" :alt="story.title" class="story-card__img" />
      <div class="story-card__paint-overlay"></div>
      <span class="story-tag story-tag--sm">{{ story.tag }}</span>
    </div>

    <!-- Content -->
    <div class="story-card__body">
      <h4 class="story-card__title">{{ story.title }}</h4>
      <p class="story-card__excerpt">{{ story.excerpt }}</p>
      <div class="story-card__footer">
        <div class="story-meta">
          <img :src="story.avatar" :alt="story.author" class="story-meta__avatar" />
          <span class="story-meta__author">{{ story.author }}</span>
        </div>
        <div class="story-card__stats">
          <span>❤️ {{ story.likes }}</span>
          <span>💬 {{ story.comments }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  story: {
    type: Object,
    required: true,
  },
})
</script>

<style scoped>
.story-card {
  background: var(--cream);
  border-radius: 14px;
  overflow: hidden;
  display: grid;
  grid-template-columns: 120px 1fr;
  box-shadow: 0 4px 20px var(--warm-shadow);
  border: 1px solid rgba(196, 127, 53, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.story-card:hover {
  transform: translateX(4px);
  box-shadow: 0 8px 30px var(--warm-shadow-lg);
}

/* Image */
.story-card__img-wrap {
  position: relative;
  height: 100%;
  min-height: 100px;
  overflow: hidden;
}
.story-card__img {
  width: 100%; height: 100%;
  object-fit: cover;
  filter: sepia(20%) saturate(110%) contrast(106%) brightness(95%);
  transition: transform 0.45s;
}
.story-card:hover .story-card__img { transform: scale(1.08); }
.story-card__paint-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to right, transparent 60%, rgba(247, 237, 216, 0.3));
}

/* Body */
.story-card__body {
  padding: 0.9rem 1rem;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.story-card__title {
  font-family: var(--font-serif);
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--brown);
  line-height: 1.35;
  margin-bottom: 0.4rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.story-card__excerpt {
  font-size: 0.78rem;
  line-height: 1.6;
  color: var(--brown-mid);
  opacity: 0.8;
  margin-bottom: 0.5rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.story-card__footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.5rem;
}
.story-meta {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
.story-meta__avatar {
  width: 28px; height: 28px;
  border-radius: 50%;
  object-fit: cover;
  border: 1.5px solid rgba(196, 127, 53, 0.3);
  filter: sepia(10%) saturate(110%);
}
.story-meta__author {
  font-size: 0.75rem;
  font-weight: 700;
  color: var(--brown);
}
.story-card__stats {
  display: flex;
  gap: 0.7rem;
  font-size: 0.72rem;
  color: var(--brown-mid);
  opacity: 0.75;
}
</style>

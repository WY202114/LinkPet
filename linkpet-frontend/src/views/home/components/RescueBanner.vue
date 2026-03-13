<template>
  <section class="rescue-banner" id="guide">
    <div class="rescue-banner__paint" aria-hidden="true"></div>
    <div class="rescue-banner__grain"  aria-hidden="true"></div>

    <!-- Floating emojis (decorative) -->
    <div class="rescue-banner__deco" aria-hidden="true">
      <span class="deco-emoji deco-emoji--1">🐕</span>
      <span class="deco-emoji deco-emoji--2">🐈</span>
      <span class="deco-emoji deco-emoji--3">🏠</span>
      <span class="deco-emoji deco-emoji--4">🐾</span>
    </div>

    <!-- Content -->
    <div class="rescue-banner__content">
      <p class="section__tag section__tag--light">Not ready to adopt?</p>
      <h2 class="rescue-banner__title">You Can Still Make a Difference</h2>
      <p class="rescue-banner__desc">
        Learn how to safely rescue a stray, provide first aid, find local shelters,
        and support the animals in your neighbourhood.
      </p>
      <div class="rescue-banner__actions">
        <button class="btn-cta btn-cta--light">Read the Rescue Guide</button>
        <button class="btn-ghost btn-ghost--light">Volunteer with Us</button>
      </div>
    </div>
  </section>
</template>

<script setup>
// RescueBanner is a static promotional section — no reactive state needed
</script>

<style scoped>
.rescue-banner {
  position: relative;
  padding: 6rem 2rem;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

/* Background */
.rescue-banner__paint {
  position: absolute; inset: 0;
  background: linear-gradient(135deg, #3D2B1F 0%, #5C3D2E 40%, #4A6741 100%);
}
.rescue-banner__grain {
  position: absolute; inset: 0;
  opacity: 0.6;
  pointer-events: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='200' height='200'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.7' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='200' height='200' filter='url(%23n)' opacity='0.07'/%3E%3C/svg%3E");
}

/* Content */
.rescue-banner__content {
  position: relative;
  z-index: 2;
  max-width: 600px;
}
.rescue-banner__title {
  font-family: var(--font-serif);
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 700;
  color: var(--cream);
  margin-bottom: 1rem;
  line-height: 1.2;
}
.rescue-banner__desc {
  font-size: 1rem;
  line-height: 1.75;
  color: var(--cream-dark);
  margin-bottom: 2.2rem;
  opacity: 0.88;
}
.rescue-banner__actions {
  display: flex;
  justify-content: center;
  gap: 1rem;
  flex-wrap: wrap;
}

/* Floating decorations */
.rescue-banner__deco {
  position: absolute;
  inset: 0;
  pointer-events: none;
  z-index: 1;
}
.deco-emoji {
  position: absolute;
  font-size: 3rem;
  opacity: 0.12;
  animation: decoFloat 14s ease-in-out infinite;
}
.deco-emoji--1 { top: 15%; left: 8%;   font-size: 4rem; animation-delay: 0s; }
.deco-emoji--2 { top: 20%; right: 10%; font-size: 3.5rem; animation-delay: 4s; }
.deco-emoji--3 { bottom: 15%; left: 15%;              animation-delay: 8s; }
.deco-emoji--4 { bottom: 20%; right: 8%; font-size: 2.5rem; animation-delay: 2s; }

@keyframes decoFloat {
  0%, 100% { transform: translateY(0) rotate(0deg); opacity: 0.12; }
  50%       { transform: translateY(-18px) rotate(8deg); opacity: 0.18; }
}

/* Responsive */
@media (max-width: 768px) {
  .rescue-banner__deco { display: none; }
}
</style>

<template>
  <section class="hero">
    <!-- Painterly layered background -->
    <div class="hero__canvas"></div>
    <div class="hero__paint-blobs" aria-hidden="true">
      <div class="blob blob--1"></div>
      <div class="blob blob--2"></div>
      <div class="blob blob--3"></div>
      <div class="blob blob--4"></div>
    </div>
    <!-- Canvas grain -->
    <div class="hero__grain" aria-hidden="true"></div>
    <!-- Floating paw decorations -->
    <div class="hero__paws" aria-hidden="true">
      <span class="paw paw--1">🐾</span>
      <span class="paw paw--2">🐾</span>
      <span class="paw paw--3">🐾</span>
    </div>

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
          <span class="stat-item__num">2,400+</span>
          <span class="stat-item__label">Happy Adoptions</span>
        </div>
        <div class="stat-sep"></div>
        <div class="stat-item">
          <span class="stat-item__num">820</span>
          <span class="stat-item__label">Pets Waiting</span>
        </div>
        <div class="stat-sep"></div>
        <div class="stat-item">
          <span class="stat-item__num">12K+</span>
          <span class="stat-item__label">Community Members</span>
        </div>
      </div>
    </div>

  </section>
</template>

<script setup>
// HeroSection has no external dependencies — fully self-contained
</script>

<style scoped>
.hero {
  position: relative;
  height: calc(100vh - 70px);
  max-height: calc(100vh - 70px);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  padding: 1rem 2rem;
}

/* ── Painterly background ── */
.hero__canvas {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse 90% 80% at 20% 30%, #E8C88A 0%, transparent 60%),
    radial-gradient(ellipse 70% 60% at 80% 70%, #C4A06A 0%, transparent 50%),
    radial-gradient(ellipse 60% 70% at 60% 20%, #A8C49A 0%, transparent 50%),
    linear-gradient(160deg, #DEB87A 0%, #C9965A 35%, #8FA878 65%, #C4A455 100%);
}

/* ── Animated paint blobs ── */
.hero__paint-blobs { position: absolute; inset: 0; pointer-events: none; }
.blob {
  position: absolute;
  border-radius: 60% 40% 70% 30% / 50% 60% 40% 50%;
  filter: blur(60px);
  opacity: 0.55;
  animation: blobFloat 12s ease-in-out infinite;
}
.blob--1 { width: 500px; height: 400px; top: -10%; left: -15%; background: #E2A84A; animation-duration: 12s; }
.blob--2 { width: 350px; height: 350px; top: 30%; right: -8%; background: #7A9E72; animation-duration: 15s; animation-direction: reverse; }
.blob--3 { width: 280px; height: 280px; bottom: 10%; left: 20%; background: #C47F35; opacity: 0.35; animation-duration: 10s; animation-delay: 3s; }
.blob--4 { width: 200px; height: 250px; top: 60%; right: 25%; background: #D4B896; opacity: 0.4; animation-duration: 8s; animation-delay: 1s; }

@keyframes blobFloat {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33%       { transform: translate(20px, -25px) scale(1.05); }
  66%       { transform: translate(-15px, 15px) scale(0.97); }
}

/* ── Grain overlay ── */
.hero__grain {
  position: absolute;
  inset: 0;
  opacity: 0.09;
  pointer-events: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='250' height='250'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='250' height='250' filter='url(%23n)'/%3E%3C/svg%3E");
}

/* ── Floating paw prints ── */
.hero__paws { position: absolute; inset: 0; pointer-events: none; }
.paw {
  position: absolute;
  font-size: 2rem;
  opacity: 0.18;
  animation: pawDrift 20s ease-in-out infinite;
}
.paw--1 { top: 15%; right: 12%; font-size: 2.5rem; animation-delay: 0s; }
.paw--2 { top: 65%; left: 5%;  font-size: 1.5rem; animation-delay: 7s; }
.paw--3 { bottom: 20%; right: 35%;               animation-delay: 14s; }

@keyframes pawDrift {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50%       { transform: translateY(-20px) rotate(15deg); }
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
  margin: 0 auto 1.5rem;
  opacity: 0.9;
}

.hero__cta-group {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
  margin-bottom: 1.5rem;
}

/* ── Stats bar ── */
.hero__stats {
  display: inline-flex;
  align-items: center;
  gap: 2rem;
  padding: 1.2rem 2.5rem;
  background: rgba(247, 237, 216, 0.55);
  backdrop-filter: blur(14px);
  border-radius: 60px;
  border: 1px solid rgba(196, 127, 53, 0.25);
  box-shadow: 0 8px 32px var(--warm-shadow);
}
.stat-item { text-align: center; }
.stat-item__num {
  display: block;
  font-family: var(--font-serif);
  font-size: 1.5rem;
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
  height: 36px;
  background: rgba(196, 127, 53, 0.3);
}

/* Responsive */
@media (max-width: 480px) {
  .hero__cta-group { flex-direction: column; align-items: center; }
  .hero__stats { flex-direction: column; gap: 1rem; padding: 1.2rem 2rem; }
  .stat-sep { width: 60px; height: 1px; }
}
</style>

<template>
  <!-- Global decorative background -->
  <div class="global-bg" aria-hidden="true">
    <div class="global-bg__halo"></div>
    <div class="global-bg__paint-blobs">
      <div class="blob blob--1"></div>
      <div class="blob blob--2"></div>
      <div class="blob blob--3"></div>
    </div>
    <div class="global-bg__grain"></div>
    <div class="global-bg__paws">
      <span class="paw paw--1">🐾</span>
      <span class="paw paw--2">🐾</span>
      <span class="paw paw--3">🐾</span>
      <span class="paw paw--4">🐾</span>
      <span class="paw paw--5">🐾</span>
    </div>
  </div>

  <AppNavbar />
  <main class="page-main" :class="{ 'page-main--no-scroll': noScroll }">
    <RouterView />
  </main>
  <AppFooter v-if="showFooter" />
</template>

<script setup>
import { computed } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import AppNavbar from '@/views/home/components/AppNavbar.vue'
import AppFooter from '@/views/home/components/AppFooter.vue'

const route = useRoute()
const showFooter = computed(() => route.path === '/guide')
const noScroll = computed(() => route.path === '/')
</script>

<style scoped>
.page-main {
  min-height: 100vh;
  padding-top: 70px;
}
.page-main--no-scroll {
  height: 100vh;
  min-height: auto;
  overflow: hidden;
}

/* ── Global Background (Warm Healing Theme) ── */
.global-bg {
  position: fixed;
  inset: 0;
  z-index: -1;
  /* Left beige & light orange, right soft light green */
  background: linear-gradient(120deg, #FDF7E3 0%, #F5D0A9 45%, #D5E5C9 100%);
  overflow: hidden;
}

.global-bg__halo {
  position: absolute;
  inset: 0;
  background: 
    radial-gradient(ellipse at 15% 25%, rgba(245, 208, 169, 0.75) 0%, transparent 55%),
    radial-gradient(ellipse at 85% 75%, rgba(213, 229, 201, 0.8) 0%, transparent 60%);
  filter: blur(60px);
}

/* ── Animated paint blobs ── */
.global-bg__paint-blobs { position: absolute; inset: 0; pointer-events: none; }
.blob {
  position: absolute;
  border-radius: 60% 40% 70% 30% / 50% 60% 40% 50%;
  filter: blur(80px);
  opacity: 0.65;
  animation: blobFloat 15s ease-in-out infinite;
}
.blob--1 { width: 45vw; height: 45vw; top: -10%; left: -10%; background: #FDE8C4; animation-duration: 14s; }
.blob--2 { width: 40vw; height: 40vw; bottom: -10%; right: -5%; background: #E1E8D5; animation-duration: 18s; animation-direction: reverse; }
.blob--3 { width: 30vw; height: 30vw; top: 35%; left: 30%; background: #F5D0A9; opacity: 0.45; animation-duration: 12s; animation-delay: 2s; }

@keyframes blobFloat {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33%       { transform: translate(30px, -40px) scale(1.05); }
  66%       { transform: translate(-20px, 20px) scale(0.95); }
}

/* ── Grain overlay ── */
.global-bg__grain {
  position: absolute;
  inset: 0;
  opacity: 0.04;
  pointer-events: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='250' height='250'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.6' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='250' height='250' filter='url(%23n)'/%3E%3C/svg%3E");
}

/* ── Very faint floating paw prints ── */
.global-bg__paws { position: absolute; inset: 0; pointer-events: none; }
.paw {
  position: absolute;
  font-size: 2rem;
  opacity: 0.05; /* Very subtle and faint */
  color: #8B5B1C;
  animation: pawDrift 20s ease-in-out infinite;
}
.paw--1 { top: 18%; right: 12%; font-size: 2.2rem; animation-delay: 0s; }
.paw--2 { top: 70%; left: 8%;  font-size: 1.6rem; animation-delay: 4s; }
.paw--3 { top: 40%; right: 35%; font-size: 1.4rem; animation-delay: 10s; }
.paw--4 { top: 85%; right: 20%; font-size: 1.9rem; animation-delay: 7s; }
.paw--5 { top: 25%; left: 18%; font-size: 1.5rem; animation-delay: 14s; }

@keyframes pawDrift {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50%       { transform: translateY(-20px) rotate(12deg); }
}
</style>

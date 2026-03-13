<template>
  <div class="home-page">
    <AppNavbar />
    <HeroSection />
    <AdoptionSection />
    <CommunitySection />
    <RescueBanner />
    <AppFooter />
  </div>
</template>

<script setup>
import AppNavbar       from './components/AppNavbar.vue'
import HeroSection     from './components/HeroSection.vue'
import AdoptionSection from './components/AdoptionSection.vue'
import CommunitySection from './components/CommunitySection.vue'
import RescueBanner    from './components/RescueBanner.vue'
import AppFooter       from './components/AppFooter.vue'
</script>

<template>
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
  min-height: calc(100vh - 70px);
  padding-top: 70px;
}
.page-main--no-scroll {
  height: calc(100vh - 70px);
  max-height: calc(100vh - 70px);
  overflow: hidden;
}
</style>

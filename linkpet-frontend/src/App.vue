<template>
  <AppNavbar />
  <main class="page-main">
    <RouterView />
  </main>
  <AppFooter />
</template>

<script setup>
import { RouterView } from 'vue-router'
import AppNavbar from '@/views/home/components/AppNavbar.vue'
import AppFooter from '@/views/home/components/AppFooter.vue'
</script>

<style scoped>
.page-main {
  min-height: calc(100vh - 70px);
  padding-top: 70px;
}
</style>

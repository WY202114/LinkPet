import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/home/index.vue'),
    meta: { title: 'LinkPet — 首页' },
  },
  // 后续可添加更多路由，例如：
  // { path: '/adoption', name: 'Adoption', component: () => import('@/views/adoption/index.vue') },
  // { path: '/community', name: 'Community', component: () => import('@/views/community/index.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) return savedPosition
    if (to.hash) return { el: to.hash, behavior: 'smooth' }
    return { top: 0 }
  },
})

router.afterEach((to) => {
  if (to.meta?.title) document.title = to.meta.title
})

export default router

import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/home/index.vue'),
    meta: { title: 'LinkPet — 首页' },
  },
  {
    path: '/adoption',
    name: 'Adoption',
    component: () => import('@/views/adoption/index.vue'),
    meta: { title: 'LinkPet — 领养中心' },
  },
  {
    path: '/community',
    name: 'Community',
    component: () => import('@/views/community/index.vue'),
    meta: { title: 'LinkPet — 宠物社区' },
  },
  {
    path: '/guide',
    name: 'Guide',
    component: () => import('@/views/guide/index.vue'),
    meta: { title: 'LinkPet — 救助指南' },
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/profile/index.vue'),
    meta: { title: 'LinkPet — 个人中心' },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  },
})

router.afterEach((to) => {
  if (to.meta?.title) document.title = to.meta.title
})

export default router

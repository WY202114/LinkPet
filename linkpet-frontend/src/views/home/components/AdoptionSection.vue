<template>
  <section class="section adoption-section">
    <!-- Section header -->
    <div class="section__header">
      <p class="section__tag">Ready to find a home</p>
      <h2 class="section__title">Furry Friends Waiting for You</h2>
      <p class="section__desc">
        Each one has a story. Each one deserves a loving chapter.
        Could you be the one to write it?
      </p>
    </div>

    <!-- Filter tabs (built from /pet-types API) -->
    <div class="filter-tabs">
      <button
        v-for="tab in filterTabs"
        :key="tab.value"
        class="filter-tab"
        :class="{ 'filter-tab--active': activeFilter.value === tab.value }"
        @click="setFilter(tab)"
      >
        {{ tab.label }}
      </button>
    </div>

    <!-- Loading skeleton -->
    <div v-if="loading" class="pets-grid">
      <div v-for="n in 4" :key="n" class="skeleton-card">
        <div class="skeleton-img"></div>
        <div class="skeleton-body">
          <div class="skeleton-line skeleton-line--wide"></div>
          <div class="skeleton-line"></div>
          <div class="skeleton-line skeleton-line--narrow"></div>
        </div>
      </div>
    </div>

    <!-- Error state -->
    <div v-else-if="error" class="state-msg state-msg--error">
      <p>⚠️ {{ error }}</p>
      <button class="btn-retry" @click="fetchPets">Retry</button>
    </div>

    <!-- Empty state -->
    <div v-else-if="petList.length === 0" class="state-msg">
      <p>🐾 No pets found for this category yet.</p>
    </div>

    <!-- Pet cards grid -->
    <div v-else class="pets-grid">
      <PetCard v-for="pet in petList" :key="pet.id" :pet="pet" />
    </div>

    <!-- Pagination info -->
    <p v-if="total > pageSize && !loading && !error" class="pagination-hint">
      Showing {{ petList.length }} of {{ total }} pets
    </p>

  </section>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import PetCard from './PetCard.vue'
import { getPets } from '@/api/pets.js'
import { getPetTypes } from '@/api/petTypes.js'
import { formatPet } from '@/utils/format.js'
import { pets as mockPets, filterTabs as mockTabs } from '@/data/mockData.js'

// ── State ─────────────────────────────────────────────────────
const petList    = ref([])
const loading    = ref(false)
const error      = ref('')
const total      = ref(0)
const pageSize   = 50

// activeFilter holds { value: 'all' | typeId, typeId: number | null }
const activeFilter = reactive({ value: 'all', typeId: null })

// Filter tabs — starts with "All", extended after /pet-types loads
const filterTabs = ref([{ label: '🐾 All Pets', value: 'all', typeId: null }])

// ── Load pet types → build filter tabs ───────────────────────
const loadPetTypes = async () => {
  try {
    const types = await getPetTypes()
    if (Array.isArray(types) && types.length) {
      const icons = { cat: '🐱', dog: '🐶', rabbit: '🐰', bird: '🐦' }
      const extra = types.map(t => ({
        label:  `${icons[t.name?.toLowerCase()] ?? '🐾'} ${t.name}`,
        value:  t.id,
        typeId: t.id,
      }))
      filterTabs.value = [{ label: '🐾 All Pets', value: 'all', typeId: null }, ...extra]
    }
  } catch {
    // Keep default tabs if API fails
    filterTabs.value = mockTabs
  }
}

// ── Fetch pets ────────────────────────────────────────────────
const fetchPets = async () => {
  loading.value = true
  error.value   = ''
  try {
    const params = { page: 1, pageSize }
    if (activeFilter.typeId) params.typeId = activeFilter.typeId

    const result = await getPets(params)
    petList.value = result.records.map((p, i) => formatPet(p, i))
    total.value   = result.total
  } catch (e) {
    error.value = e.message || 'Failed to load pets. Showing sample data.'
    // Graceful fallback to mock data
    petList.value = mockPets
    total.value   = mockPets.length
  } finally {
    loading.value = false
  }
}

// ── Filter change ─────────────────────────────────────────────
const setFilter = (tab) => {
  activeFilter.value  = tab.value
  activeFilter.typeId = tab.typeId ?? null
  fetchPets()
}

// ── Init ──────────────────────────────────────────────────────
onMounted(async () => {
  await loadPetTypes()
  await fetchPets()
})
</script>

<style scoped>
.adoption-section {
  background: var(--warm-white);
}
.adoption-section::after {
  content: '';
  position: absolute;
  inset: 0;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='400' height='400'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.5' numOctaves='2' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='400' height='400' filter='url(%23n)' opacity='0.03'/%3E%3C/svg%3E");
  pointer-events: none;
  opacity: 0.5;
}

/* Filter tabs */
.filter-tabs {
  display: flex;
  justify-content: center;
  gap: 0.7rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
}
.filter-tab {
  padding: 0.5rem 1.4rem;
  font-family: var(--font-sans);
  font-size: 0.88rem;
  border: 1.5px solid rgba(196, 127, 53, 0.3);
  background: transparent;
  color: var(--brown-mid);
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.25s;
}
.filter-tab:hover   { border-color: var(--amber); color: var(--amber-dark); }
.filter-tab--active { background: var(--amber); border-color: var(--amber); color: white; font-weight: 700; }

/* Pet cards grid */
.pets-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1rem;
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

/* Pagination hint */
.pagination-hint {
  text-align: center;
  margin-top: 1.5rem;
  font-size: 0.82rem;
  color: var(--brown-mid);
  opacity: 0.6;
}

/* Loading skeleton */
.skeleton-card {
  background: var(--cream);
  border-radius: 16px;
  overflow: hidden;
  border: 1px solid rgba(196, 127, 53, 0.1);
  animation: shimmer 1.5s infinite;
}
.skeleton-img  { height: 220px; background: linear-gradient(90deg, #EDD9AB 25%, #F7EDD8 50%, #EDD9AB 75%); background-size: 200% 100%; animation: skeletonWave 1.5s infinite; }
.skeleton-body { padding: 1.2rem 1.3rem 1.4rem; display: flex; flex-direction: column; gap: 0.7rem; }
.skeleton-line { height: 12px; border-radius: 6px; background: linear-gradient(90deg, #EDD9AB 25%, #F7EDD8 50%, #EDD9AB 75%); background-size: 200% 100%; animation: skeletonWave 1.5s infinite; }
.skeleton-line--wide   { width: 70%; }
.skeleton-line--narrow { width: 40%; }

@keyframes skeletonWave {
  0%   { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

/* State messages */
.state-msg {
  text-align: center;
  padding: 4rem 2rem;
  color: var(--brown-mid);
  font-size: 1rem;
  opacity: 0.7;
}
.state-msg--error { color: #b91c1c; opacity: 1; }
.btn-retry {
  margin-top: 1rem;
  padding: 0.5rem 1.5rem;
  background: var(--amber);
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-family: var(--font-sans);
  font-weight: 700;
  transition: background 0.2s;
}
.btn-retry:hover { background: var(--amber-dark); }

@media (max-width: 768px) {
  .pets-grid { grid-template-columns: 1fr; max-width: 420px; margin: 0 auto; }
}
</style>

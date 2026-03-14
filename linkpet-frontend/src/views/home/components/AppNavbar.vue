<template>
  <nav class="navbar" :class="{ 'navbar--scrolled': scrolled }">
    <div class="nav-inner">
      <!-- Logo -->
      <div class="nav-logo">
        <span class="nav-logo__paw">🐾</span>
        <span class="nav-logo__text">LinkPet</span>
      </div>

      <!-- Desktop links -->
      <div class="nav-links">
        <router-link to="/"          class="nav-link" active-class="nav-link--active" exact>Home</router-link>
        <router-link to="/adoption"  class="nav-link" active-class="nav-link--active">Adoption Center</router-link>
        <router-link to="/community" class="nav-link" active-class="nav-link--active">Pet Community</router-link>
        <router-link to="/guide"     class="nav-link" active-class="nav-link--active">Rescue Guide</router-link>
      </div>

      <!-- Auth area -->
      <div class="nav-auth">
        <!-- Not logged in -->
        <template v-if="!isLoggedIn">
          <button class="btn-login"    @click="openModal('login')">Login</button>
          <button class="btn-register" @click="openModal('register')">Register</button>
        </template>
        <!-- Logged in -->
        <template v-else>
          <div class="user-info" @click="userMenuOpen = !userMenuOpen">
            <img :src="avatarUrl" :alt="userStore.state.userInfo?.nickname" class="user-avatar" />
            <span class="user-name">{{ userStore.state.userInfo?.nickname }}</span>
            <svg width="12" height="12" viewBox="0 0 24 24" fill="currentColor"><path d="M7 10l5 5 5-5z"/></svg>
          </div>
          <!-- User dropdown -->
          <Transition name="fade">
            <div v-if="userMenuOpen" class="user-dropdown">
              <p class="user-dropdown__name">{{ userStore.state.userInfo?.username }}</p>
              <router-link to="/profile" class="user-dropdown__item" @click="userMenuOpen = false">My Profile</router-link>
              <button class="user-dropdown__logout" @click="handleLogout">Log Out</button>
            </div>
          </Transition>
        </template>
      </div>

      <!-- Mobile hamburger -->
      <button class="nav-hamburger" :class="{ open: mobileOpen }" @click="mobileOpen = !mobileOpen" aria-label="Toggle menu">
        <span></span><span></span><span></span>
      </button>
    </div>

    <!-- Mobile menu -->
    <Transition name="slide-down">
      <div v-if="mobileOpen" class="nav-mobile">
        <router-link to="/"          class="nav-mobile__link" @click="mobileOpen = false">Home</router-link>
        <router-link to="/adoption"  class="nav-mobile__link" @click="mobileOpen = false">Adoption Center</router-link>
        <router-link to="/community" class="nav-mobile__link" @click="mobileOpen = false">Pet Community</router-link>
        <router-link to="/guide"     class="nav-mobile__link" @click="mobileOpen = false">Rescue Guide</router-link>
        <div class="nav-mobile__auth">
          <template v-if="!isLoggedIn">
            <button class="btn-login"    @click="openModal('login');    mobileOpen = false">Login</button>
            <button class="btn-register" @click="openModal('register'); mobileOpen = false">Register</button>
          </template>
          <template v-else>
            <span class="mobile-user">Hi, {{ userStore.state.userInfo?.nickname }}</span>
            <router-link to="/profile" class="btn-login" style="text-decoration:none;text-align:center" @click="mobileOpen = false">My Profile</router-link>
            <button class="btn-login" @click="handleLogout">Log Out</button>
          </template>
        </div>
      </div>
    </Transition>
  </nav>

  <!-- ======================================================
       LOGIN / REGISTER MODAL
  ====================================================== -->
  <Teleport to="body">
    <Transition name="modal">
      <div v-if="modalVisible" class="modal-overlay" @click.self="closeModal">
        <div class="modal-card">
          <button class="modal-close" @click="closeModal" aria-label="Close">✕</button>

          <div class="modal-header">
            <span class="modal-logo">🐾</span>
            <h2 class="modal-title">{{ modalMode === 'login' ? 'Welcome Back' : 'Join LinkPet' }}</h2>
            <p class="modal-subtitle">{{ modalMode === 'login' ? 'Login to connect with furry friends.' : 'Create your account today.' }}</p>
          </div>

          <p v-if="authError" class="modal-error">{{ authError }}</p>

          <!-- Login Form -->
          <form v-if="modalMode === 'login'" class="modal-form" @submit.prevent="handleLogin">
            <div class="form-group">
              <label>Username</label>
              <input v-model="loginForm.username" type="text" placeholder="Enter your username" required autocomplete="username" />
            </div>
            <div class="form-group">
              <label>Password</label>
              <input v-model="loginForm.password" type="password" placeholder="Enter your password" required autocomplete="current-password" />
            </div>
            <button type="submit" class="modal-submit" :disabled="authLoading">
              <span v-if="!authLoading">Login</span>
              <span v-else class="loading-dot">●●●</span>
            </button>
          </form>

          <!-- Register Form -->
          <form v-else class="modal-form" @submit.prevent="handleRegister">
            <div class="form-group">
              <label>Username <span class="hint">(4–20 chars, letters / numbers / _)</span></label>
              <input v-model="regForm.username" type="text" placeholder="Choose a username" required />
            </div>
            <div class="form-group">
              <label>Nickname</label>
              <input v-model="regForm.nickname" type="text" placeholder="Display name (max 20 chars)" required />
            </div>
            <div class="form-group">
              <label>Password <span class="hint">(min 8 chars)</span></label>
              <input v-model="regForm.password" type="password" placeholder="Create a password" required autocomplete="new-password" />
            </div>
            <div class="form-group">
              <label>Phone <span class="hint">(optional)</span></label>
              <input v-model="regForm.phone" type="tel" placeholder="13800138000" />
            </div>
            <button type="submit" class="modal-submit" :disabled="authLoading">
              <span v-if="!authLoading">Create Account</span>
              <span v-else class="loading-dot">●●●</span>
            </button>
          </form>

          <p class="modal-switch">
            <template v-if="modalMode === 'login'">
              New to LinkPet?
              <button class="modal-switch__btn" @click="switchMode('register')">Sign Up Free</button>
            </template>
            <template v-else>
              Already have an account?
              <button class="modal-switch__btn" @click="switchMode('login')">Login</button>
            </template>
          </p>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { login, register } from '@/api/auth.js'
import { useUserStore } from '@/store/user.js'

// ── Scroll & mobile ───────────────────────────────────────────
const scrolled     = ref(false)
const mobileOpen   = ref(false)
const userMenuOpen = ref(false)

const handleScroll = () => { scrolled.value = window.scrollY > 60 }
onMounted(()   => window.addEventListener('scroll', handleScroll))
onUnmounted(() => window.removeEventListener('scroll', handleScroll))

// Close user dropdown on outside click
const closeUserMenu = (e) => {
  if (!e.target.closest('.user-info')) userMenuOpen.value = false
}
onMounted(()   => document.addEventListener('click', closeUserMenu))
onUnmounted(() => document.removeEventListener('click', closeUserMenu))

// ── User store ────────────────────────────────────────────────
const userStore  = useUserStore()
const isLoggedIn = userStore.isLoggedIn
const avatarUrl  = userStore.avatarUrl

const handleLogout = () => {
  userStore.logout()
  userMenuOpen.value = false
  mobileOpen.value   = false
}

// ── Auth modal ────────────────────────────────────────────────
const modalVisible = ref(false)
const modalMode    = ref('login')
const authLoading  = ref(false)
const authError    = ref('')

const loginForm = ref({ username: '', password: '' })
const regForm   = ref({ username: '', nickname: '', password: '', phone: '' })

const openModal  = (mode) => { modalMode.value = mode; authError.value = ''; modalVisible.value = true }
const closeModal = () => { modalVisible.value = false; authError.value = '' }
const switchMode = (mode) => { modalMode.value = mode; authError.value = '' }

const handleLogin = async () => {
  authError.value   = ''
  authLoading.value = true
  try {
    const data = await login(loginForm.value)
    userStore.setUser(data)
    closeModal()
    loginForm.value = { username: '', password: '' }
  } catch (e) {
    authError.value = e.message || 'Login failed. Please check your credentials.'
  } finally {
    authLoading.value = false
  }
}

const handleRegister = async () => {
  authError.value   = ''
  authLoading.value = true
  try {
    await register(regForm.value)
    // Auto-login after registration
    const data = await login({ username: regForm.value.username, password: regForm.value.password })
    userStore.setUser(data)
    closeModal()
    regForm.value = { username: '', nickname: '', password: '', phone: '' }
  } catch (e) {
    authError.value = e.message || 'Registration failed. Please try again.'
  } finally {
    authLoading.value = false
  }
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0; left: 0; right: 0;
  z-index: 100;
  transition: all 0.35s ease;
  padding: 0 2rem;
}
.navbar::before {
  content: '';
  position: absolute;
  inset: 0;
  background: transparent;
  transition: background 0.35s ease, box-shadow 0.35s ease;
  pointer-events: none;
}
.navbar--scrolled::before {
  background: rgba(247, 237, 216, 0.94);
  backdrop-filter: blur(12px);
  box-shadow: 0 4px 24px var(--warm-shadow);
}
.nav-inner {
  max-width: 1200px;
  margin: 0 auto;
  height: 70px;
  display: flex;
  align-items: center;
  gap: 2rem;
  position: relative;
}
.nav-logo      { display: flex; align-items: center; gap: 0.45rem; flex-shrink: 0; }
.nav-logo__paw { font-size: 1.6rem; filter: drop-shadow(0 2px 4px var(--warm-shadow)); }
.nav-logo__text {
  font-family: var(--font-serif);
  font-size: 1.55rem;
  font-weight: 700;
  color: var(--amber-dark);
  letter-spacing: 0.02em;
}
.nav-links { display: flex; gap: 0.3rem; position: absolute; left: 50%; transform: translateX(-50%); }
.nav-link {
  padding: 0.45rem 0.9rem;
  font-size: 0.9rem;
  color: var(--brown-mid);
  text-decoration: none;
  border-radius: 20px;
  transition: all 0.25s;
}
.nav-link:hover,
.nav-link--active { color: var(--amber-dark); background: rgba(196, 127, 53, 0.12); }

.nav-auth { display: flex; gap: 0.7rem; flex-shrink: 0; position: relative; margin-left: auto; }
.btn-login {
  padding: 0.42rem 1.1rem;
  font-size: 0.88rem;
  font-family: var(--font-sans);
  border: 1.5px solid var(--amber);
  background: transparent;
  color: var(--amber-dark);
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.25s;
}
.btn-login:hover { background: rgba(196, 127, 53, 0.1); }
.btn-register {
  padding: 0.42rem 1.1rem;
  font-size: 0.88rem;
  font-family: var(--font-sans);
  border: none;
  background: var(--amber);
  color: var(--warm-white);
  border-radius: 20px;
  cursor: pointer;
  box-shadow: 0 3px 10px rgba(196, 127, 53, 0.35);
  transition: all 0.25s;
}
.btn-register:hover { background: var(--amber-dark); transform: translateY(-1px); }

/* User info widget */
.user-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  padding: 0.3rem 0.6rem;
  border-radius: 20px;
  transition: background 0.2s;
  position: relative;
  user-select: none;
}
.user-info:hover { background: rgba(196, 127, 53, 0.12); }
.user-avatar {
  width: 32px; height: 32px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--amber-light);
}
.user-name { font-size: 0.88rem; font-weight: 700; color: var(--brown); }
.user-dropdown {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  background: var(--warm-white);
  border: 1px solid rgba(196, 127, 53, 0.2);
  border-radius: 12px;
  padding: 0.6rem 0;
  min-width: 160px;
  box-shadow: 0 8px 28px var(--warm-shadow-lg);
  z-index: 200;
}
.user-dropdown__name {
  padding: 0.4rem 1rem 0.6rem;
  font-size: 0.78rem;
  color: var(--brown-mid);
  opacity: 0.6;
  border-bottom: 1px solid rgba(196, 127, 53, 0.12);
  margin-bottom: 0.3rem;
}
.user-dropdown__item {
  display: block;
  padding: 0.5rem 1rem;
  font-size: 0.88rem;
  color: var(--brown);
  text-decoration: none;
  transition: background 0.2s;
}
.user-dropdown__item:hover { background: rgba(196, 127, 53, 0.08); }
.user-dropdown__logout {
  display: block;
  width: 100%;
  padding: 0.5rem 1rem;
  text-align: left;
  font-size: 0.88rem;
  font-family: var(--font-sans);
  color: #b91c1c;
  background: none;
  border: none;
  border-top: 1px solid rgba(196, 127, 53, 0.12);
  margin-top: 0.3rem;
  cursor: pointer;
  transition: background 0.2s;
}
.user-dropdown__logout:hover { background: rgba(185, 28, 28, 0.06); }

/* Hamburger */
.nav-hamburger { display: none; flex-direction: column; gap: 5px; background: none; border: none; cursor: pointer; padding: 4px; margin-left: auto; }
.nav-hamburger span { display: block; width: 24px; height: 2px; background: var(--brown-mid); border-radius: 2px; transition: all 0.3s; }
.nav-hamburger.open span:nth-child(1) { transform: translateY(7px) rotate(45deg); }
.nav-hamburger.open span:nth-child(2) { opacity: 0; }
.nav-hamburger.open span:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }

.nav-mobile {
  background: rgba(247, 237, 216, 0.98);
  backdrop-filter: blur(12px);
  padding: 1.2rem 2rem 1.5rem;
  border-top: 1px solid rgba(196, 127, 53, 0.2);
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}
.nav-mobile__link { padding: 0.7rem 0.5rem; font-size: 1rem; color: var(--brown-mid); text-decoration: none; border-bottom: 1px solid rgba(196, 127, 53, 0.12); }
.nav-mobile__auth { display: flex; gap: 0.8rem; padding-top: 1rem; flex-wrap: wrap; }
.mobile-user      { font-size: 0.9rem; color: var(--brown-mid); line-height: 2; }

/* Transitions */
.slide-down-enter-active, .slide-down-leave-active { transition: all 0.3s ease; }
.slide-down-enter-from,   .slide-down-leave-to     { opacity: 0; transform: translateY(-10px); }
.fade-enter-active,       .fade-leave-active        { transition: all 0.2s ease; }
.fade-enter-from,         .fade-leave-to            { opacity: 0; transform: translateY(-6px); }

/* ── Auth Modal ──────────────────────────────────────────── */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(61, 43, 31, 0.6);
  backdrop-filter: blur(6px);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
}
.modal-card {
  background: var(--warm-white);
  border-radius: 20px;
  padding: 2.5rem 2rem 2rem;
  width: 100%;
  max-width: 420px;
  position: relative;
  box-shadow: 0 20px 60px rgba(61, 43, 31, 0.4);
  border: 1px solid rgba(196, 127, 53, 0.18);
}
.modal-close {
  position: absolute;
  top: 1rem; right: 1rem;
  background: none;
  border: none;
  font-size: 1rem;
  color: var(--brown-mid);
  cursor: pointer;
  opacity: 0.6;
  transition: opacity 0.2s;
}
.modal-close:hover { opacity: 1; }
.modal-header     { text-align: center; margin-bottom: 1.5rem; }
.modal-logo       { font-size: 2rem; display: block; margin-bottom: 0.4rem; }
.modal-title      { font-family: var(--font-serif); font-size: 1.5rem; font-weight: 700; color: var(--brown); margin-bottom: 0.3rem; }
.modal-subtitle   { font-size: 0.85rem; color: var(--brown-mid); opacity: 0.75; }
.modal-error {
  background: rgba(185, 28, 28, 0.08);
  border: 1px solid rgba(185, 28, 28, 0.2);
  color: #b91c1c;
  border-radius: 8px;
  padding: 0.6rem 0.9rem;
  font-size: 0.85rem;
  margin-bottom: 1rem;
}
.modal-form  { display: flex; flex-direction: column; gap: 1rem; }
.form-group  { display: flex; flex-direction: column; gap: 0.35rem; }
.form-group label { font-size: 0.82rem; font-weight: 700; color: var(--brown); }
.hint        { font-weight: 400; opacity: 0.6; font-size: 0.76rem; }
.form-group input {
  padding: 0.65rem 0.9rem;
  border: 1.5px solid rgba(196, 127, 53, 0.25);
  border-radius: 10px;
  background: var(--cream);
  color: var(--brown);
  font-size: 0.92rem;
  font-family: var(--font-sans);
  outline: none;
  transition: border-color 0.2s;
}
.form-group input:focus { border-color: var(--amber); }
.form-group input::placeholder { color: rgba(92, 61, 46, 0.35); }
.modal-submit {
  margin-top: 0.5rem;
  padding: 0.8rem;
  background: var(--amber-dark);
  color: white;
  border: none;
  border-radius: 10px;
  font-family: var(--font-sans);
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.25s;
  box-shadow: 0 4px 14px rgba(139, 91, 28, 0.38);
}
.modal-submit:hover:not(:disabled) { background: #7A4D14; transform: translateY(-1px); }
.modal-submit:disabled { opacity: 0.6; cursor: not-allowed; }
.loading-dot { letter-spacing: 0.3em; animation: blink 1s infinite; }
@keyframes blink { 0%,100%{opacity:1} 50%{opacity:0.3} }
.modal-switch {
  text-align: center;
  margin-top: 1.2rem;
  font-size: 0.85rem;
  color: var(--brown-mid);
  opacity: 0.8;
}
.modal-switch__btn {
  background: none;
  border: none;
  color: var(--amber-dark);
  font-weight: 700;
  cursor: pointer;
  font-size: 0.85rem;
  font-family: var(--font-sans);
  text-decoration: underline;
  margin-left: 0.3rem;
}
.modal-enter-active, .modal-leave-active { transition: all 0.3s ease; }
.modal-enter-from,   .modal-leave-to     { opacity: 0; transform: scale(0.95); }

@media (max-width: 768px) {
  .nav-links, .nav-auth { display: none; }
  .nav-hamburger { display: flex; }
}
</style>

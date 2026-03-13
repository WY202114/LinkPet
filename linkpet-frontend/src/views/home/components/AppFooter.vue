<template>
  <footer class="footer">
    <div class="footer__inner">
      <!-- Brand column -->
      <div class="footer__brand">
        <div class="footer__logo">
          <span>🐾</span>
          <span class="footer__logo-text">LinkPet</span>
        </div>
        <p class="footer__tagline">
          Connecting wandering paws with warm hearts since 2024.
        </p>
        <div class="footer__socials">
          <a href="#" class="social-icon" title="Weibo">微</a>
          <a href="#" class="social-icon" title="WeChat">微信</a>
          <a href="#" class="social-icon" title="GitHub">GH</a>
        </div>
      </div>

      <!-- Platform links -->
      <div class="footer__col">
        <p class="footer__col-title">Platform</p>
        <a href="#" class="footer__link">Home</a>
        <a href="#" class="footer__link">Adoption Center</a>
        <a href="#" class="footer__link">Pet Community</a>
        <a href="#" class="footer__link">Rescue Guide</a>
      </div>

      <!-- Community links -->
      <div class="footer__col">
        <p class="footer__col-title">Community</p>
        <a href="#" class="footer__link">Adoption Diaries</a>
        <a href="#" class="footer__link">Daily Pet Life</a>
        <a href="#" class="footer__link">Foster a Pet</a>
        <a href="#" class="footer__link">Volunteer</a>
      </div>

      <!-- Support links -->
      <div class="footer__col">
        <p class="footer__col-title">Support</p>
        <a href="#" class="footer__link">About LinkPet</a>
        <a href="#" class="footer__link">Contact Us</a>
        <a href="#" class="footer__link">Privacy Policy</a>
        <a href="#" class="footer__link">Terms of Service</a>
      </div>

      <!-- Contact & newsletter -->
      <div class="footer__col footer__col--contact">
        <p class="footer__col-title">Get in Touch</p>
        <p class="footer__contact-item">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor">
            <path d="M20 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"/>
          </svg>
          hello@linkpet.org
        </p>
        <p class="footer__contact-item">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7z"/>
          </svg>
          Beijing · Shanghai · Guangzhou
        </p>
        <div class="footer__newsletter">
          <input
            v-model="email"
            type="email"
            placeholder="Your email address"
            class="footer__email-input"
          />
          <button class="footer__email-btn" @click="handleSubscribe">Subscribe</button>
        </div>
      </div>
    </div>

    <!-- Bottom bar -->
    <div class="footer__bottom">
      <p>© 2024 LinkPet. Made with ❤️ for strays everywhere.</p>
      <p>A project dedicated to animal welfare and community connection.</p>
    </div>
  </footer>
</template>

<script setup>
import { ref } from 'vue'

const email = ref('')

const handleSubscribe = () => {
  if (!email.value) return
  // TODO: connect to real newsletter API
  alert(`Thank you! ${email.value} has been subscribed.`)
  email.value = ''
}
</script>

<style scoped>
.footer {
  background: var(--brown);
  color: var(--cream-dark);
  padding: 4rem 2rem 1.5rem;
}

.footer__inner {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr 1.8fr;
  gap: 2.5rem;
  padding-bottom: 3rem;
  border-bottom: 1px solid rgba(247, 237, 216, 0.12);
}

/* Brand */
.footer__logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.9rem;
  font-size: 1.1rem;
}
.footer__logo-text {
  font-family: var(--font-serif);
  font-size: 1.4rem;
  font-weight: 700;
  color: var(--cream);
}
.footer__tagline {
  font-size: 0.85rem;
  line-height: 1.7;
  color: var(--cream-dark);
  opacity: 0.7;
  margin-bottom: 1.2rem;
}
.footer__socials { display: flex; gap: 0.7rem; }
.social-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 34px; height: 34px;
  border-radius: 8px;
  border: 1px solid rgba(247, 237, 216, 0.2);
  color: var(--cream-dark);
  font-size: 0.72rem;
  font-weight: 700;
  text-decoration: none;
  transition: all 0.25s;
  letter-spacing: -0.02em;
}
.social-icon:hover {
  background: rgba(196, 127, 53, 0.35);
  border-color: var(--amber-light);
  color: var(--cream);
}

/* Columns */
.footer__col {
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
}
.footer__col-title {
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 0.14em;
  text-transform: uppercase;
  color: var(--cream);
  margin-bottom: 0.4rem;
  opacity: 0.9;
}
.footer__link {
  font-size: 0.85rem;
  color: var(--cream-dark);
  text-decoration: none;
  opacity: 0.65;
  transition: all 0.2s;
  display: inline-block;
}
.footer__link:hover { opacity: 1; color: var(--amber-light); transform: translateX(3px); }

/* Contact */
.footer__contact-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.83rem;
  color: var(--cream-dark);
  opacity: 0.7;
  margin-bottom: 0.5rem;
}

/* Newsletter */
.footer__newsletter {
  display: flex;
  margin-top: 1rem;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid rgba(247, 237, 216, 0.2);
}
.footer__email-input {
  flex: 1;
  padding: 0.55rem 0.9rem;
  background: rgba(255, 255, 255, 0.06);
  border: none;
  color: var(--cream);
  font-size: 0.82rem;
  font-family: var(--font-sans);
  outline: none;
}
.footer__email-input::placeholder { color: rgba(237, 217, 171, 0.45); }
.footer__email-btn {
  padding: 0.55rem 1rem;
  background: var(--amber);
  border: none;
  color: white;
  font-family: var(--font-sans);
  font-size: 0.8rem;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.2s;
  white-space: nowrap;
}
.footer__email-btn:hover { background: var(--amber-dark); }

/* Bottom bar */
.footer__bottom {
  max-width: 1200px;
  margin: 0 auto;
  padding-top: 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 0.5rem;
  font-size: 0.78rem;
  color: var(--cream-dark);
  opacity: 0.5;
}

/* Responsive */
@media (max-width: 1024px) {
  .footer__inner { grid-template-columns: 1fr 1fr 1fr; }
  .footer__brand { grid-column: 1 / -1; }
  .footer__col--contact { grid-column: 1 / -1; }
}
@media (max-width: 640px) {
  .footer__inner { grid-template-columns: 1fr 1fr; }
  .footer__bottom { flex-direction: column; text-align: center; }
}
@media (max-width: 420px) {
  .footer__inner { grid-template-columns: 1fr; }
}
</style>

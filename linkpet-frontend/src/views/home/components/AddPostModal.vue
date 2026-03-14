<template>
  <Teleport to="body">
    <div v-if="visible" class="modal-overlay" @click.self="close">
      <div class="modal-card">
        <!-- Header -->
        <div class="modal-header">
          <h3 class="modal-title">✍️ 发布分享</h3>
          <button class="modal-close" @click="close">&times;</button>
        </div>

        <!-- Form -->
        <form class="modal-form" @submit.prevent="handleSubmit">
          <!-- Title -->
          <div class="form-group">
            <label class="form-label">故事标题 <span class="required">*</span></label>
            <input v-model="form.title" class="form-input" placeholder="如：小橘的第一天" required />
          </div>

          <!-- Content -->
          <div class="form-group">
            <label class="form-label">故事内容 <span class="required">*</span></label>
            <textarea v-model="form.content" class="form-input form-textarea" rows="4" placeholder="分享你们的温馨瞬间..." required />
          </div>

          <!-- Tags -->
          <div class="form-group">
            <label class="form-label">标签（逗号分隔）</label>
            <input v-model="form.tags" class="form-input" placeholder="如：领养日记,日常记录" />
          </div>

          <!-- Image upload -->
          <div class="form-group">
            <label class="form-label">上传图片（最多 4 张）</label>
            <div class="upload-area">
              <div v-for="(img, i) in previewUrls" :key="i" class="upload-thumb">
                <img :src="img" alt="preview" />
                <button type="button" class="thumb-remove" @click="removeImage(i)">&times;</button>
              </div>
              <label v-if="previewUrls.length < 4" class="upload-btn">
                <span>＋</span>
                <input type="file" accept="image/*" hidden @change="onFileChange" />
              </label>
            </div>
          </div>

          <!-- Submit -->
          <div class="form-actions">
            <button type="button" class="btn-cancel" @click="close">取消</button>
            <button type="submit" class="btn-submit" :disabled="submitting">
              {{ submitting ? '发布中...' : '确认发布' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { createPost } from '@/api/posts.js'
import { uploadImage } from '@/api/upload.js'

const props = defineProps({ visible: Boolean })
const emit = defineEmits(['close', 'success'])

const submitting  = ref(false)
const previewUrls = ref([])   // local blob URLs for preview
const uploadedKeys = ref([])  // MinIO keys returned from server

const form = reactive({
  title: '',
  content: '',
  tags: '',
})

// ── Image handling ─────────────────────────────────────────────
const onFileChange = async (e) => {
  const file = e.target.files?.[0]
  if (!file) return
  // Show preview immediately
  previewUrls.value.push(URL.createObjectURL(file))
  try {
    const key = await uploadImage(file)
    uploadedKeys.value.push(key)
  } catch {
    alert('图片上传失败，请重试')
    previewUrls.value.pop()
  }
  e.target.value = ''          // allow re-selecting same file
}

const removeImage = (index) => {
  URL.revokeObjectURL(previewUrls.value[index])
  previewUrls.value.splice(index, 1)
  uploadedKeys.value.splice(index, 1)
}

// ── Submit ─────────────────────────────────────────────────────
const handleSubmit = async () => {
  submitting.value = true
  try {
    await createPost({
      ...form,
      images: uploadedKeys.value.length ? uploadedKeys.value : null,
    })
    alert('发布成功！')
    emit('success')
    close()
  } catch (err) {
    alert(err.message || '发布失败，请重试')
  } finally {
    submitting.value = false
  }
}

// ── Close & reset ──────────────────────────────────────────────
const close = () => {
  Object.assign(form, { title: '', content: '', tags: '' })
  previewUrls.value.forEach(u => URL.revokeObjectURL(u))
  previewUrls.value = []
  uploadedKeys.value = []
  emit('close')
}
</script>

<style scoped>
/* Same overlay and card styling as AddPetModal.vue */
.modal-overlay {
  position: fixed;
  inset: 0;
  z-index: 9000;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(61, 43, 31, 0.45);
  backdrop-filter: blur(6px);
  animation: fadeIn 0.2s ease;
}

.modal-card {
  width: 520px;
  max-width: 92vw;
  max-height: 85vh;
  overflow-y: auto;
  background: var(--warm-white, #FBF5E6);
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(61, 43, 31, 0.35);
  animation: slideUp 0.3s ease;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.4rem 1.8rem 0.6rem;
  border-bottom: 1px solid rgba(196, 127, 53, 0.15);
}
.modal-title {
  font-family: var(--font-serif, serif);
  font-size: 1.35rem;
  color: var(--brown, #3D2B1F);
}
.modal-close {
  background: none;
  border: none;
  font-size: 1.6rem;
  cursor: pointer;
  color: var(--brown-mid, #5C3D2E);
  line-height: 1;
  transition: color 0.2s;
}
.modal-close:hover { color: var(--amber, #C47F35); }

.modal-form { padding: 1.2rem 1.8rem 1.6rem; }

.form-group { margin-bottom: 1.2rem; }

.form-label {
  display: block;
  font-size: 0.85rem;
  font-weight: 700;
  color: var(--brown-mid, #5C3D2E);
  margin-bottom: 0.4rem;
}
.required { color: #c0392b; }

.form-input {
  width: 100%;
  padding: 0.65rem 1rem;
  font-family: var(--font-sans, sans-serif);
  font-size: 0.95rem;
  border: 1.5px solid rgba(196, 127, 53, 0.3);
  border-radius: 12px;
  background: rgba(247, 237, 216, 0.4);
  color: var(--brown, #3D2B1F);
  outline: none;
  transition: border-color 0.2s;
}
.form-input:focus { border-color: var(--amber, #C47F35); }
.form-textarea { resize: vertical; line-height: 1.5; }

.upload-area {
  display: flex;
  gap: 0.6rem;
  flex-wrap: wrap;
}
.upload-thumb {
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 10px;
  overflow: hidden;
  border: 1.5px solid rgba(196, 127, 53, 0.25);
}
.upload-thumb img { width: 100%; height: 100%; object-fit: cover; }
.thumb-remove {
  position: absolute;
  top: 2px; right: 2px;
  width: 20px; height: 20px;
  border-radius: 50%;
  background: rgba(0,0,0,0.55);
  color: white;
  border: none;
  font-size: 0.75rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-btn {
  width: 80px;
  height: 80px;
  border-radius: 10px;
  border: 2px dashed rgba(196, 127, 53, 0.35);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 1.6rem;
  color: var(--amber, #C47F35);
  transition: border-color 0.2s, background 0.2s;
}
.upload-btn:hover {
  border-color: var(--amber, #C47F35);
  background: rgba(196, 127, 53, 0.08);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.8rem;
  margin-top: 1.5rem;
}
.btn-cancel {
  padding: 0.6rem 1.6rem;
  border-radius: 30px;
  border: 1.5px solid rgba(196, 127, 53, 0.3);
  background: transparent;
  color: var(--brown-mid, #5C3D2E);
  font-family: var(--font-sans, sans-serif);
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.2s;
}
.btn-cancel:hover { border-color: var(--amber, #C47F35); color: var(--amber-dark, #8B5B1C); }

.btn-submit {
  padding: 0.6rem 2rem;
  border-radius: 30px;
  border: none;
  background: var(--amber, #C47F35);
  color: white;
  font-family: var(--font-sans, sans-serif);
  font-size: 0.95rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 4px 16px rgba(196, 127, 53, 0.3);
}
.btn-submit:hover { background: var(--amber-dark, #8B5B1C); transform: translateY(-1px); }
.btn-submit:disabled { opacity: 0.6; cursor: not-allowed; transform: none; }

@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
@keyframes slideUp { from { opacity: 0; transform: translateY(30px); } to { opacity: 1; transform: translateY(0); } }

.modal-card::-webkit-scrollbar { width: 6px; }
.modal-card::-webkit-scrollbar-thumb { background: rgba(196, 127, 53, 0.3); border-radius: 3px; }
</style>

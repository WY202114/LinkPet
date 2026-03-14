<template>
  <Teleport to="body">
    <div v-if="visible" class="modal-overlay" @click.self="close">
      <div class="modal-card">
        <!-- Header -->
        <div class="modal-header">
          <h3 class="modal-title">🐾 发布宠物</h3>
          <button class="modal-close" @click="close">&times;</button>
        </div>

        <!-- Form -->
        <form class="modal-form" @submit.prevent="handleSubmit">
          <!-- Row: name + typeId -->
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">宠物名称 <span class="required">*</span></label>
              <input v-model="form.name" class="form-input" placeholder="如：小橘" required />
            </div>
            <div class="form-group">
              <label class="form-label">类型 <span class="required">*</span></label>
              <select v-model="form.typeId" class="form-input" required @change="onTypeChange">
                <option disabled value="">请选择类型</option>
                <option v-for="t in petTypes" :key="t.id" :value="t.id">{{ t.name }}</option>
              </select>
            </div>
          </div>

          <!-- Custom type input (when "其他" is selected) -->
          <div v-if="isOtherType" class="form-group custom-type-group">
            <label class="form-label">自定义品种 <span class="required">*</span></label>
            <input
              v-model="form.customTypeName"
              class="form-input"
              placeholder="请输入自定义品种，如：龙猫、仓鼠"
              :required="isOtherType"
            />
            <p class="custom-type-hint">自定义品种将提交管理员审核，审核通过后将正式生效</p>
          </div>

          <!-- Row: ageMonth + gender -->
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">年龄（月）</label>
              <input v-model.number="form.ageMonth" type="number" min="0" class="form-input" placeholder="如：12" />
            </div>
            <div class="form-group">
              <label class="form-label">性别</label>
              <div class="radio-group">
                <label class="radio-item"><input type="radio" v-model.number="form.gender" :value="0" /> 未知</label>
                <label class="radio-item"><input type="radio" v-model.number="form.gender" :value="1" /> 雄</label>
                <label class="radio-item"><input type="radio" v-model.number="form.gender" :value="2" /> 雌</label>
              </div>
            </div>
          </div>

          <!-- healthDesc -->
          <div class="form-group">
            <label class="form-label">健康状况</label>
            <textarea v-model="form.healthDesc" class="form-input form-textarea" rows="2" placeholder="如：已打疫苗，已绝育" />
          </div>

          <!-- personalityDesc -->
          <div class="form-group">
            <label class="form-label">性格描述</label>
            <textarea v-model="form.personalityDesc" class="form-input form-textarea" rows="2" placeholder="如：温顺，亲人" />
          </div>

          <!-- Image upload -->
          <div class="form-group">
            <label class="form-label">宠物图片</label>
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

          <!-- address -->
          <div class="form-group">
            <label class="form-label">所在地址 <span class="required">*</span></label>
            <input v-model="form.address" class="form-input" placeholder="如：北京市朝阳区" required />
          </div>

          <!-- location -->
          <div class="form-group">
            <label class="form-label">发现地点</label>
            <input v-model="form.location" class="form-input" placeholder="选填，如：小区门口" />
          </div>

          <!-- Submit -->
          <div class="form-actions">
            <button type="button" class="btn-cancel" @click="close">取消</button>
            <button type="submit" class="btn-submit" :disabled="submitting">
              {{ submitting ? '提交中...' : '发布宠物' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { createPet } from '@/api/pets.js'
import { getPetTypes } from '@/api/petTypes.js'
import { uploadImage } from '@/api/upload.js'

const props = defineProps({ visible: Boolean })
const emit = defineEmits(['close', 'success'])

const petTypes    = ref([])
const submitting  = ref(false)
const previewUrls = ref([])   // local blob URLs for preview
const uploadedKeys = ref([])  // MinIO keys returned from server

const form = reactive({
  name: '',
  typeId: '',
  customTypeName: '',
  ageMonth: null,
  gender: 0,
  healthDesc: '',
  personalityDesc: '',
  address: '',
  location: '',
})

// Check if user selected "其他"
const isOtherType = computed(() => {
  if (!form.typeId) return false
  const selected = petTypes.value.find(t => t.id === form.typeId)
  return selected?.name === '其他'
})

const onTypeChange = () => {
  if (!isOtherType.value) {
    form.customTypeName = ''
  }
}

// Load pet types when modal opens
watch(() => props.visible, async (val) => {
  if (val && petTypes.value.length === 0) {
    try {
      const types = await getPetTypes()
      if (Array.isArray(types)) petTypes.value = types
    } catch { /* ignore */ }
  }
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
    const payload = {
      ...form,
      images: uploadedKeys.value.length ? uploadedKeys.value : null,
    }
    // Only send customTypeName when "其他" is selected
    if (!isOtherType.value) {
      delete payload.customTypeName
    }
    await createPet(payload)
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
  Object.assign(form, { name: '', typeId: '', customTypeName: '', ageMonth: null, gender: 0, healthDesc: '', personalityDesc: '', address: '', location: '' })
  previewUrls.value.forEach(u => URL.revokeObjectURL(u))
  previewUrls.value = []
  uploadedKeys.value = []
  emit('close')
}
</script>

<style scoped>
/* ── Overlay ── */
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

/* ── Card ── */
.modal-card {
  width: 540px;
  max-width: 92vw;
  max-height: 88vh;
  overflow-y: auto;
  background: var(--warm-white, #FBF5E6);
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(61, 43, 31, 0.35);
  animation: slideUp 0.3s ease;
}

/* ── Header ── */
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

/* ── Form ── */
.modal-form { padding: 1.2rem 1.8rem 1.6rem; }

.form-row { display: flex; gap: 1rem; }
.form-row > .form-group { flex: 1; }

.form-group { margin-bottom: 1rem; }

.form-label {
  display: block;
  font-size: 0.82rem;
  font-weight: 700;
  color: var(--brown-mid, #5C3D2E);
  margin-bottom: 0.35rem;
}
.required { color: #c0392b; }

.form-input {
  width: 100%;
  padding: 0.55rem 0.9rem;
  font-family: var(--font-sans, sans-serif);
  font-size: 0.9rem;
  border: 1.5px solid rgba(196, 127, 53, 0.3);
  border-radius: 10px;
  background: rgba(247, 237, 216, 0.4);
  color: var(--brown, #3D2B1F);
  outline: none;
  transition: border-color 0.2s;
}
.form-input:focus { border-color: var(--amber, #C47F35); }

.form-textarea { resize: vertical; }

/* Custom type input */
.custom-type-group {
  animation: slideDown 0.25s ease;
}
.custom-type-hint {
  margin: 0.35rem 0 0;
  font-size: 0.72rem;
  color: var(--amber-dark, #8B5B1C);
  opacity: 0.75;
  line-height: 1.4;
}

@keyframes slideDown {
  from { opacity: 0; max-height: 0; transform: translateY(-8px); }
  to   { opacity: 1; max-height: 120px; transform: translateY(0); }
}

/* Radio group */
.radio-group {
  display: flex;
  gap: 1rem;
  padding-top: 0.4rem;
}
.radio-item {
  display: flex;
  align-items: center;
  gap: 0.3rem;
  font-size: 0.88rem;
  color: var(--brown-mid, #5C3D2E);
  cursor: pointer;
}

/* ── Upload ── */
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

/* ── Actions ── */
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.8rem;
  margin-top: 0.8rem;
}
.btn-cancel {
  padding: 0.55rem 1.5rem;
  border-radius: 30px;
  border: 1.5px solid rgba(196, 127, 53, 0.3);
  background: transparent;
  color: var(--brown-mid, #5C3D2E);
  font-family: var(--font-sans, sans-serif);
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.2s;
}
.btn-cancel:hover { border-color: var(--amber, #C47F35); color: var(--amber-dark, #8B5B1C); }

.btn-submit {
  padding: 0.55rem 1.8rem;
  border-radius: 30px;
  border: none;
  background: var(--amber, #C47F35);
  color: white;
  font-family: var(--font-sans, sans-serif);
  font-size: 0.9rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 4px 16px rgba(196, 127, 53, 0.3);
}
.btn-submit:hover { background: var(--amber-dark, #8B5B1C); transform: translateY(-1px); }
.btn-submit:disabled { opacity: 0.6; cursor: not-allowed; transform: none; }

/* ── Animations ── */
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
@keyframes slideUp { from { opacity: 0; transform: translateY(30px); } to { opacity: 1; transform: translateY(0); } }

/* ── Scrollbar ── */
.modal-card::-webkit-scrollbar { width: 6px; }
.modal-card::-webkit-scrollbar-thumb { background: rgba(196, 127, 53, 0.3); border-radius: 3px; }
</style>

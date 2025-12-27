<template>
  <div class="login-container">
    <div class="login-card">
      <!-- Logo区域 -->
      <div class="logo-section">
        <div class="logo-icon">
          <svg width="60" height="60" viewBox="0 0 24 24" fill="none">
            <path d="M13 2L3 14h8l-1 8 10-12h-8l1-8z" fill="url(#gradient)" stroke="currentColor" stroke-width="1.5" stroke-linejoin="round"/>
            <defs>
              <linearGradient id="gradient" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" style="stop-color:#4CAF50;stop-opacity:1" />
                <stop offset="100%" style="stop-color:#2E7D32;stop-opacity:1" />
              </linearGradient>
            </defs>
          </svg>
        </div>
        <h1 class="logo-title">充电桩管理系统</h1>
        <p class="logo-subtitle">用户注册</p>
      </div>

      <!-- 注册表单 -->
      <form @submit.prevent="handleRegister" class="login-form">
        <div class="form-group">
          <label for="username">用户名</label>
          <div class="input-wrapper">
            <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2M12 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8z" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <input
              id="username"
              v-model="formData.username"
              type="text"
              placeholder="请输入用户名 (3-50字符)"
              required
              minlength="3"
              maxlength="50"
              autocomplete="username"
            />
          </div>
        </div>

        <div class="form-group">
          <label for="phone">手机号</label>
          <div class="input-wrapper">
            <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <input
              id="phone"
              v-model="formData.phone"
              type="tel"
              placeholder="请输入手机号"
              required
              pattern="^1[3-9]\d{9}$"
              autocomplete="tel"
            />
          </div>
        </div>

        <div class="form-group">
          <label for="password">密码</label>
          <div class="input-wrapper">
            <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <rect x="3" y="11" width="18" height="11" rx="2" ry="2" stroke-width="2"/>
              <path d="M7 11V7a5 5 0 0 1 10 0v4" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <input
              id="password"
              v-model="formData.password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="请输入密码 (6-20字符)"
              required
              minlength="6"
              maxlength="20"
              autocomplete="new-password"
            />
            <button
              type="button"
              class="password-toggle"
              @click="showPassword = !showPassword"
            >
              <svg v-if="!showPassword" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" stroke-width="2"/>
                <circle cx="12" cy="12" r="3" stroke-width="2"/>
              </svg>
              <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24M1 1l22 22" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
          </div>
        </div>

        <div class="form-group">
          <label for="confirmPassword">确认密码</label>
          <div class="input-wrapper">
            <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <rect x="3" y="11" width="18" height="11" rx="2" ry="2" stroke-width="2"/>
              <path d="M7 11V7a5 5 0 0 1 10 0v4" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <input
              id="confirmPassword"
              v-model="confirmPassword"
              :type="showConfirmPassword ? 'text' : 'password'"
              placeholder="请再次输入密码"
              required
              autocomplete="new-password"
            />
            <button
              type="button"
              class="password-toggle"
              @click="showConfirmPassword = !showConfirmPassword"
            >
              <svg v-if="!showConfirmPassword" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" stroke-width="2"/>
                <circle cx="12" cy="12" r="3" stroke-width="2"/>
              </svg>
              <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24M1 1l22 22" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
          </div>
        </div>

        <button type="submit" class="login-button" :disabled="loading">
          <span v-if="!loading">注册</span>
          <span v-else class="loading-spinner">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <circle cx="12" cy="12" r="10" stroke-width="3" stroke-dasharray="31.4 31.4" stroke-linecap="round">
                <animateTransform attributeName="transform" type="rotate" from="0 12 12" to="360 12 12" dur="1s" repeatCount="indefinite"/>
              </circle>
            </svg>
            注册中...
          </span>
        </button>

        <div class="register-link">
          已有账号？ <a href="#" @click.prevent="$router.push('/login')">立即登录</a>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { authAPI } from '../api'

const router = useRouter()
const loading = ref(false)
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const confirmPassword = ref('')

const formData = reactive({
  username: '',
  password: '',
  phone: ''
})

const handleRegister = async () => {
  if (formData.password !== confirmPassword.value) {
    alert('两次输入的密码不一致')
    return
  }

  loading.value = true
  try {
    await authAPI.register(formData)
    alert('注册成功，请登录')
    router.push('/login')
  } catch (error) {
    console.error('注册失败:', error)
    alert(error.response?.data?.message || '注册失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 复用 Login.vue 的样式 */
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
}

.login-card {
  background: white;
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
  width: 100%;
  max-width: 420px;
  animation: slideUp 0.5s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.logo-section {
  text-align: center;
  margin-bottom: 40px;
}

.logo-icon {
  margin-bottom: 16px;
  color: #4CAF50;
}

.logo-title {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 8px;
}

.logo-subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 14px;
  font-weight: 500;
  color: #606266;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 12px;
  color: #909399;
  pointer-events: none;
}

.input-wrapper input {
  width: 100%;
  height: 44px;
  padding: 0 40px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  font-size: 14px;
  color: #606266;
  transition: all 0.3s;
  background: #f8f9fa;
}

.input-wrapper input:focus {
  border-color: #4CAF50;
  background: white;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.1);
  outline: none;
}

.input-wrapper input:focus + .input-icon {
  color: #4CAF50;
}

.password-toggle {
  position: absolute;
  right: 12px;
  background: none;
  border: none;
  padding: 0;
  color: #909399;
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: color 0.3s;
}

.password-toggle:hover {
  color: #606266;
}

.login-button {
  height: 44px;
  background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 8px;
}

.login-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.3);
}

.login-button:active {
  transform: translateY(0);
}

.login-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.loading-spinner {
  display: flex;
  align-items: center;
  gap: 8px;
}

.loading-spinner svg {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.register-link {
  text-align: center;
  font-size: 14px;
  color: #606266;
}

.register-link a {
  color: #4CAF50;
  text-decoration: none;
  font-weight: 500;
  margin-left: 4px;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>

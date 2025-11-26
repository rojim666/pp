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
        <p class="logo-subtitle">Charging Station Management System</p>
      </div>

      <!-- 登录表单 -->
      <form @submit.prevent="handleLogin" class="login-form">
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
              placeholder="请输入用户名"
              required
              autocomplete="username"
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
              placeholder="请输入密码"
              required
              autocomplete="current-password"
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

        <div class="form-options">
          <label class="checkbox-label">
            <input v-model="rememberMe" type="checkbox" />
            <span>记住我</span>
          </label>
          <a href="#" class="forgot-password">忘记密码？</a>
        </div>

        <button type="submit" class="login-button" :disabled="loading">
          <span v-if="!loading">登录</span>
          <span v-else class="loading-spinner">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <circle cx="12" cy="12" r="10" stroke-width="3" stroke-dasharray="31.4 31.4" stroke-linecap="round">
                <animateTransform attributeName="transform" type="rotate" from="0 12 12" to="360 12 12" dur="1s" repeatCount="indefinite"/>
              </circle>
            </svg>
            登录中...
          </span>
        </button>
      </form>

      <!-- 快速登录提示 -->
      <div class="quick-login">
        <p class="hint">💡 快速登录测试账号</p>
        <div class="test-accounts">
          <button @click="quickLogin('admin')" class="test-btn">管理员</button>
          <button @click="quickLogin('operator')" class="test-btn">运营</button>
          <button @click="quickLogin('user')" class="test-btn">用户</button>
        </div>
      </div>

      <!-- 错误提示 -->
      <transition name="fade">
        <div v-if="errorMessage" class="error-message">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
            <circle cx="12" cy="12" r="10" stroke-width="2"/>
            <line x1="12" y1="8" x2="12" y2="12" stroke-width="2" stroke-linecap="round"/>
            <line x1="12" y1="16" x2="12.01" y2="16" stroke-width="2" stroke-linecap="round"/>
          </svg>
          {{ errorMessage }}
        </div>
      </transition>

      <!-- 注册链接 -->
      <div class="register-link">
        还没有账号？ <a href="#" @click.prevent="$router.push('/register')">立即注册</a>
      </div>
    </div>

    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()

const formData = ref({
  username: '',
  password: ''
})

const showPassword = ref(false)
const rememberMe = ref(false)
const loading = ref(false)
const errorMessage = ref('')

// 登录处理
const handleLogin = async () => {
  try {
    loading.value = true
    errorMessage.value = ''

    await userStore.login(formData.value)
    
    // 登录成功，跳转到首页
    router.push('/dashboard')
  } catch (error) {
    errorMessage.value = error.message || '登录失败，请检查用户名和密码'
    
    // 3秒后自动清除错误信息
    setTimeout(() => {
      errorMessage.value = ''
    }, 3000)
  } finally {
    loading.value = false
  }
}

// 快速登录
const quickLogin = async (role) => {
  const accounts = {
    admin: { username: 'admin', password: 'admin123' },
    operator: { username: 'operator', password: 'operator123' },
    user: { username: 'user', password: 'user123' }
  }
  
  formData.value = accounts[role]
  await handleLogin()
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f5f5 0%, #e8e8e8 100%);
  padding: 20px;
  position: relative;
  overflow: hidden;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 48px;
  width: 100%;
  max-width: 440px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  position: relative;
  z-index: 1;
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Logo区域 */
.logo-section {
  text-align: center;
  margin-bottom: 40px;
}

.logo-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%);
  border-radius: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.15);
  color: #2E7D32;
}

.logo-title {
  font-size: 28px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0 0 8px 0;
}

.logo-subtitle {
  font-size: 14px;
  color: #8e8e93;
  margin: 0;
  font-weight: 500;
}

/* 表单样式 */
.login-form {
  margin-bottom: 24px;
}

.form-group {
  margin-bottom: 24px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 8px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 16px;
  color: #8e8e93;
  pointer-events: none;
}

.input-wrapper input {
  width: 100%;
  height: 52px;
  padding: 0 48px;
  border: 2px solid #e5e5ea;
  border-radius: 12px;
  font-size: 16px;
  transition: all 0.3s ease;
  background: white;
}

.input-wrapper input:focus {
  outline: none;
  border-color: #333;
  box-shadow: 0 0 0 4px rgba(0, 0, 0, 0.05);
}

.password-toggle {
  position: absolute;
  right: 16px;
  background: none;
  border: none;
  color: #8e8e93;
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: color 0.3s;
}

.password-toggle:hover {
  color: #333;
}

/* 表单选项 */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #3a3a3c;
  cursor: pointer;
  user-select: none;
}

.checkbox-label input {
  margin-right: 8px;
  cursor: pointer;
}

.forgot-password {
  font-size: 14px;
  color: #666;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}

.forgot-password:hover {
  color: #333;
}

/* 登录按钮 */
.login-button {
  width: 100%;
  height: 52px;
  background: linear-gradient(135deg, #333 0%, #555 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.login-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.login-button:active:not(:disabled) {
  transform: translateY(0);
}

.login-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 快速登录 */
.quick-login {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #e5e5ea;
}

.hint {
  font-size: 13px;
  color: #8e8e93;
  text-align: center;
  margin-bottom: 12px;
}

.test-accounts {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.test-btn {
  padding: 8px 20px;
  background: #f2f2f7;
  border: 1px solid #e5e5ea;
  border-radius: 8px;
  font-size: 13px;
  color: #3a3a3c;
  cursor: pointer;
  transition: all 0.3s;
  font-weight: 500;
}

.test-btn:hover {
  background: #333;
  color: white;
  border-color: #333;
  transform: translateY(-2px);
}

/* 错误提示 */
.error-message {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: #ffebee;
  border: 1px solid #ffcdd2;
  border-radius: 8px;
  color: #c62828;
  font-size: 14px;
  margin-top: 16px;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

/* 注册链接 */
.register-link {
  text-align: center;
  margin-top: 24px;
  font-size: 14px;
  color: #8e8e93;
}

.register-link a {
  color: #333;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.3s;
}

.register-link a:hover {
  color: #666;
}

/* 背景装饰 */
.bg-decoration {
  position: absolute;
  inset: 0;
  overflow: hidden;
  z-index: 0;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  animation: float 20s infinite ease-in-out;
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -150px;
  right: -150px;
  animation-delay: 0s;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: -100px;
  left: -100px;
  animation-delay: 5s;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  left: -75px;
  animation-delay: 10s;
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  25% {
    transform: translate(20px, 20px) rotate(90deg);
  }
  50% {
    transform: translate(0, 40px) rotate(180deg);
  }
  75% {
    transform: translate(-20px, 20px) rotate(270deg);
  }
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-card {
    padding: 32px 24px;
  }

  .logo-title {
    font-size: 24px;
  }

  .test-accounts {
    flex-direction: column;
  }

  .test-btn {
    width: 100%;
  }
}
</style>

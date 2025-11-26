<template>
  <div class="page theme-settings">
    <div class="settings-header">
      <h1 class="page-title">主题与外观设置</h1>
      <p class="settings-description">自定义应用的显示效果和主题风格</p>
    </div>

    <!-- 主题模式选择 -->
    <div class="card theme-mode-card">
      <div class="card-header">
        <div class="header-title">
          <div class="header-icon">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="12" cy="12" r="5" stroke="currentColor" stroke-width="2"/>
              <path d="M12 1v6M12 17v6M4.22 4.22l4.24 4.24M15.54 15.54l4.24 4.24M1 12h6M17 12h6M4.22 19.78l4.24-4.24M15.54 8.46l4.24-4.24" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div>
            <h3>主题模式</h3>
            <p>选择应用主题显示方式</p>
          </div>
        </div>
      </div>

      <div class="theme-mode-options">
        <button
          v-for="option in themeModes"
          :key="option.value"
          :class="['theme-option', { active: themeStore.mode === option.value }]"
          @click="setThemeMode(option.value)"
        >
          <div class="option-icon">
            <svg :viewBox="option.icon.viewBox" xmlns="http://www.w3.org/2000/svg">
              <g v-html="option.icon.content"></g>
            </svg>
          </div>
          <div class="option-content">
            <div class="option-title">{{ option.label }}</div>
            <div class="option-desc">{{ option.description }}</div>
          </div>
          <div class="option-check">
            <svg v-if="themeStore.mode === option.value" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M20 6L9 17L4 12" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
        </button>
      </div>
    </div>

    <!-- 主题预览 -->
    <div class="card theme-preview-card">
      <div class="card-header">
        <div class="header-title">
          <div class="header-icon">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect x="3" y="3" width="18" height="18" rx="2" stroke="currentColor" stroke-width="2"/>
              <path d="M3 9h18M3 15h18M9 3v18M15 3v18" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div>
            <h3>主题预览</h3>
            <p>预览当前主题效果</p>
          </div>
        </div>
      </div>

      <div class="preview-container">
        <div class="preview-item light-preview">
          <div class="preview-title">浅色模式</div>
          <div class="preview-demo">
            <div class="preview-header"></div>
            <div class="preview-content">
              <div class="preview-line short"></div>
              <div class="preview-line"></div>
              <div class="preview-line short"></div>
            </div>
          </div>
        </div>

        <div class="preview-item dark-preview">
          <div class="preview-title">深色模式</div>
          <div class="preview-demo dark">
            <div class="preview-header"></div>
            <div class="preview-content">
              <div class="preview-line short"></div>
              <div class="preview-line"></div>
              <div class="preview-line short"></div>
            </div>
          </div>
        </div>

        <div class="preview-item auto-preview">
          <div class="preview-title">自动（跟随系统）</div>
          <div class="preview-demo split">
            <div class="preview-header"></div>
            <div class="preview-content">
              <div class="preview-line short"></div>
              <div class="preview-line"></div>
              <div class="preview-line short"></div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 显示选项 -->
    <div class="card display-options-card">
      <div class="card-header">
        <div class="header-title">
          <div class="header-icon">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect x="2" y="3" width="20" height="14" rx="2" stroke="currentColor" stroke-width="2"/>
              <path d="M8 19h8M12 16v3" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div>
            <h3>显示选项</h3>
            <p>调整显示效果和外观</p>
          </div>
        </div>
      </div>

      <div class="options-list">
        <div class="option-item">
          <div class="option-info">
            <div class="option-title">系统跟随</div>
            <div class="option-desc">在系统主题变化时自动切换应用主题</div>
          </div>
          <div class="option-control">
            <label class="toggle-switch">
              <input
                type="checkbox"
                :checked="themeStore.followSystem"
                @change="toggleFollowSystem"
              />
              <span class="toggle-slider"></span>
            </label>
          </div>
        </div>

        <div class="option-divider"></div>

        <div class="option-item">
          <div class="option-info">
            <div class="option-title">当前主题模式</div>
            <div class="option-desc">{{ actualThemeLabel }}</div>
          </div>
          <div class="option-value">
            <span class="theme-badge" :class="themeStore.actualTheme">
              {{ themeStore.actualTheme === 'light' ? '浅色' : '深色' }}
            </span>
          </div>
        </div>

        <div class="option-divider"></div>

        <div class="option-item">
          <div class="option-info">
            <div class="option-title">系统偏好</div>
            <div class="option-desc">您的操作系统设置的主题偏好</div>
          </div>
          <div class="option-value">
            <span class="system-preference">
              {{ systemPreference }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 快捷操作 -->
    <div class="card quick-actions-card">
      <div class="card-header">
        <div class="header-title">
          <div class="header-icon">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M13 10V3L4 14h7v7l9-11h-7z" stroke="currentColor" stroke-width="2" stroke-linejoin="round" stroke-linecap="round"/>
            </svg>
          </div>
          <div>
            <h3>快捷操作</h3>
            <p>快速切换主题</p>
          </div>
        </div>
      </div>

      <div class="quick-buttons">
        <button class="quick-btn light-btn" @click="themeStore.setThemeMode('light')">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="12" cy="12" r="5" stroke="currentColor" stroke-width="2"/>
            <path d="M12 1v6M12 17v6M4.22 4.22l4.24 4.24M15.54 15.54l4.24 4.24M1 12h6M17 12h6M4.22 19.78l4.24-4.24M15.54 8.46l4.24-4.24" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          <span>浅色模式</span>
        </button>

        <button class="quick-btn dark-btn" @click="themeStore.setThemeMode('dark')">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <span>深色模式</span>
        </button>

        <button class="quick-btn auto-btn" @click="themeStore.setThemeMode('auto')">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm3.5-9c0 1.93-1.57 3.5-3.5 3.5S8.5 12.93 8.5 11 10.07 7.5 12 7.5s3.5 1.57 3.5 3.5z" fill="currentColor"/>
          </svg>
          <span>自动</span>
        </button>
      </div>
    </div>

    <!-- 信息面板 -->
    <div class="card info-card">
      <div class="info-icon">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
          <path d="M12 16v-4M12 8h.01" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
        </svg>
      </div>
      <div class="info-content">
        <h4>提示</h4>
        <p>主题设置会自动保存到本地，下次打开应用时会应用您的选择。如果选择"自动"模式，应用会根据您的系统主题偏好自动切换。</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useThemeStore } from '../stores/theme'

const themeStore = useThemeStore()

const themeModes = [
  {
    value: 'light',
    label: '浅色模式',
    description: '始终使用浅色主题',
    icon: {
      viewBox: '0 0 24 24',
      content: `<circle cx="12" cy="12" r="5" stroke="currentColor" stroke-width="2" fill="none"/>
               <path d="M12 1v6M12 17v6M4.22 4.22l4.24 4.24M15.54 15.54l4.24 4.24M1 12h6M17 12h6M4.22 19.78l4.24-4.24M15.54 8.46l4.24-4.24" stroke="currentColor" stroke-width="2" stroke-linecap="round" fill="none"/>`
    }
  },
  {
    value: 'dark',
    label: '深色模式',
    description: '始终使用深色主题',
    icon: {
      viewBox: '0 0 24 24',
      content: `<path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round"/>`
    }
  },
  {
    value: 'auto',
    label: '自动',
    description: '跟随系统设置自动切换',
    icon: {
      viewBox: '0 0 24 24',
      content: `<path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm3.5-9c0 1.93-1.57 3.5-3.5 3.5S8.5 12.93 8.5 11 10.07 7.5 12 7.5s3.5 1.57 3.5 3.5z" fill="currentColor"/>`
    }
  }
]

const systemPreference = computed(() => {
  if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
    return '深色主题'
  }
  return '浅色主题'
})

const actualThemeLabel = computed(() => {
  if (themeStore.mode === 'auto') {
    return '自动模式 - ' + (themeStore.actualTheme === 'light' ? '当前为浅色' : '当前为深色')
  }
  return themeStore.mode === 'light' ? '浅色模式' : '深色模式'
})

const setThemeMode = (mode) => {
  themeStore.setThemeMode(mode)
}

const toggleFollowSystem = (e) => {
  themeStore.setFollowSystem(e.target.checked)
}
</script>

<style scoped>
.theme-settings {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.settings-header {
  margin-bottom: 8px;
}

.settings-description {
  font-size: 15px;
  color: var(--color-text-secondary);
  margin: 8px 0 0 0;
  font-weight: 500;
}

/* 卡片通用样式 */
.card {
  background: var(--color-bg-primary);
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-separator);
  box-shadow: var(--shadow-sm);
  overflow: hidden;
  transition: all var(--transition-duration) ease;
}

.card:hover {
  box-shadow: var(--shadow-md);
}

.card-header {
  padding: 24px;
  border-bottom: 1px solid var(--color-separator);
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 16px;
  flex: 1;
}

.header-icon {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.1), rgba(48, 183, 79, 0.1));
  display: flex;
  align-items: center;
  justify-content: center;
  color: #34C759;
  flex-shrink: 0;
}

.header-icon svg {
  width: 24px;
  height: 24px;
}

.header-title h3 {
  font-size: 18px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0 0 4px 0;
}

.header-title p {
  font-size: 13px;
  color: var(--color-text-secondary);
  margin: 0;
  font-weight: 500;
}

/* 主题模式选择 */
.theme-mode-options {
  padding: 24px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 16px;
}

.theme-option {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  border: 2px solid var(--color-separator);
  border-radius: 12px;
  background: transparent;
  cursor: pointer;
  transition: all 0.25s ease;
  text-align: left;
}

.theme-option:hover {
  border-color: #34C759;
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.03), rgba(48, 183, 79, 0.03));
}

.theme-option.active {
  border-color: #34C759;
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.08), rgba(48, 183, 79, 0.08));
  box-shadow: 0 0 0 3px rgba(52, 199, 89, 0.1);
}

.option-icon {
  width: 44px;
  height: 44px;
  border-radius: 10px;
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.1), rgba(48, 183, 79, 0.1));
  display: flex;
  align-items: center;
  justify-content: center;
  color: #34C759;
  flex-shrink: 0;
}

.option-icon svg {
  width: 22px;
  height: 22px;
}

.option-content {
  flex: 1;
}

.option-title {
  font-size: 15px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 4px;
}

.option-desc {
  font-size: 12px;
  color: var(--color-text-secondary);
}

.option-check {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  opacity: 0;
  transition: all 0.25s ease;
}

.option-check svg {
  width: 16px;
  height: 16px;
  color: #34C759;
  stroke-width: 3;
}

.theme-option.active .option-check {
  opacity: 1;
  background: rgba(52, 199, 89, 0.1);
}

/* 主题预览 */
.preview-container {
  padding: 24px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
}

.preview-item {
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid var(--color-separator);
}

.preview-title {
  font-size: 14px;
  font-weight: 700;
  color: var(--color-text-primary);
  padding: 12px 16px;
  background: var(--color-bg-secondary);
  border-bottom: 1px solid var(--color-separator);
}

.preview-demo {
  background: #FFFFFF;
  padding: 16px;
  height: 120px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.preview-demo.dark {
  background: #1C1C1E;
}

.preview-demo.split {
  background: linear-gradient(to right, #FFFFFF 50%, #1C1C1E 50%);
}

.preview-header {
  height: 20px;
  background: #E5E5EA;
  border-radius: 4px;
}

.preview-demo.dark .preview-header {
  background: #3A3A3C;
}

.preview-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 6px;
}

.preview-line {
  height: 8px;
  background: #D1D1D6;
  border-radius: 2px;
}

.preview-line.short {
  width: 70%;
}

.preview-demo.dark .preview-line {
  background: #5A5A5E;
}

/* 显示选项 */
.options-list {
  padding: 0;
}

.option-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-bottom: 1px solid var(--color-separator);
}

.option-item:last-child {
  border-bottom: none;
}

.option-info {
  flex: 1;
}

.option-title {
  font-size: 15px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 4px;
}

.option-desc {
  font-size: 13px;
  color: var(--color-text-secondary);
}

.option-control {
  display: flex;
  align-items: center;
}

.option-value {
  display: flex;
  align-items: center;
}

.option-divider {
  height: 1px;
  background: var(--color-separator);
}

/* 切换开关 */
.toggle-switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 28px;
  cursor: pointer;
}

.toggle-switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.toggle-slider {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: 0.3s;
  border-radius: 28px;
}

.toggle-slider::before {
  content: '';
  position: absolute;
  height: 20px;
  width: 20px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  transition: 0.3s;
  border-radius: 50%;
}

.toggle-switch input:checked + .toggle-slider {
  background: linear-gradient(135deg, #34C759, #30B74F);
}

.toggle-switch input:checked + .toggle-slider::before {
  transform: translateX(22px);
}

.theme-badge {
  display: inline-block;
  padding: 6px 14px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.theme-badge.light {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.1), rgba(48, 183, 79, 0.1));
  color: #34C759;
}

.theme-badge.dark {
  background: linear-gradient(135deg, rgba(175, 82, 222, 0.1), rgba(191, 90, 242, 0.1));
  color: #AF52DE;
}

.system-preference {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

/* 快捷操作 */
.quick-buttons {
  padding: 24px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 12px;
}

.quick-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 20px;
  border: 2px solid var(--color-separator);
  border-radius: 12px;
  background: var(--color-bg-secondary);
  cursor: pointer;
  transition: all 0.25s ease;
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.quick-btn:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.quick-btn:active {
  transform: translateY(-2px);
}

.quick-btn svg {
  width: 32px;
  height: 32px;
}

.light-btn:hover {
  border-color: #FF9500;
  background: linear-gradient(135deg, rgba(255, 149, 0, 0.08), rgba(255, 107, 0, 0.08));
  color: #FF9500;
}

.light-btn:hover svg {
  color: #FF9500;
}

.dark-btn:hover {
  border-color: #AF52DE;
  background: linear-gradient(135deg, rgba(175, 82, 222, 0.08), rgba(191, 90, 242, 0.08));
  color: #AF52DE;
}

.dark-btn:hover svg {
  color: #AF52DE;
}

.auto-btn:hover {
  border-color: #5AC8FA;
  background: linear-gradient(135deg, rgba(90, 200, 250, 0.08), rgba(10, 181, 255, 0.08));
  color: #5AC8FA;
}

.auto-btn:hover svg {
  color: #5AC8FA;
}

/* 信息面板 */
.info-card {
  display: flex;
  gap: 16px;
  padding: 20px 24px;
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.06), rgba(48, 183, 79, 0.06));
  border-color: rgba(52, 199, 89, 0.2);
}

.info-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(52, 199, 89, 0.1);
  color: #34C759;
  flex-shrink: 0;
}

.info-icon svg {
  width: 24px;
  height: 24px;
}

.info-content h4 {
  font-size: 15px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0 0 8px 0;
}

.info-content p {
  font-size: 13px;
  color: var(--color-text-secondary);
  margin: 0;
  line-height: 1.6;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .theme-mode-options,
  .preview-container,
  .quick-buttons {
    grid-template-columns: 1fr;
  }

  .option-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .option-control,
  .option-value {
    width: 100%;
    justify-content: flex-start;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>

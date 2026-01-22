<template>
  <div class="settings-page">
    <div class="page-header">
      <div class="header-left">
        <h1>系统设置</h1>
        <p class="subtitle">配置系统参数和偏好设置</p>
      </div>
    </div>

    <!-- 设置分类标签 -->
    <div class="settings-tabs">
      <button 
        v-for="tab in tabs"
        :key="tab.id"
        class="tab-btn"
        :class="{ active: activeTab === tab.id }"
        @click="activeTab = tab.id"
      >
        <Icons :name="tab.icon" size="sm" />
        <span>{{ tab.label }}</span>
      </button>
    </div>

    <!-- 通用设置 -->
    <div v-show="activeTab === 'general'" class="settings-section">
      <h2 class="section-title">通用设置</h2>
      
      <div class="setting-card">
        <div class="setting-item">
          <div class="setting-info">
            <h3>系统语言</h3>
            <p>设置系统显示语言</p>
          </div>
          <select v-model="settings.language" class="select-field">
            <option value="zh-CN">简体中文</option>
            <option value="en-US">English</option>
            <option value="ja-JP">日本語</option>
          </select>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>时区设置</h3>
            <p>设置系统时区</p>
          </div>
          <select v-model="settings.timezone" class="select-field">
            <option value="UTC+8">UTC+8 (北京时间)</option>
            <option value="UTC+9">UTC+9 (东京时间)</option>
            <option value="UTC+0">UTC+0 (格林威治时间)</option>
          </select>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>主题模式</h3>
            <p>选择浅色或深色主题</p>
          </div>
          <div class="theme-selector">
            <button 
              class="theme-btn"
              :class="{ active: settings.theme === 'light' }"
              @click="settings.theme = 'light'"
            >
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <circle cx="12" cy="12" r="5" stroke="currentColor" stroke-width="2"/>
                <path d="M12 1V3M12 21V23M23 12H21M3 12H1M20.49 3.51L19.07 4.93M4.93 19.07L3.51 20.49M20.49 20.49L19.07 19.07M4.93 4.93L3.51 3.51" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
              浅色
            </button>
            <button 
              class="theme-btn"
              :class="{ active: settings.theme === 'dark' }"
              @click="settings.theme = 'dark'"
            >
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
              深色
            </button>
            <button 
              class="theme-btn"
              :class="{ active: settings.theme === 'auto' }"
              @click="settings.theme = 'auto'"
            >
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
                <path d="M12 2A10 10 0 0 0 12 22V2Z" fill="currentColor"/>
              </svg>
              自动
            </button>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>自动刷新</h3>
            <p>页面数据自动刷新</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.autoRefresh }" @click="settings.autoRefresh = !settings.autoRefresh">
            <div class="toggle-thumb"></div>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>动画效果</h3>
            <p>启用页面动画和过渡效果</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.animations }" @click="settings.animations = !settings.animations">
            <div class="toggle-thumb"></div>
          </div>
        </div>
      </div>
    </div>

    <!-- 通知设置 -->
    <div v-show="activeTab === 'notifications'" class="settings-section">
      <h2 class="section-title">通知设置</h2>
      
      <div class="setting-card">
        <div class="setting-item">
          <div class="setting-info">
            <h3>系统通知</h3>
            <p>接收系统更新、维护等重要通知</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.notifySystem }" @click="settings.notifySystem = !settings.notifySystem">
            <div class="toggle-thumb"></div>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>设备通知</h3>
            <p>接收设备状态、告警等通知</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.notifyDevice }" @click="settings.notifyDevice = !settings.notifyDevice">
            <div class="toggle-thumb"></div>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>用户通知</h3>
            <p>接收用户注册、操作等通知</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.notifyUser }" @click="settings.notifyUser = !settings.notifyUser">
            <div class="toggle-thumb"></div>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>桌面通知</h3>
            <p>在桌面显示通知提醒</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.desktopNotify }" @click="settings.desktopNotify = !settings.desktopNotify">
            <div class="toggle-thumb"></div>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>声音提示</h3>
            <p>通知时播放提示音</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.notifySound }" @click="settings.notifySound = !settings.notifySound">
            <div class="toggle-thumb"></div>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>邮件通知</h3>
            <p>通过邮件发送重要通知</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.emailNotify }" @click="settings.emailNotify = !settings.emailNotify">
            <div class="toggle-thumb"></div>
          </div>
        </div>
      </div>
    </div>

    <!-- 安全设置 -->
    <div v-show="activeTab === 'security'" class="settings-section">
      <h2 class="section-title">安全设置</h2>
      
      <div class="setting-card">
        <div class="setting-item">
          <div class="setting-info">
            <h3>双因素认证</h3>
            <p>启用双因素身份验证增强安全性</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.twoFactor }" @click="settings.twoFactor = !settings.twoFactor">
            <div class="toggle-thumb"></div>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>登录保护</h3>
            <p>检测异常登录行为并发送提醒</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.loginProtection }" @click="settings.loginProtection = !settings.loginProtection">
            <div class="toggle-thumb"></div>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>会话超时</h3>
            <p>设置会话超时时间</p>
          </div>
          <select v-model="settings.sessionTimeout" class="select-field">
            <option value="30">30分钟</option>
            <option value="60">1小时</option>
            <option value="120">2小时</option>
            <option value="240">4小时</option>
          </select>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>IP白名单</h3>
            <p>只允许白名单IP访问系统</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.ipWhitelist }" @click="settings.ipWhitelist = !settings.ipWhitelist">
            <div class="toggle-thumb"></div>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>操作日志</h3>
            <p>记录所有用户操作日志</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.operationLog }" @click="settings.operationLog = !settings.operationLog">
            <div class="toggle-thumb"></div>
          </div>
        </div>
      </div>
    </div>

    <!-- 数据设置 -->
    <div v-show="activeTab === 'data'" class="settings-section">
      <h2 class="section-title">数据设置</h2>
      
      <div class="setting-card">
        <div class="setting-item">
          <div class="setting-info">
            <h3>自动备份</h3>
            <p>定期自动备份系统数据</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.autoBackup }" @click="settings.autoBackup = !settings.autoBackup">
            <div class="toggle-thumb"></div>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>备份频率</h3>
            <p>设置数据备份频率</p>
          </div>
          <select v-model="settings.backupFrequency" class="select-field">
            <option value="daily">每天</option>
            <option value="weekly">每周</option>
            <option value="monthly">每月</option>
          </select>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>数据保留期</h3>
            <p>设置数据保留时间</p>
          </div>
          <select v-model="settings.dataRetention" class="select-field">
            <option value="30">30天</option>
            <option value="90">90天</option>
            <option value="180">180天</option>
            <option value="365">1年</option>
          </select>
        </div>

        <div class="setting-item">
          <div class="setting-info">
            <h3>数据压缩</h3>
            <p>自动压缩历史数据节省空间</p>
          </div>
          <div class="toggle-switch" :class="{ active: settings.dataCompression }" @click="settings.dataCompression = !settings.dataCompression">
            <div class="toggle-thumb"></div>
          </div>
        </div>

        <div class="setting-item action-item">
          <button class="action-button primary" @click="backupNow">
            <Icons name="save" size="sm" />
            立即备份
          </button>
          <button class="action-button secondary" @click="exportData">
            <Icons name="download" size="sm" />
            导出数据
          </button>
          <button class="action-button danger" @click="clearCache">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 18px; height: 18px;">
              <polyline points="3 6 5 6 21 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            清除缓存
          </button>
        </div>
      </div>
    </div>

    <!-- 保存按钮 -->
    <div class="save-section">
      <button class="btn secondary" @click="resetSettings">重置为默认</button>
      <button class="btn primary" @click="saveSettings">
        <Icons name="check" size="sm" />
        保存设置
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import Icons from '../components/Icons.vue'

const activeTab = ref('general')

const tabs = [
  { id: 'general', label: '通用设置', icon: 'settings' },
  { id: 'notifications', label: '通知设置', icon: 'bell' },
  { id: 'security', label: '安全设置', icon: 'user' },
  { id: 'data', label: '数据设置', icon: 'save' }
]

const defaultSettings = {
  // 通用设置
  language: 'zh-CN',
  timezone: 'UTC+8',
  theme: 'light',
  autoRefresh: true,
  animations: true,
  
  // 通知设置
  notifySystem: true,
  notifyDevice: true,
  notifyUser: true,
  desktopNotify: false,
  notifySound: true,
  emailNotify: false,
  
  // 安全设置
  twoFactor: false,
  loginProtection: true,
  sessionTimeout: '120',
  ipWhitelist: false,
  operationLog: true,
  
  // 数据设置
  autoBackup: true,
  backupFrequency: 'daily',
  dataRetention: '90',
  dataCompression: true
}

const settings = reactive({ ...defaultSettings })

const loadSettings = () => {
  const saved = localStorage.getItem('systemSettings')
  if (saved) {
    Object.assign(settings, JSON.parse(saved))
  }
}

const saveSettings = () => {
  localStorage.setItem('systemSettings', JSON.stringify(settings))
  alert('设置已保存！')
}

const resetSettings = () => {
  if (confirm('确定要重置所有设置为默认值吗？')) {
    Object.assign(settings, defaultSettings)
    localStorage.removeItem('systemSettings')
    alert('设置已重置为默认值！')
  }
}

const backupNow = () => {
  alert('正在备份数据...')
  setTimeout(() => {
    alert('数据备份成功！')
  }, 1000)
}

const exportData = () => {
  alert('正在导出数据...')
  setTimeout(() => {
    alert('数据导出成功！')
  }, 1000)
}

const clearCache = () => {
  if (confirm('确定要清除缓存吗？这将清除所有临时数据。')) {
    alert('缓存清除成功！')
  }
}

onMounted(() => {
  loadSettings()
})
</script>

<style scoped>
.settings-page {
  padding: 0;
}

.page-header {
  margin-bottom: 28px;
  padding-bottom: 24px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
}

.header-left h1 {
  font-size: 32px;
  font-weight: 900;
  color: var(--color-text-primary);
  margin: 0 0 8px 0;
  letter-spacing: -1px;
}

.subtitle {
  font-size: 15px;
  color: var(--color-text-secondary);
  margin: 0;
  font-weight: 500;
}

/* 设置标签 */
.settings-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 28px;
  overflow-x: auto;
  padding-bottom: 4px;
}

.tab-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: white;
  border: 2px solid rgba(0, 0, 0, 0.08);
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-secondary);
  cursor: pointer;
  transition: all 0.3s;
  white-space: nowrap;
}

.tab-btn:hover {
  background: rgba(52, 199, 89, 0.05);
  border-color: rgba(52, 199, 89, 0.2);
  color: var(--color-text-primary);
}

.tab-btn.active {
  background: linear-gradient(135deg, #34C759, #30B74F);
  border-color: transparent;
  color: white;
  box-shadow: 0 4px 12px rgba(52, 199, 89, 0.3);
}

/* 设置区块 */
.settings-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 20px;
  font-weight: 800;
  color: var(--color-text-primary);
  margin: 0 0 16px 0;
}

.setting-card {
  background: white;
  border-radius: 16px;
  padding: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  transition: all 0.2s;
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-item:hover {
  background: rgba(0, 0, 0, 0.02);
}

.setting-info {
  flex: 1;
}

.setting-info h3 {
  font-size: 15px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0 0 4px 0;
}

.setting-info p {
  font-size: 13px;
  color: var(--color-text-secondary);
  margin: 0;
}

/* 开关 */
.toggle-switch {
  width: 50px;
  height: 28px;
  background: rgba(0, 0, 0, 0.1);
  border-radius: 14px;
  position: relative;
  cursor: pointer;
  transition: all 0.3s;
  flex-shrink: 0;
}

.toggle-switch.active {
  background: linear-gradient(135deg, #34C759, #30B74F);
}

.toggle-thumb {
  width: 24px;
  height: 24px;
  background: white;
  border-radius: 50%;
  position: absolute;
  top: 2px;
  left: 2px;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
}

.toggle-switch.active .toggle-thumb {
  transform: translateX(22px);
}

/* 选择框 */
.select-field {
  padding: 10px 16px;
  border: 2px solid rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  background: white;
  cursor: pointer;
  transition: all 0.2s;
  font-family: inherit;
  min-width: 150px;
}

.select-field:focus {
  outline: none;
  border-color: #34C759;
  background: rgba(52, 199, 89, 0.03);
}

/* 主题选择器 */
.theme-selector {
  display: flex;
  gap: 8px;
}

.theme-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 12px 16px;
  background: white;
  border: 2px solid rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  font-size: 12px;
  font-weight: 600;
  color: var(--color-text-secondary);
  cursor: pointer;
  transition: all 0.2s;
}

.theme-btn svg {
  width: 20px;
  height: 20px;
}

.theme-btn:hover {
  background: rgba(52, 199, 89, 0.05);
  border-color: rgba(52, 199, 89, 0.3);
  color: var(--color-text-primary);
}

.theme-btn.active {
  background: rgba(52, 199, 89, 0.1);
  border-color: #34C759;
  color: #34C759;
}

/* 操作项 */
.setting-item.action-item {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.action-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 18px;
  border: none;
  border-radius: 10px;
  font-size: 13px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s;
}

.action-button.primary {
  background: linear-gradient(135deg, #34C759, #30B74F);
  color: white;
  box-shadow: 0 2px 8px rgba(52, 199, 89, 0.25);
}

.action-button.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(52, 199, 89, 0.35);
}

.action-button.secondary {
  background: linear-gradient(135deg, #5AC8FA, #0AB5FF);
  color: white;
  box-shadow: 0 2px 8px rgba(90, 200, 250, 0.25);
}

.action-button.secondary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(90, 200, 250, 0.35);
}

.action-button.danger {
  background: linear-gradient(135deg, #FF3B30, #FF453A);
  color: white;
  box-shadow: 0 2px 8px rgba(255, 59, 48, 0.25);
}

.action-button.danger:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 59, 48, 0.35);
}

/* 保存区域 */
.save-section {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  padding: 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 14px 28px;
  border: none;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s;
}

.btn.primary {
  background: linear-gradient(135deg, #34C759, #30B74F);
  color: white;
  box-shadow: 0 4px 12px rgba(52, 199, 89, 0.3);
}

.btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(52, 199, 89, 0.4);
}

.btn.secondary {
  background: white;
  color: var(--color-text-primary);
  border: 2px solid rgba(0, 0, 0, 0.1);
}

.btn.secondary:hover {
  background: rgba(0, 0, 0, 0.05);
}
</style>

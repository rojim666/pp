<template>
  <div class="notifications-page">
    <div class="page-header">
      <div class="header-left">
        <h1>通知中心</h1>
        <p class="subtitle">管理和查看所有系统通知</p>
      </div>
      <div class="header-actions">
        <button class="action-btn secondary" @click="markAllRead">
          <Icons name="check" size="sm" />
          全部已读
        </button>
        <button class="action-btn" @click="showSettings = true">
          <Icons name="settings" size="sm" />
          通知设置
        </button>
      </div>
    </div>

    <!-- 过滤标签 -->
    <div class="filter-tabs">
      <button 
        v-for="tab in tabs" 
        :key="tab.type"
        class="tab-btn"
        :class="{ active: activeTab === tab.type }"
        @click="activeTab = tab.type"
      >
        <Icons :name="tab.icon" size="sm" />
        <span>{{ tab.label }}</span>
        <span v-if="getCount(tab.type) > 0" class="count-badge">{{ getCount(tab.type) }}</span>
      </button>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #FF9500, #FF8C00);">
          <Icons name="bell" size="md" />
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ unreadCount }}</div>
          <div class="stat-label">未读通知</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #34C759, #30B74F);">
          <Icons name="check" size="md" />
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ readCount }}</div>
          <div class="stat-label">已读通知</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #5AC8FA, #0AB5FF);">
          <Icons name="list" size="md" />
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ notifications.length }}</div>
          <div class="stat-label">全部通知</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #FF6B40, #FF5E3A);">
          <Icons name="trending" size="md" />
        </div>
        <div class="stat-content">
          <div class="stat-value">+{{ todayCount }}</div>
          <div class="stat-label">今日新增</div>
        </div>
      </div>
    </div>

    <!-- 通知列表 -->
    <div class="notifications-list">
      <div 
        v-for="notification in filteredNotifications" 
        :key="notification.id"
        class="notification-card"
        :class="{ unread: !notification.read, [notification.type]: true }"
        @click="openNotification(notification)"
      >
        <div class="notification-indicator"></div>
        <div class="notification-icon" :class="notification.type">
          <svg v-if="notification.type === 'success'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M20 6L9 17L4 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <svg v-else-if="notification.type === 'warning'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 9V13M12 17H12.01" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          <svg v-else-if="notification.type === 'error'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
            <line x1="15" y1="9" x2="9" y2="15" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            <line x1="9" y1="9" x2="15" y2="15" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          <svg v-else viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2" fill="none"/>
            <path d="M12 8V12M12 16H12.01" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="notification-content">
          <div class="notification-header">
            <h3>{{ notification.title }}</h3>
            <span class="notification-time">{{ notification.time }}</span>
          </div>
          <p class="notification-message">{{ notification.message }}</p>
          <div class="notification-footer">
            <span class="category-tag">{{ notification.category }}</span>
            <div class="notification-actions">
              <button v-if="!notification.read" class="mark-read-btn" @click.stop="markAsRead(notification)">
                标记已读
              </button>
              <button class="delete-btn" @click.stop="deleteNotification(notification.id)">
                删除
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 通知设置弹窗 -->
    <div v-if="showSettings" class="modal-overlay" @click="showSettings = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>通知设置</h2>
          <button class="close-btn" @click="showSettings = false">✕</button>
        </div>
        <div class="modal-body">
          <div class="setting-item">
            <div class="setting-info">
              <h4>系统通知</h4>
              <p>接收系统更新、维护等重要通知</p>
            </div>
            <div class="toggle-switch" :class="{ active: settings.system }" @click="settings.system = !settings.system">
              <div class="toggle-thumb"></div>
            </div>
          </div>
          <div class="setting-item">
            <div class="setting-info">
              <h4>设备通知</h4>
              <p>接收设备状态、告警等通知</p>
            </div>
            <div class="toggle-switch" :class="{ active: settings.device }" @click="settings.device = !settings.device">
              <div class="toggle-thumb"></div>
            </div>
          </div>
          <div class="setting-item">
            <div class="setting-info">
              <h4>用户通知</h4>
              <p>接收用户注册、操作等通知</p>
            </div>
            <div class="toggle-switch" :class="{ active: settings.user }" @click="settings.user = !settings.user">
              <div class="toggle-thumb"></div>
            </div>
          </div>
          <div class="setting-item">
            <div class="setting-info">
              <h4>桌面通知</h4>
              <p>在桌面显示通知提醒</p>
            </div>
            <div class="toggle-switch" :class="{ active: settings.desktop }" @click="settings.desktop = !settings.desktop">
              <div class="toggle-thumb"></div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn secondary" @click="showSettings = false">取消</button>
          <button class="btn primary" @click="saveSettings">保存设置</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import Icons from '../components/Icons.vue'
import { useNotificationStore } from '../stores/notification'

const activeTab = ref('all')
const showSettings = ref(false)
const notificationStore = useNotificationStore()
const { notifications, unreadCount, readCount, todayCount } = storeToRefs(notificationStore)

onMounted(() => {
  notificationStore.fetchNotifications()
})

const tabs = [
  { type: 'all', label: '全部', icon: 'list' },
  { type: 'unread', label: '未读', icon: 'bell' },
  { type: 'system', label: '系统', icon: 'settings' },
  { type: 'device', label: '设备', icon: 'plug' },
  { type: 'business', label: '业务', icon: 'file-text' }
]

const settings = ref({
  system: true,
  device: true,
  user: true,
  desktop: false
})

const filteredNotifications = computed(() => {
  if (activeTab.value === 'all') {
    return notifications.value
  } else if (activeTab.value === 'unread') {
    return notifications.value.filter(n => !n.read)
  } else {
    return notifications.value.filter(n => n.tab === activeTab.value)
  }
})

const getCount = (type) => {
  if (type === 'all') return notifications.value.length
  if (type === 'unread') return unreadCount.value
  return notifications.value.filter(n => n.tab === type).length
}

const markAllRead = () => {
  notificationStore.markAllRead()
}

const markAsRead = (notification) => {
  notificationStore.markAsRead(notification.id)
}

const deleteNotification = (id) => {
  if (confirm('确定要删除这条通知吗？')) {
    notificationStore.deleteNotification(id)
  }
}

const openNotification = (notification) => {
  notificationStore.markAsRead(notification.id)
  console.log('打开通知详情:', notification)
}

const saveSettings = () => {
  console.log('保存设置:', settings.value)
  showSettings.value = false
  alert('通知设置已保存！')
}
</script>

<style scoped>
.notifications-page {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
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

.header-actions {
  display: flex;
  gap: 12px;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: linear-gradient(135deg, #34C759, #30B74F);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(52, 199, 89, 0.25);
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(52, 199, 89, 0.35);
}

.action-btn.secondary {
  background: white;
  color: var(--color-text-primary);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border: 2px solid rgba(0, 0, 0, 0.06);
}

.action-btn.secondary:hover {
  background: #f8f8f8;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

/* 过滤标签 */
.filter-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  overflow-x: auto;
  padding-bottom: 4px;
}

.tab-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 18px;
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

.count-badge {
  background: rgba(255, 255, 255, 0.25);
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
  font-weight: 700;
  min-width: 22px;
  text-align: center;
}

.tab-btn.active .count-badge {
  background: rgba(255, 255, 255, 0.3);
}

.tab-btn:not(.active) .count-badge {
  background: rgba(0, 0, 0, 0.08);
  color: var(--color-text-secondary);
}

/* 统计卡片 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 28px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 24px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 900;
  color: var(--color-text-primary);
  line-height: 1;
  margin-bottom: 6px;
}

.stat-label {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

/* 通知列表 */
.notifications-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.notification-card {
  display: flex;
  gap: 16px;
  padding: 24px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s;
  cursor: pointer;
  position: relative;
  border-left: 4px solid transparent;
}

.notification-card:hover {
  transform: translateX(4px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.notification-card.unread {
  background: linear-gradient(90deg, rgba(52, 199, 89, 0.03), white);
}

.notification-card.unread .notification-indicator {
  position: absolute;
  left: -4px;
  top: 0;
  bottom: 0;
  width: 4px;
  background: linear-gradient(135deg, #34C759, #30B74F);
  border-radius: 0 2px 2px 0;
}

.notification-card.success {
  border-left-color: #34C759;
}

.notification-card.warning {
  border-left-color: #FF9500;
}

.notification-card.error {
  border-left-color: #FF3B30;
}

.notification-card.info {
  border-left-color: #5AC8FA;
}

.notification-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.notification-icon.success {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.12), rgba(48, 209, 88, 0.12));
  color: #34C759;
}

.notification-icon.warning {
  background: linear-gradient(135deg, rgba(255, 149, 0, 0.12), rgba(255, 107, 0, 0.12));
  color: #FF9500;
}

.notification-icon.error {
  background: linear-gradient(135deg, rgba(255, 59, 48, 0.12), rgba(255, 69, 58, 0.12));
  color: #FF3B30;
}

.notification-icon.info {
  background: linear-gradient(135deg, rgba(90, 200, 250, 0.12), rgba(10, 181, 255, 0.12));
  color: #5AC8FA;
}

.notification-icon svg {
  width: 24px;
  height: 24px;
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 8px;
}

.notification-header h3 {
  font-size: 16px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0;
  flex: 1;
}

.notification-time {
  font-size: 12px;
  color: var(--color-text-tertiary);
  font-weight: 600;
  white-space: nowrap;
}

.notification-message {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.6;
  margin: 0 0 12px 0;
}

.notification-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.category-tag {
  font-size: 12px;
  padding: 4px 12px;
  border-radius: 12px;
  background: rgba(0, 0, 0, 0.06);
  color: var(--color-text-secondary);
  font-weight: 600;
}

.notification-actions {
  display: flex;
  gap: 8px;
}

.mark-read-btn,
.delete-btn {
  padding: 6px 14px;
  border: none;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.mark-read-btn {
  background: rgba(52, 199, 89, 0.1);
  color: #34C759;
}

.mark-read-btn:hover {
  background: rgba(52, 199, 89, 0.2);
}

.delete-btn {
  background: rgba(255, 59, 48, 0.1);
  color: #FF3B30;
}

.delete-btn:hover {
  background: rgba(255, 59, 48, 0.2);
}

/* 模态框 */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.modal-content {
  background: white;
  border-radius: 20px;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow: hidden;
  animation: slideUp 0.3s;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
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

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
}

.modal-header h2 {
  font-size: 20px;
  font-weight: 800;
  color: var(--color-text-primary);
  margin: 0;
}

.close-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 50%;
  font-size: 18px;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  background: rgba(0, 0, 0, 0.1);
  transform: rotate(90deg);
}

.modal-body {
  padding: 24px;
  max-height: 60vh;
  overflow-y: auto;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-info h4 {
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

.toggle-switch {
  width: 50px;
  height: 28px;
  background: rgba(0, 0, 0, 0.1);
  border-radius: 14px;
  position: relative;
  cursor: pointer;
  transition: all 0.3s;
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

.modal-footer {
  display: flex;
  gap: 12px;
  padding: 20px 24px;
  border-top: 2px solid rgba(0, 0, 0, 0.06);
}

.btn {
  flex: 1;
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  font-size: 14px;
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

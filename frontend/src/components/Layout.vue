<template>
  <div class="layout">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="logo">
        <div class="logo-icon">
          <svg viewBox="0 0 48 48" xmlns="http://www.w3.org/2000/svg">
            <defs>
              <linearGradient id="logoGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" style="stop-color:#34C759;stop-opacity:1" />
                <stop offset="100%" style="stop-color:#30B74F;stop-opacity:1" />
              </linearGradient>
            </defs>
            <!-- 充电插头外壳 -->
            <rect x="14" y="8" width="20" height="28" rx="3" fill="url(#logoGradient)" />
            <!-- 充电插头接口 -->
            <rect x="18" y="12" width="4" height="8" rx="1" fill="white" opacity="0.9" />
            <rect x="26" y="12" width="4" height="8" rx="1" fill="white" opacity="0.9" />
            <!-- 闪电标志 -->
            <path d="M24 24 L28 28 L25 28 L26 32 L22 28 L25 28 Z" fill="#FFD60A" />
            <!-- 充电线 -->
            <path d="M24 36 Q24 42, 24 44" stroke="url(#logoGradient)" stroke-width="3" stroke-linecap="round" fill="none" />
            <!-- 底座 -->
            <ellipse cx="24" cy="44" rx="8" ry="2" fill="url(#logoGradient)" opacity="0.3" />
          </svg>
        </div>
        <span class="logo-text">充电桩管理</span>
      </div>
      
      <nav class="menu">
        <!-- 管理员可见菜单 -->
        <div class="menu-group" v-if="isAdmin">
          <div class="menu-title">
            <Icons name="settings" size="sm" />
            <span>充电资费设置</span>
          </div>
          <router-link 
            v-for="item in settingsMenu" 
            :key="item.path" 
            :to="item.path" 
            class="menu-item"
            active-class="active"
          >
            <span class="menu-icon">
              <Icons :name="item.icon" size="md" />
            </span>
            <span>{{ item.name }}</span>
          </router-link>
        </div>

        <div class="menu-group">
          <div class="menu-title">
            <Icons name="chart" size="sm" />
            <span>充电服务</span>
          </div>
          <router-link 
            v-for="item in chargingMenu" 
            :key="item.path" 
            :to="item.path" 
            class="menu-item"
            active-class="active"
          >
            <span class="menu-icon">
              <Icons :name="item.icon" size="md" />
            </span>
            <span>{{ item.name }}</span>
          </router-link>
        </div>

        <!-- 个人服务菜单 -->
        <div class="menu-group">
          <div class="menu-title">
            <Icons name="user" size="sm" />
            <span>个人服务</span>
          </div>
          <router-link to="/profile" class="menu-item" active-class="active">
            <span class="menu-icon">
              <Icons name="user" size="md" />
            </span>
            <span>个人中心</span>
          </router-link>
          <router-link to="/recharge" class="menu-item" active-class="active">
            <span class="menu-icon">
              <Icons name="money" size="md" />
            </span>
            <span>充值中心</span>
          </router-link>
        </div>

        <!-- 管理员可见菜单 -->
        <div class="menu-group" v-if="isAdmin">
          <div class="menu-title">
            <Icons name="user" size="sm" />
            <span>用户信息管理</span>
          </div>
          <router-link to="/user" class="menu-item" active-class="active">
            <span class="menu-icon">
              <Icons name="user" size="md" />
            </span>
            <span>用户信息管理</span>
          </router-link>
        </div>

        <!-- 管理员可见菜单 -->
        <div class="menu-group" v-if="isAdmin">
          <div class="menu-title">
            <Icons name="money" size="sm" />
            <span>充电费用统计</span>
          </div>
          <router-link to="/statistics" class="menu-item" active-class="active">
            <span class="menu-icon">
              <Icons name="money" size="md" />
            </span>
            <span>充电费用统计</span>
          </router-link>
        </div>

        <!-- 管理员可见菜单 -->
        <div class="menu-group" v-if="isAdmin">
          <div class="menu-title">
            <Icons name="search" size="sm" />
            <span>系统健康巡查</span>
          </div>
          <router-link to="/system" class="menu-item" active-class="active">
            <span class="menu-icon">
              <Icons name="search" size="md" />
            </span>
            <span>系统健康巡查</span>
          </router-link>
        </div>
      </nav>

    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <header class="header">
        <div class="breadcrumb">
          <span>充电设施</span>
          <span class="separator">/</span>
          <span>{{ currentPageTitle }}</span>
        </div>
        <div class="header-actions">
          <!-- 余额显示 -->
          <div class="balance-display" @click="$router.push('/recharge')" title="点击充值">
            <span class="balance-label">余额</span>
            <span class="balance-value">¥{{ currentUser?.balance?.toFixed(2) || '0.00' }}</span>
            <div class="add-btn">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 5v14M5 12h14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </div>
          </div>

          <!-- 通知按钮 -->
          <div class="action-wrapper">
            <button class="icon-btn" :class="{ active: showNotifications }" @click="toggleNotifications" title="通知">
              <Icons name="bell" size="md" />
              <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span>
            </button>
            
            <!-- 通知下拉面板 -->
            <div v-if="showNotifications" class="dropdown notifications-dropdown">
              <div class="dropdown-header">
                <h4>通知中心</h4>
                <div class="header-actions">
                  <button class="settings-icon-btn" @click="openNotificationSettings" title="通知设置">
                    <Icons name="settings" size="sm" />
                  </button>
                  <button class="clear-btn" @click="clearNotifications">全部已读</button>
                </div>
              </div>
              
              <!-- 通知分类标签 -->
              <div class="notification-tabs">
                <button 
                  v-for="tab in notificationTabs" 
                  :key="tab.type"
                  class="tab-btn"
                  :class="{ active: activeNotificationTab === tab.type }"
                  @click="activeNotificationTab = tab.type"
                >
                  {{ tab.label }}
                  <span v-if="getTabCount(tab.type) > 0" class="tab-count">{{ getTabCount(tab.type) }}</span>
                </button>
              </div>
              
              <div class="notifications-list">
                <div 
                  v-for="notification in filteredNotifications" 
                  :key="notification.id"
                  class="notification-item"
                  :class="{ unread: !notification.read }"
                  @click="markAsRead(notification)"
                >
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
                      <div class="notification-title">{{ notification.title }}</div>
                      <button v-if="!notification.read" class="mark-read-btn" @click.stop="markAsRead(notification)" title="标记已读">
                        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                          <circle cx="12" cy="12" r="4" fill="currentColor"/>
                        </svg>
                      </button>
                    </div>
                    <div class="notification-message">{{ notification.message }}</div>
                    <div class="notification-footer">
                      <span class="notification-time">{{ notification.time }}</span>
                      <span v-if="notification.category" class="notification-category">{{ notification.category }}</span>
                    </div>
                  </div>
                </div>
                <div v-if="filteredNotifications.length === 0" class="empty-state">
                  <svg viewBox="0 0 64 64" xmlns="http://www.w3.org/2000/svg">
                    <circle cx="32" cy="32" r="28" fill="#f0f0f0"/>
                    <path d="M32 20v16M32 44h.01" stroke="#999" stroke-width="3" stroke-linecap="round"/>
                  </svg>
                  <p>暂无{{ activeNotificationTab === 'all' ? '' : notificationTabs.find(t => t.type === activeNotificationTab)?.label }}通知</p>
                </div>
              </div>
              <div class="dropdown-footer">
                <router-link to="/notifications" class="view-all-btn" @click="showNotifications = false">
                  查看所有通知
                  <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 16px; height: 16px; margin-left: 4px;">
                    <path d="M5 12h14M12 5l7 7-7 7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </router-link>
              </div>
            </div>
          </div>

          <!-- 快捷菜单按钮 -->
          <div class="action-wrapper">
            <button class="icon-btn" :class="{ active: showQuickMenu }" @click="toggleQuickMenu" title="快捷菜单">
              <Icons name="home" size="md" />
            </button>
            
            <!-- 快捷菜单下拉面板 -->
            <div v-if="showQuickMenu" class="dropdown quick-menu-dropdown">
              <div class="dropdown-header">
                <h4>快捷菜单</h4>
                <button class="customize-btn" @click="customizeQuickMenu" title="自定义">
                  <Icons name="settings" size="sm" />
                </button>
              </div>
              
              <!-- 常用功能 -->
              <div class="quick-section">
                <div class="section-title">常用功能</div>
                <div class="quick-menu-grid">
                  <router-link 
                    v-for="item in quickMenuItems" 
                    :key="item.path"
                    :to="item.path"
                    class="quick-menu-item"
                    @click="showQuickMenu = false"
                  >
                    <div class="quick-icon" :style="{ background: item.color }">
                      <Icons :name="item.icon" size="md" />
                    </div>
                    <div class="quick-name">{{ item.name }}</div>
                  </router-link>
                </div>
              </div>
              
              <!-- 快捷操作 -->
              <div class="quick-section">
                <div class="section-title">快捷操作</div>
                <div class="quick-actions">
                  <button 
                    v-for="action in quickActions" 
                    :key="action.id"
                    class="action-item"
                    @click="handleQuickAction(action.id)"
                  >
                    <div class="action-icon" :style="{ background: action.color }">
                      <Icons :name="action.icon" size="sm" />
                    </div>
                    <span>{{ action.name }}</span>
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="action-arrow">
                      <path d="M9 18l6-6-6-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </button>
                </div>
              </div>
              
              <!-- 系统状态 -->
              <div class="quick-section">
                <div class="section-title">系统状态</div>
                <div class="system-stats">
                  <div class="stat-item">
                    <div class="stat-label">在线设备</div>
                    <div class="stat-value" style="color: #34C759;">{{ systemStatus.onlineDevices }}</div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-label">充电中</div>
                    <div class="stat-value" style="color: #FF9500;">{{ systemStatus.charging }}</div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-label">今日收益</div>
                    <div class="stat-value" style="color: #5AC8FA;">¥{{ systemStatus.todayRevenue }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 用户菜单按钮 -->
          <div class="action-wrapper">
            <button class="icon-btn user-btn" :class="{ active: showUserMenu }" @click="toggleUserMenu" title="用户菜单">
              <img :src="currentUser?.avatar || `https://ui-avatars.com/api/?name=${currentUser?.username || 'User'}&background=34C759&color=fff`" alt="用户头像" class="user-avatar" />
            </button>
            
            <!-- 用户菜单下拉面板 -->
            <div v-if="showUserMenu" class="dropdown user-menu-dropdown">
              <div class="user-profile">
                <img :src="currentUser?.avatar || `https://ui-avatars.com/api/?name=${currentUser?.username || 'User'}&background=34C759&color=fff&size=80`" alt="用户头像" class="profile-avatar" />
                <div class="profile-info">
                  <div class="profile-name">{{ currentUser?.realName || currentUser?.username || '未登录' }}</div>
                  <div class="profile-role">{{ getRoleName(currentUser?.role) }}</div>
                  <div class="profile-email">{{ currentUser?.email || '未绑定邮箱' }}</div>
                </div>
                <button class="edit-profile-btn" @click="editProfile" title="编辑资料">
                  <Icons name="settings" size="sm" />
                </button>
              </div>
              
              <!-- 用户统计 -->
              <div class="user-stats">
                <div class="user-stat-item">
                  <div class="stat-number">{{ userStats.loginDays }}</div>
                  <div class="stat-label">连续登录</div>
                </div>
                <div class="stat-divider"></div>
                <div class="user-stat-item">
                  <div class="stat-number">{{ userStats.operations }}</div>
                  <div class="stat-label">今日操作</div>
                </div>
                <div class="stat-divider"></div>
                <div class="user-stat-item">
                  <div class="stat-number">{{ userStats.messages }}</div>
                  <div class="stat-label">未读消息</div>
                </div>
              </div>
              
              <div class="menu-divider"></div>
              
              <div class="user-menu-list">
                <router-link to="/profile" class="user-menu-item" @click="showUserMenu = false">
                  <div class="menu-item-icon">
                    <Icons name="user" size="sm" />
                  </div>
                  <div class="menu-item-content">
                    <div class="menu-item-title">个人中心</div>
                    <div class="menu-item-desc">查看和编辑个人信息</div>
                  </div>
                  <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="menu-arrow">
                    <path d="M9 18l6-6-6-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </router-link>
                
                <router-link to="/settings-system" class="user-menu-item" @click="showUserMenu = false">
                  <div class="menu-item-icon">
                    <Icons name="settings" size="sm" />
                  </div>
                  <div class="menu-item-content">
                    <div class="menu-item-title">系统设置</div>
                    <div class="menu-item-desc">配置系统参数</div>
                  </div>
                  <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="menu-arrow">
                    <path d="M9 18l6-6-6-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </router-link>
                
                <router-link to="/statistics" class="user-menu-item" @click="showUserMenu = false">
                  <div class="menu-item-icon">
                    <Icons name="chart" size="sm" />
                  </div>
                  <div class="menu-item-content">
                    <div class="menu-item-title">数据统计</div>
                    <div class="menu-item-desc">查看运营数据</div>
                  </div>
                  <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="menu-arrow">
                    <path d="M9 18l6-6-6-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </router-link>
                
                <button class="user-menu-item" @click="toggleTheme">
                  <div class="menu-item-icon">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 18px; height: 18px;">
                      <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </div>
                  <div class="menu-item-content">
                    <div class="menu-item-title">主题切换</div>
                    <div class="menu-item-desc">{{ isDarkMode ? '深色模式' : '浅色模式' }}</div>
                  </div>
                  <div class="theme-toggle">
                    <div class="toggle-track" :class="{ active: isDarkMode }">
                      <div class="toggle-thumb"></div>
                    </div>
                  </div>
                </button>
              </div>
              
              <div class="menu-divider"></div>
              
              <button class="user-menu-item logout" @click="handleLogout">
                <div class="menu-item-icon logout-icon">
                  <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 18px; height: 18px;">
                    <path d="M9 21H5C4.46957 21 3.96086 20.7893 3.58579 20.4142C3.21071 20.0391 3 19.5304 3 19V5C3 4.46957 3.21071 3.96086 3.58579 3.58579C3.96086 3.21071 4.46957 3 5 3H9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M16 17L21 12L16 7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M21 12H9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
                <span>退出登录</span>
              </button>
            </div>
          </div>
        </div>
      </header>

      <div class="content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'
import Icons from './Icons.vue'
import { useUserStore } from '../stores/user'
import { useThemeStore } from '../stores/theme'
import { useNotificationStore } from '../stores/notification'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const themeStore = useThemeStore()
const notificationStore = useNotificationStore()
const { notifications, unreadCount } = storeToRefs(notificationStore)
const { currentUser, isAdmin } = storeToRefs(userStore)

onMounted(() => {
  notificationStore.fetchNotifications()
  if (!currentUser.value) {
    userStore.initAuth()
  }
  // 刷新用户信息以获取最新余额
  if (currentUser.value) {
    userStore.fetchCurrentUser()
  }
})

const settingsMenu = [
  { path: '/settings', name: '充电资费设置', icon: 'document' },
  { path: '/device', name: '充电设备状态', icon: 'plug' },
  { path: '/usage', name: '充电使用统计', icon: 'trending' }
]

const chargingMenu = [
  { path: '/charging', name: '充电桩列表', icon: 'zap' },
  { path: '/records', name: '充电记录查看', icon: 'list' }
]

const currentPageTitle = computed(() => {
  return route.meta.title || '充电桩记录'
})

// 通知功能
const showNotifications = ref(false)
const activeNotificationTab = ref('all')
const notificationTabs = [
  { type: 'all', label: '全部' },
  { type: 'unread', label: '未读' },
  { type: 'system', label: '系统' },
  { type: 'device', label: '设备' },
  { type: 'user', label: '用户' }
]

// notifications data is now managed by Pinia store

const filteredNotifications = computed(() => {
  if (activeNotificationTab.value === 'all') {
    return notifications.value
  } else if (activeNotificationTab.value === 'unread') {
    return notifications.value.filter(n => !n.read)
  } else {
    return notifications.value.filter(n => n.tab === activeNotificationTab.value)
  }
})

const getTabCount = (type) => {
  if (type === 'all') return notifications.value.length
  if (type === 'unread') return notifications.value.filter(n => !n.read).length
  return notifications.value.filter(n => n.tab === type).length
}

// unreadCount is imported from store

const toggleNotifications = () => {
  showNotifications.value = !showNotifications.value
  showQuickMenu.value = false
  showUserMenu.value = false
}

const clearNotifications = () => {
  notificationStore.markAllRead()
}

const markAsRead = (notification) => {
  notificationStore.markAsRead(notification.id)
}

const openNotificationSettings = () => {
  console.log('打开通知设置')
  showNotifications.value = false
  router.push('/settings')
}

// 快捷菜单功能
const showQuickMenu = ref(false)
const quickMenuItems = [
  { path: '/charging', name: '充电桩管理', icon: 'zap', color: 'linear-gradient(135deg, #34C759, #30B74F)' },
  { path: '/device', name: '设备状态', icon: 'plug', color: 'linear-gradient(135deg, #32ADE6, #00A8E1)' },
  { path: '/user', name: '用户管理', icon: 'user', color: 'linear-gradient(135deg, #5AC8FA, #0AB5FF)' },
  { path: '/statistics', name: '费用统计', icon: 'money', color: 'linear-gradient(135deg, #FF9500, #FF8C00)' },
  { path: '/settings', name: '资费设置', icon: 'settings', color: 'linear-gradient(135deg, #FF6B40, #FF5E3A)' },
  { path: '/system', name: '系统巡查', icon: 'search', color: 'linear-gradient(135deg, #00C7BE, #00A8E1)' }
]

const quickActions = [
  { id: 'addStation', name: '新建充电桩', icon: 'zap', color: 'linear-gradient(135deg, #34C759, #30B74F)' },
  { id: 'exportData', name: '导出数据', icon: 'download', color: 'linear-gradient(135deg, #5AC8FA, #0AB5FF)' },
  { id: 'systemCheck', name: '系统检测', icon: 'search', color: 'linear-gradient(135deg, #FF9500, #FF8C00)' },
  { id: 'backup', name: '数据备份', icon: 'save', color: 'linear-gradient(135deg, #AF52DE, #BF5AF2)' }
]

const systemStatus = ref({
  onlineDevices: 45,
  charging: 12,
  todayRevenue: '12,458'
})

const toggleQuickMenu = () => {
  showQuickMenu.value = !showQuickMenu.value
  showNotifications.value = false
  showUserMenu.value = false
}

const handleQuickAction = (actionId) => {
  console.log('执行快捷操作:', actionId)
  showQuickMenu.value = false
  // 这里可以添加具体的操作逻辑
  switch(actionId) {
    case 'addStation':
      router.push('/charging?action=add')
      break
    case 'exportData':
      alert('正在导出数据...')
      break
    case 'systemCheck':
      router.push('/system')
      break
    case 'backup':
      alert('正在备份数据...')
      break
  }
}

const customizeQuickMenu = () => {
  console.log('自定义快捷菜单')
  alert('快捷菜单自定义功能开发中...')
}

// 用户菜单功能
const showUserMenu = ref(false)

// 监听主题变化
const isDarkMode = computed(() => themeStore.actualTheme === 'dark')

const userStats = computed(() => ({
  loginDays: 1, // 暂无此数据，默认为1
  operations: notificationStore.todayCount, // 使用今日通知数作为今日操作数
  messages: unreadCount.value
}))

const getRoleName = (role) => {
  const map = {
    'admin': '系统管理员',
    'operator': '运营商',
    'user': '普通用户'
  }
  return map[role] || role || '访客'
}

const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value
  showNotifications.value = false
  showQuickMenu.value = false
}

const editProfile = () => {
  console.log('编辑个人资料')
  showUserMenu.value = false
  router.push('/user')
}

const toggleTheme = () => {
  themeStore.toggleTheme()
}

const handleLogout = () => {
  if (confirm('确定要退出登录吗？')) {
    showUserMenu.value = false
    userStore.logout()  // 清除登录状态
    router.push('/login')
  }
}
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
  background: var(--color-bg-secondary);
  position: relative;
}

.layout::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(52, 199, 89, 0.03) 0%, transparent 70%);
  pointer-events: none;
  animation: pulse 8s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 0.5; transform: scale(1); }
  50% { opacity: 0.8; transform: scale(1.1); }
}

.sidebar {
  width: 280px;
  background: var(--color-bg-primary);
  border-right: 1px solid var(--color-separator);
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  box-shadow: 4px 0 24px rgba(0, 0, 0, 0.04);
  position: relative;
  z-index: 10;
  transition: background-color var(--transition-duration) ease;
}

.logo {
  padding: 24px 20px;
  display: flex;
  align-items: center;
  gap: 14px;
  font-size: 20px;
  font-weight: 600;
  color: var(--color-text-primary);
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  letter-spacing: -0.5px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.logo:hover {
  padding-left: 24px;
}

.logo-icon {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.12) 0%, rgba(48, 183, 79, 0.12) 100%);
  border-radius: 14px;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  box-shadow: 0 4px 12px rgba(52, 199, 89, 0.15);
}

.logo-icon::before {
  content: '';
  position: absolute;
  inset: -2px;
  border-radius: 16px;
  background: linear-gradient(135deg, #34C759, #30B74F);
  opacity: 0;
  transition: opacity 0.3s;
}

.logo:hover .logo-icon::before {
  opacity: 0.1;
}

.logo-icon svg {
  width: 32px;
  height: 32px;
  filter: drop-shadow(0 2px 8px rgba(52, 199, 89, 0.4));
  position: relative;
  z-index: 1;
}

.logo:hover .logo-icon {
  transform: scale(1.08) rotate(5deg);
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.18) 0%, rgba(48, 183, 79, 0.18) 100%);
  box-shadow: 0 6px 20px rgba(52, 199, 89, 0.25);
}

.logo-text {
  background: linear-gradient(135deg, #34C759 0%, #30B74F 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-weight: 700;
  position: relative;
}

.menu {
  flex: 1;
  padding: 16px 0;
  overflow-y: auto;
}

.menu-group {
  margin-bottom: 24px;
  animation: slideIn 0.4s ease-out backwards;
}

.menu-group:nth-child(1) { animation-delay: 0.05s; }
.menu-group:nth-child(2) { animation-delay: 0.1s; }
.menu-group:nth-child(3) { animation-delay: 0.15s; }
.menu-group:nth-child(4) { animation-delay: 0.2s; }
.menu-group:nth-child(5) { animation-delay: 0.25s; }

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.menu-title {
  padding: 10px 20px;
  font-size: 11px;
  font-weight: 700;
  color: var(--color-text-secondary);
  text-transform: uppercase;
  letter-spacing: 1px;
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.menu-title .icon {
  opacity: 0.6;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 20px;
  margin: 3px 12px;
  color: var(--color-text-primary);
  text-decoration: none;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  font-size: 14px;
  border-radius: 10px;
  font-weight: 500;
  position: relative;
  overflow: hidden;
}

.menu-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 0;
  background: linear-gradient(135deg, #34C759, #30B74F);
  border-radius: 0 2px 2px 0;
  transition: height 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.menu-item:hover::before {
  height: 60%;
}

.menu-item:hover {
  background: linear-gradient(90deg, rgba(52, 199, 89, 0.08) 0%, rgba(52, 199, 89, 0.04) 100%);
  color: #34C759;
  transform: translateX(4px);
  padding-left: 24px;
}

.menu-item.active {
  background: linear-gradient(135deg, #34C759 0%, #30B74F 100%);
  color: white;
  font-weight: 600;
  box-shadow: 0 4px 16px rgba(52, 199, 89, 0.35), 
              0 0 0 1px rgba(255, 255, 255, 0.1) inset;
  transform: translateX(0);
  padding-left: 20px;
}

.menu-item.active::before {
  display: none;
}

.menu-item.active:hover {
  transform: translateX(2px) scale(1.02);
  box-shadow: 0 6px 20px rgba(52, 199, 89, 0.4),
              0 0 0 1px rgba(255, 255, 255, 0.15) inset;
}

.menu-item.active .menu-icon,
.menu-item.active .menu-icon svg {
  color: white !important;
  fill: white !important;
}

.menu-icon {
  font-size: 20px;
  width: 22px;
  text-align: center;
  transition: transform 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.menu-item:hover .menu-icon {
  transform: scale(1.1);
}

.menu-item.active .menu-icon {
  transform: scale(1.05);
}

.user-info {
  padding: 20px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  display: none;
}

.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  border: 3px solid rgba(52, 199, 89, 0.15);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s;
}

.user-name {
  flex: 1;
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.login-btn {
  padding: 10px 20px;
  background: linear-gradient(135deg, #34C759 0%, #30B74F 100%);
  color: white;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-size: 13px;
  font-weight: 700;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 12px rgba(52, 199, 89, 0.25);
}

.login-btn:hover {
  box-shadow: 0 6px 20px rgba(52, 199, 89, 0.35);
  transform: translateY(-2px);
}

.login-btn:active {
  transform: translateY(0) scale(0.98);
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  position: relative;
}

.header {
  background: var(--color-bg-primary);
  padding: 18px 28px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid var(--color-separator);
  box-shadow: 0 2px 16px rgba(0, 0, 0, 0.03);
  position: relative;
  z-index: 5;
  transition: background-color var(--transition-duration) ease;
}

.breadcrumb {
  font-size: 14px;
  color: var(--color-text-secondary);
  font-weight: 600;
  letter-spacing: 0.2px;
}

.breadcrumb span:last-child {
  color: var(--color-primary);
  font-weight: 700;
}

.separator {
  margin: 0 10px;
  color: var(--color-text-tertiary);
  font-weight: 400;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.icon-btn {
  width: 44px;
  height: 44px;
  border: none;
  background: var(--color-bg-secondary);
  border-radius: 12px;
  cursor: pointer;
  font-size: 20px;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.icon-btn::before {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: 12px;
  background: linear-gradient(135deg, #34C759, #30B74F);
  opacity: 0;
  transition: opacity 0.25s;
}

.icon-btn:hover {
  background: rgba(52, 199, 89, 0.1);
  color: #34C759;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.icon-btn:hover::before {
  opacity: 0.05;
}

.icon-btn:active {
  transform: translateY(0) scale(0.95);
}

.content {
  flex: 1;
  overflow-y: auto;
  padding: 28px;
  animation: contentFadeIn 0.4s ease-out;
  position: relative;
  background-color: var(--color-bg-secondary);
  transition: background-color var(--transition-duration) ease;
}

@keyframes contentFadeIn {
  from {
    opacity: 0;
    transform: translateY(15px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 自定义滚动条 */
.sidebar::-webkit-scrollbar,
.menu::-webkit-scrollbar,
.content::-webkit-scrollbar {
  width: 8px;
}

.sidebar::-webkit-scrollbar-track,
.menu::-webkit-scrollbar-track,
.content::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.02);
  border-radius: 4px;
}

.sidebar::-webkit-scrollbar-thumb,
.menu::-webkit-scrollbar-thumb,
.content::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.15);
  border-radius: 4px;
  transition: background 0.2s;
}

.sidebar::-webkit-scrollbar-thumb:hover,
.menu::-webkit-scrollbar-thumb:hover,
.content::-webkit-scrollbar-thumb:hover {
  background: rgba(0, 0, 0, 0.25);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    width: 240px;
  }

  .logo {
    padding: 20px 16px;
  }

  .menu-item {
    font-size: 13px;
    padding: 10px 16px;
  }

  .content {
    padding: 20px;
  }
}

/* 新增：下拉面板样式 */
.action-wrapper {
  position: relative;
}

.icon-btn.active {
  background: rgba(52, 199, 89, 0.15);
  color: #34C759;
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(52, 199, 89, 0.25);
}

.icon-btn.active::before {
  opacity: 0.1;
}

.icon-btn.active .icon,
.icon-btn.active svg {
  color: #34C759 !important;
  fill: #34C759 !important;
}

.badge {
  position: absolute;
  top: -4px;
  right: -4px;
  background: linear-gradient(135deg, #FF9500, #FF8C00);
  color: white;
  font-size: 10px;
  font-weight: 700;
  padding: 2px 6px;
  border-radius: 10px;
  line-height: 1;
  box-shadow: 0 2px 8px rgba(255, 149, 0, 0.4);
  min-width: 18px;
  text-align: center;
}

.dropdown {
  position: absolute;
  top: calc(100% + 12px);
  right: 0;
  background: var(--color-bg-primary);
  border-radius: 16px;
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.15);
  border: 1px solid var(--color-separator);
  z-index: 1000;
  animation: dropdownSlide 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  min-width: 320px;
  transition: background-color var(--transition-duration) ease;
}

@keyframes dropdownSlide {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.dropdown-header {
  padding: 16px 20px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dropdown-header h4 {
  margin: 0;
  font-size: 16px;
  font-weight: 800;
  color: var(--color-text-primary);
  letter-spacing: -0.3px;
}

.header-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}

.settings-icon-btn {
  background: none;
  border: none;
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
  color: var(--color-text-secondary);
}

.settings-icon-btn:hover {
  background: rgba(0, 0, 0, 0.05);
  color: var(--color-text-primary);
}

.clear-btn {
  background: none;
  border: none;
  color: #34C759;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 6px;
  transition: all 0.2s;
}

.clear-btn:hover {
  background: rgba(52, 199, 89, 0.1);
}

.customize-btn {
  background: none;
  border: none;
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
  color: var(--color-text-secondary);
}

.customize-btn:hover {
  background: rgba(0, 0, 0, 0.05);
  color: var(--color-text-primary);
}

.dropdown-footer {
  padding: 12px 20px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  text-align: center;
}

.view-all-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: #34C759;
  text-decoration: none;
  font-size: 13px;
  font-weight: 600;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.2s;
}

.view-all-btn:hover {
  background: rgba(52, 199, 89, 0.1);
}

/* 通知分类标签 */
.notification-tabs {
  display: flex;
  gap: 8px;
  padding: 12px 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  overflow-x: auto;
}

.tab-btn {
  background: none;
  border: none;
  padding: 6px 12px;
  border-radius: 12px;
  font-size: 13px;
  font-weight: 600;
  color: var(--color-text-secondary);
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
  display: flex;
  align-items: center;
  gap: 6px;
}

.tab-btn:hover {
  background: rgba(0, 0, 0, 0.05);
  color: var(--color-text-primary);
}

.tab-btn.active {
  background: linear-gradient(135deg, #34C759, #30B74F);
  color: white;
}

.tab-count {
  background: rgba(255, 255, 255, 0.3);
  padding: 2px 6px;
  border-radius: 8px;
  font-size: 11px;
  min-width: 18px;
  text-align: center;
}

.tab-btn.active .tab-count {
  background: rgba(255, 255, 255, 0.25);
}

.tab-btn:not(.active) .tab-count {
  background: rgba(0, 0, 0, 0.08);
  color: var(--color-text-secondary);
}

/* 通知列表 */
.notifications-list {
  max-height: 450px;
  overflow-y: auto;
}

.notification-item {
  display: flex;
  gap: 12px;
  padding: 16px 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.04);
  transition: all 0.2s;
  cursor: pointer;
}

.notification-item:hover {
  background: rgba(52, 199, 89, 0.03);
}

.notification-item.unread {
  background: rgba(52, 199, 89, 0.05);
}

.notification-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
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
  width: 20px;
  height: 20px;
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 8px;
  margin-bottom: 4px;
}

.notification-title {
  font-size: 14px;
  font-weight: 700;
  color: var(--color-text-primary);
  flex: 1;
}

.mark-read-btn {
  background: none;
  border: none;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #34C759;
  transition: all 0.2s;
  flex-shrink: 0;
}

.mark-read-btn:hover {
  transform: scale(1.2);
}

.notification-message {
  font-size: 13px;
  color: var(--color-text-secondary);
  margin-bottom: 8px;
  line-height: 1.5;
  display: -webkit-box;
  line-clamp: 2;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.notification-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.notification-time {
  font-size: 11px;
  color: var(--color-text-tertiary);
  font-weight: 600;
}

.notification-category {
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 10px;
  background: rgba(0, 0, 0, 0.06);
  color: var(--color-text-secondary);
  font-weight: 600;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px 20px;
  color: var(--color-text-secondary);
}

.empty-state svg {
  width: 64px;
  height: 64px;
  margin-bottom: 12px;
  opacity: 0.4;
}

.empty-state p {
  font-size: 14px;
  margin: 0;
}

/* 快捷菜单 */
.quick-menu-dropdown {
  min-width: 380px;
  max-width: 420px;
}

.quick-section {
  padding: 16px 20px;
}

.quick-section:not(:last-child) {
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.section-title {
  font-size: 12px;
  font-weight: 700;
  color: var(--color-text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 12px;
}

.quick-menu-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.quick-menu-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 16px 12px;
  border-radius: 12px;
  text-decoration: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(0, 0, 0, 0.02);
}

.quick-menu-item:hover {
  background: rgba(52, 199, 89, 0.08);
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.quick-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.quick-icon svg {
  width: 24px;
  height: 24px;
}

.quick-name {
  font-size: 12px;
  font-weight: 600;
  color: var(--color-text-primary);
  text-align: center;
  line-height: 1.3;
}

/* 快捷操作 */
.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: none;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s;
  text-align: left;
  width: 100%;
}

.action-item:hover {
  background: rgba(0, 0, 0, 0.04);
}

.action-icon {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.action-item span {
  flex: 1;
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.action-arrow {
  width: 16px;
  height: 16px;
  color: var(--color-text-tertiary);
  transition: transform 0.2s;
}

.action-item:hover .action-arrow {
  transform: translateX(4px);
}

/* 系统状态 */
.system-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 12px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 10px;
}

.stat-label {
  font-size: 11px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.stat-value {
  font-size: 20px;
  font-weight: 800;
  line-height: 1;
}

/* 用户菜单 */
.user-btn {
  padding: 0;
  overflow: hidden;
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.user-menu-dropdown {
  min-width: 300px;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 20px;
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.06), rgba(48, 183, 79, 0.06));
  position: relative;
}

.edit-profile-btn {
  position: absolute;
  top: 16px;
  right: 16px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border: none;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.edit-profile-btn:hover {
  transform: scale(1.1) rotate(90deg);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.profile-avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.profile-info {
  flex: 1;
}

.profile-name {
  font-size: 17px;
  font-weight: 800;
  color: var(--color-text-primary);
  margin-bottom: 2px;
}

.profile-role {
  font-size: 12px;
  color: #34C759;
  font-weight: 700;
  margin-bottom: 3px;
}

.profile-email {
  font-size: 11px;
  color: var(--color-text-tertiary);
  font-weight: 500;
}

/* 用户统计 */
.user-stats {
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding: 16px 20px;
  background: rgba(0, 0, 0, 0.02);
}

.user-stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.stat-number {
  font-size: 20px;
  font-weight: 800;
  color: var(--color-text-primary);
  line-height: 1;
}

.stat-label {
  font-size: 11px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.stat-divider {
  width: 1px;
  height: 32px;
  background: rgba(0, 0, 0, 0.1);
}

.menu-divider {
  height: 1px;
  background: rgba(0, 0, 0, 0.06);
  margin: 0;
}

.user-menu-list {
  padding: 8px 0;
}

.user-menu-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 20px;
  color: var(--color-text-primary);
  text-decoration: none;
  transition: all 0.2s;
  font-size: 14px;
  cursor: pointer;
  border: none;
  background: none;
  width: 100%;
  text-align: left;
  position: relative;
}

.user-menu-item:hover {
  background: rgba(52, 199, 89, 0.06);
}

.menu-item-icon {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.1), rgba(48, 183, 79, 0.1));
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.2s;
}

.user-menu-item:hover .menu-item-icon {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.15), rgba(48, 183, 79, 0.15));
  transform: scale(1.05);
}

.menu-item-icon svg {
  width: 18px;
  height: 18px;
  color: #34C759;
}

.menu-item-content {
  flex: 1;
}

.menu-item-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 2px;
}

.menu-item-desc {
  font-size: 11px;
  color: var(--color-text-tertiary);
  font-weight: 500;
}

.menu-arrow {
  width: 16px;
  height: 16px;
  color: var(--color-text-tertiary);
  flex-shrink: 0;
  transition: transform 0.2s;
}

.user-menu-item:hover .menu-arrow {
  transform: translateX(4px);
}

/* 主题切换 */
.theme-toggle {
  flex-shrink: 0;
}

.toggle-track {
  width: 44px;
  height: 24px;
  background: rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  position: relative;
  transition: all 0.3s;
  cursor: pointer;
}

.toggle-track.active {
  background: linear-gradient(135deg, #34C759, #30B74F);
}

.toggle-thumb {
  width: 20px;
  height: 20px;
  background: white;
  border-radius: 50%;
  position: absolute;
  top: 2px;
  left: 2px;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
}

.toggle-track.active .toggle-thumb {
  transform: translateX(20px);
}

.user-menu-item.logout {
  color: #FF3B30;
}

.user-menu-item.logout:hover {
  background: rgba(255, 59, 48, 0.06);
}

.logout-icon {
  background: linear-gradient(135deg, rgba(255, 59, 48, 0.1), rgba(255, 69, 58, 0.1));
}

.user-menu-item.logout:hover .logout-icon {
  background: linear-gradient(135deg, rgba(255, 59, 48, 0.15), rgba(255, 69, 58, 0.15));
}

.logout-icon svg {
  color: #FF3B30;
}

/* 余额显示 */
.balance-display {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  background: rgba(52, 199, 89, 0.1);
  border-radius: 20px;
  margin-right: 16px;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid rgba(52, 199, 89, 0.2);
}

.balance-display:hover {
  background: rgba(52, 199, 89, 0.15);
  transform: translateY(-1px);
}

.balance-label {
  font-size: 12px;
  color: #666;
  font-weight: 500;
}

.balance-value {
  font-size: 16px;
  font-weight: 700;
  color: #34C759;
  font-family: 'DIN Alternate', sans-serif;
}

.add-btn {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: #34C759;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 4px;
}

.add-btn svg {
  width: 12px;
  height: 12px;
}

/* 响应式 */
@media (max-width: 768px) {
  .balance-display {
    display: none;
  }
}
</style>


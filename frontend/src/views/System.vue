<template>
  <div class="page">
    <h1 class="page-title">系统健康巡查</h1>

    <!-- 系统状态总览 -->
    <div class="status-overview">
      <div class="status-card" :class="'status-' + systemStatus.overall">
        <div class="status-icon">
          <svg v-if="systemStatus.overall === 'healthy'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M20 6L9 17L4 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <svg v-else-if="systemStatus.overall === 'warning'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 9V13M12 17H12.01M10.29 3.86L1.82 18C1.64537 18.3024 1.55297 18.6453 1.55199 18.9945C1.55101 19.3437 1.64149 19.6871 1.81442 19.9905C1.98735 20.2939 2.23672 20.5467 2.53771 20.7239C2.8387 20.901 3.18018 20.9962 3.53 21H20.47C20.8198 20.9962 21.1613 20.901 21.4623 20.7239C21.7633 20.5467 22.0127 20.2939 22.1856 19.9905C22.3585 19.6871 22.449 19.3437 22.448 18.9945C22.447 18.6453 22.3546 18.3024 22.18 18L13.71 3.86C13.5317 3.56611 13.2807 3.32312 12.9812 3.15448C12.6817 2.98585 12.3437 2.89725 12 2.89725C11.6563 2.89725 11.3183 2.98585 11.0188 3.15448C10.7193 3.32312 10.4683 3.56611 10.29 3.86Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <svg v-else viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
            <path d="M15 9L9 15M9 9L15 15" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="status-info">
          <div class="status-text">{{ systemStatus.text }}</div>
          <div class="status-time">最后检查: {{ systemStatus.lastCheck }}</div>
        </div>
        <button class="refresh-btn" @click="refreshStatus" :disabled="isRefreshing">
          <svg :class="{ spinning: isRefreshing }" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M21 10C21 10 18.995 7.26822 17.3662 5.63824C15.7373 4.00827 13.4864 3 11 3C6.02944 3 2 7.02944 2 12C2 16.9706 6.02944 21 11 21C15.1031 21 18.5649 18.2543 19.6482 14.5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M21 10V4M21 10H15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
      </div>
    </div>

    <!-- 关键指标 -->
    <!-- <div class="metrics-row">
      <div class="metric-card">
        <div class="metric-header">
          <div class="metric-icon cpu">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect x="9" y="9" width="6" height="6" stroke="currentColor" stroke-width="2" fill="none"/>
              <path d="M9 1V6M15 1V6M9 18V23M15 18V23M1 9H6M18 9H23M1 15H6M18 15H23" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div class="metric-title">CPU 使用率</div>
        </div>
        <div class="metric-value">{{ metrics.cpu }}%</div>
        <div class="metric-bar">
          <div class="metric-fill cpu" :style="{ width: metrics.cpu + '%' }"></div>
        </div>
        <div class="metric-status" :class="getStatusClass(metrics.cpu, 80)">
          {{ getStatusText(metrics.cpu, 80) }}
        </div>
      </div>

      <div class="metric-card">
        <div class="metric-header">
          <div class="metric-icon memory">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect x="3" y="5" width="18" height="14" rx="2" stroke="currentColor" stroke-width="2" fill="none"/>
              <path d="M7 9H7.01M7 12H7.01M7 15H7.01M11 9H17M11 12H17M11 15H17" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div class="metric-title">内存使用率</div>
        </div>
        <div class="metric-value">{{ metrics.memory }}%</div>
        <div class="metric-bar">
          <div class="metric-fill memory" :style="{ width: metrics.memory + '%' }"></div>
        </div>
        <div class="metric-status" :class="getStatusClass(metrics.memory, 85)">
          {{ getStatusText(metrics.memory, 85) }}
        </div>
      </div>

      <div class="metric-card">
        <div class="metric-header">
          <div class="metric-icon disk">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2" fill="none"/>
              <circle cx="12" cy="12" r="3" fill="currentColor"/>
              <path d="M12 2V5M12 19V22M22 12H19M5 12H2" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div class="metric-title">磁盘使用率</div>
        </div>
        <div class="metric-value">{{ metrics.disk }}%</div>
        <div class="metric-bar">
          <div class="metric-fill disk" :style="{ width: metrics.disk + '%' }"></div>
        </div>
        <div class="metric-status" :class="getStatusClass(metrics.disk, 90)">
          {{ getStatusText(metrics.disk, 90) }}
        </div>
      </div>

      <div class="metric-card">
        <div class="metric-header">
          <div class="metric-icon network">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>
              <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <div class="metric-title">网络延迟</div>
        </div>
        <div class="metric-value">{{ metrics.network }}ms</div>
        <div class="metric-bar">
          <div class="metric-fill network" :style="{ width: Math.min(metrics.network / 2, 100) + '%' }"></div>
        </div>
        <div class="metric-status" :class="getStatusClass(metrics.network, 100)">
          {{ metrics.network < 50 ? '优秀' : metrics.network < 100 ? '良好' : '较慢' }}
        </div>
      </div>
    </div> -->

    <!-- 服务状态 -->
    <div class="card">
      <div class="section-header">
        <h3 class="section-title">服务状态</h3>
        <div class="status-summary">
          <span class="summary-item running">{{ runningServices }}个运行中</span>
          <span class="summary-item stopped">{{ stoppedServices }}个已停止</span>
        </div>
      </div>

      <div class="services-grid">
        <div 
          v-for="service in services" 
          :key="service.name"
          class="service-item"
          :class="'service-' + service.status"
        >
          <div class="service-icon">
            <svg v-if="service.status === 'running'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2" fill="none"/>
              <path d="M20 6L9 17L4 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <svg v-else-if="service.status === 'warning'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2" fill="none"/>
              <path d="M12 8V12M12 16H12.01" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <svg v-else viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2" fill="none"/>
              <path d="M15 9L9 15M9 9L15 15" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div class="service-info">
            <div class="service-name">{{ service.name }}</div>
            <div class="service-desc">{{ service.description }}</div>
          </div>
          <div class="service-meta">
            <div class="service-uptime">{{ service.uptime }}</div>
            <div class="service-badge" :class="service.status">
              {{ service.statusText }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 数据库状态 -->
    <div class="card">
      <div class="section-header">
        <h3 class="section-title">数据库连接</h3>
      </div>

      <div class="database-grid">
        <div 
          v-for="db in databases" 
          :key="db.name"
          class="database-item"
        >
          <div class="db-header">
            <div class="db-icon">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <ellipse cx="12" cy="5" rx="9" ry="3" stroke="currentColor" stroke-width="2" fill="none"/>
                <path d="M21 12C21 13.66 16.97 15 12 15C7.03 15 3 13.66 3 12" stroke="currentColor" stroke-width="2"/>
                <path d="M3 5V19C3 20.66 7.03 22 12 22C16.97 22 21 20.66 21 19V5" stroke="currentColor" stroke-width="2"/>
                <path d="M21 12C21 13.66 16.97 15 12 15C7.03 15 3 13.66 3 12" stroke="currentColor" stroke-width="2"/>
              </svg>
            </div>
            <div class="db-info">
              <div class="db-name">{{ db.name }}</div>
              <div class="db-host">{{ db.host }}</div>
            </div>
            <div class="db-status" :class="db.status">
              {{ db.status === 'connected' ? '已连接' : '未连接' }}
            </div>
          </div>
          <div class="db-stats">
            <div class="db-stat">
              <span class="stat-label">连接数</span>
              <span class="stat-value">{{ db.connections }}</span>
            </div>
            <div class="db-stat">
              <span class="stat-label">查询数</span>
              <span class="stat-value">{{ db.queries }}</span>
            </div>
            <div class="db-stat">
              <span class="stat-label">响应时间</span>
              <span class="stat-value">{{ db.responseTime }}ms</span>
            </div>
            <div class="db-stat">
              <span class="stat-label">存储大小</span>
              <span class="stat-value">{{ db.size }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 系统日志 -->
    <div class="card">
      <div class="section-header">
        <h3 class="section-title">最近日志</h3>
        <div class="log-filters">
          <button 
            v-for="level in logLevels" 
            :key="level.value"
            class="filter-btn"
            :class="{ active: logFilter === level.value }"
            @click="logFilter = level.value"
          >
            {{ level.label }}
          </button>
        </div>
      </div>

      <div class="log-list">
        <div 
          v-for="log in filteredLogs" 
          :key="log.id"
          class="log-item"
          :class="'log-' + log.level"
        >
          <div class="log-level-badge" :class="log.level">
            {{ log.level.toUpperCase() }}
          </div>
          <div class="log-time">{{ log.time }}</div>
          <div class="log-message">{{ log.message }}</div>
          <div class="log-source">{{ log.source }}</div>
        </div>
      </div>
    </div>

    <!-- 告警信息 -->
    <div class="card" v-if="alerts.length > 0">
      <div class="section-header">
        <h3 class="section-title">系统告警</h3>
        <button class="btn-secondary" @click="clearAlerts">
          清除所有告警
        </button>
      </div>

      <div class="alerts-list">
        <div 
          v-for="alert in alerts" 
          :key="alert.id"
          class="alert-item"
          :class="'alert-' + alert.severity"
        >
          <div class="alert-icon">
            <svg v-if="alert.severity === 'critical'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2" fill="none"/>
              <path d="M15 9L9 15M9 9L15 15" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <svg v-else viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M10.29 3.86L1.82 18C1.64537 18.3024 1.55297 18.6453 1.55199 18.9945C1.55101 19.3437 1.64149 19.6871 1.81442 19.9905C1.98735 20.2939 2.23672 20.5467 2.53771 20.7239C2.8387 20.901 3.18018 20.9962 3.53 21H20.47C20.8198 20.9962 21.1613 20.901 21.4623 20.7239C21.7633 20.5467 22.0127 20.2939 22.1856 19.9905C22.3585 19.6871 22.449 19.3437 22.448 18.9945C22.447 18.6453 22.3546 18.3024 22.18 18L13.71 3.86C13.5317 3.56611 13.2807 3.32312 12.9812 3.15448C12.6817 2.98585 12.3437 2.89725 12 2.89725C11.6563 2.89725 11.3183 2.98585 11.0188 3.15448C10.7193 3.32312 10.4683 3.56611 10.29 3.86Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M12 9V13M12 17H12.01" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div class="alert-content">
            <div class="alert-title">{{ alert.title }}</div>
            <div class="alert-message">{{ alert.message }}</div>
            <div class="alert-time">{{ alert.time }}</div>
          </div>
          <button class="alert-close" @click="dismissAlert(alert.id)">×</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { dashboardAPI, recordAPI } from '../api'

// 系统状态
const systemStatus = ref({
  overall: 'healthy',
  text: '系统运行正常',
  lastCheck: ''
})

const isRefreshing = ref(false)

// 关键指标 (暂无后端API支持)
const metrics = ref({
  cpu: 0,
  memory: 0,
  disk: 0,
  network: 0
})

// 服务状态
const services = ref([
  { name: 'API服务', description: '充电桩后端API服务', status: 'unknown', statusText: '检测中...', uptime: '-' },
  { name: '数据库服务', description: 'MySQL数据库服务', status: 'unknown', statusText: '检测中...', uptime: '-' }
])

const runningServices = computed(() => services.value.filter(s => s.status === 'running').length)
const stoppedServices = computed(() => services.value.filter(s => s.status === 'stopped').length)

// 数据库状态
const databases = ref([
  { 
    name: 'MySQL主库', 
    host: 'localhost:3306', 
    status: 'connected',
    connections: '-',
    queries: '-',
    responseTime: '-',
    size: '-'
  }
])

// 日志
const logFilter = ref('all')
const logLevels = [
  { label: '全部', value: 'all' },
  { label: '错误', value: 'error' },
  { label: '警告', value: 'warning' },
  { label: '信息', value: 'info' }
]

const logs = ref([])

const filteredLogs = computed(() => {
  if (logFilter.value === 'all') {
    return logs.value
  }
  return logs.value.filter(log => log.level === logFilter.value)
})

// 告警
const alerts = ref([])

// 方法
const getStatusClass = (value, threshold) => {
  if (value < threshold * 0.7) return 'status-good'
  if (value < threshold) return 'status-warning'
  return 'status-danger'
}

const getStatusText = (value, threshold) => {
  if (value < threshold * 0.7) return '正常'
  if (value < threshold) return '警告'
  return '危险'
}

const refreshStatus = async () => {
  isRefreshing.value = true
  await checkServices()
  await fetchLogs()
  isRefreshing.value = false
}

const checkServices = async () => {
  try {
    await dashboardAPI.getStats()
    // API调用成功，说明后端和数据库正常
    services.value[0].status = 'running'
    services.value[0].statusText = '运行中'
    services.value[1].status = 'running'
    services.value[1].statusText = '运行中'
    
    databases.value[0].status = 'connected'
    
    systemStatus.value.overall = 'healthy'
    systemStatus.value.text = '系统运行正常'
  } catch (e) {
    services.value[0].status = 'stopped'
    services.value[0].statusText = '异常'
    services.value[1].status = 'stopped'
    services.value[1].statusText = '异常'
    
    databases.value[0].status = 'disconnected'
    
    systemStatus.value.overall = 'error'
    systemStatus.value.text = '系统服务异常'
  }
  systemStatus.value.lastCheck = new Date().toLocaleString()
}

const fetchLogs = async () => {
  try {
    const res = await recordAPI.list({ page: 1, size: 20 })
    if (res.data && res.data.records) {
      logs.value = res.data.records.map(r => ({
        id: r.id,
        level: r.status === 'failed' ? 'error' : 'info',
        time: r.updatedAt ? new Date(r.updatedAt).toLocaleTimeString() : '',
        message: `用户 ${r.userName} 在 ${r.stationName} ${r.status === 'charging' ? '开始充电' : '结束充电'}`,
        source: 'ChargingService'
      }))
    }
  } catch (e) {
    console.error('Failed to fetch logs', e)
  }
}

const clearAlerts = () => {
  alerts.value = []
}

const dismissAlert = (id) => {
  alerts.value = alerts.value.filter(alert => alert.id !== id)
}

// 自动刷新
let autoRefreshTimer = null

onMounted(() => {
  refreshStatus()
})

onUnmounted(() => {
  if (autoRefreshTimer) {
    clearInterval(autoRefreshTimer)
  }
})
</script>

<style scoped>
.page {
  max-width: 1600px;
  animation: pageSlideIn 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes pageSlideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.page-title {
  font-size: 32px;
  font-weight: 800;
  margin-bottom: 24px;
  background: linear-gradient(135deg, #1d1d1f 0%, #007AFF 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -1px;
}

.card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(40px);
  border-radius: 20px;
  padding: 28px;
  border: 1px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.04);
  margin-bottom: 24px;
}

/* 系统状态总览 */
.status-overview {
  margin-bottom: 24px;
}

.status-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(40px);
  border-radius: 20px;
  padding: 28px;
  border: 2px solid;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  gap: 20px;
  transition: all 0.3s;
}

.status-card.status-healthy {
  border-color: #34C759;
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.05), rgba(48, 209, 88, 0.05));
}

.status-card.status-warning {
  border-color: #FF9500;
  background: linear-gradient(135deg, rgba(255, 149, 0, 0.05), rgba(255, 107, 0, 0.05));
}

.status-card.status-error {
  border-color: #FF3B30;
  background: linear-gradient(135deg, rgba(255, 59, 48, 0.05), rgba(255, 69, 58, 0.05));
}

.status-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.status-healthy .status-icon {
  background: linear-gradient(135deg, #34C759, #30D158);
  color: white;
}

.status-warning .status-icon {
  background: linear-gradient(135deg, #FF9500, #FF6B00);
  color: white;
}

.status-error .status-icon {
  background: linear-gradient(135deg, #FF3B30, #FF453A);
  color: white;
}

.status-icon svg {
  width: 32px;
  height: 32px;
}

.status-info {
  flex: 1;
}

.status-text {
  font-size: 24px;
  font-weight: 800;
  color: var(--color-text-primary);
  margin-bottom: 6px;
}

.status-time {
  font-size: 14px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.refresh-btn {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.8);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.refresh-btn:hover:not(:disabled) {
  background: rgba(0, 122, 255, 0.1);
  transform: scale(1.05);
}

.refresh-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.refresh-btn svg {
  width: 24px;
  height: 24px;
  color: #007AFF;
}

.spinning {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 关键指标 */
.metrics-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.metric-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(40px);
  border-radius: 18px;
  padding: 24px;
  border: 1px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
  transition: all 0.3s;
}

.metric-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.metric-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.metric-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.metric-icon.cpu {
  background: linear-gradient(135deg, #007AFF, #0051D5);
}

.metric-icon.memory {
  background: linear-gradient(135deg, #34C759, #30D158);
}

.metric-icon.disk {
  background: linear-gradient(135deg, #FF9500, #FF6B00);
}

.metric-icon.network {
  background: linear-gradient(135deg, #5856D6, #AF52DE);
}

.metric-icon svg {
  width: 20px;
  height: 20px;
}

.metric-title {
  font-size: 13px;
  font-weight: 700;
  color: var(--color-text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.metric-value {
  font-size: 32px;
  font-weight: 800;
  color: var(--color-text-primary);
  margin-bottom: 12px;
  line-height: 1;
}

.metric-bar {
  height: 8px;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 12px;
}

.metric-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.metric-fill.cpu {
  background: linear-gradient(90deg, #007AFF, #0051D5);
}

.metric-fill.memory {
  background: linear-gradient(90deg, #34C759, #30D158);
}

.metric-fill.disk {
  background: linear-gradient(90deg, #FF9500, #FF6B00);
}

.metric-fill.network {
  background: linear-gradient(90deg, #5856D6, #AF52DE);
}

.metric-status {
  font-size: 12px;
  font-weight: 700;
  text-align: center;
  padding: 4px 8px;
  border-radius: 6px;
}

.metric-status.status-good {
  background: rgba(52, 199, 89, 0.12);
  color: #34C759;
}

.metric-status.status-warning {
  background: rgba(255, 149, 0, 0.12);
  color: #FF9500;
}

.metric-status.status-danger {
  background: rgba(255, 59, 48, 0.12);
  color: #FF3B30;
}

/* 通用部分 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
}

.section-title {
  font-size: 18px;
  font-weight: 800;
  color: var(--color-text-primary);
  margin: 0;
  letter-spacing: -0.5px;
}

.status-summary {
  display: flex;
  gap: 16px;
}

.summary-item {
  font-size: 13px;
  font-weight: 700;
  padding: 6px 12px;
  border-radius: 8px;
}

.summary-item.running {
  background: rgba(52, 199, 89, 0.12);
  color: #34C759;
}

.summary-item.stopped {
  background: rgba(142, 142, 147, 0.12);
  color: #8E8E93;
}

/* 服务状态 */
.services-grid {
  display: grid;
  gap: 16px;
}

.service-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 14px;
  border: 2px solid transparent;
  transition: all 0.3s;
}

.service-item:hover {
  background: rgba(0, 0, 0, 0.03);
  transform: translateX(4px);
}

.service-item.service-running {
  border-color: rgba(52, 199, 89, 0.3);
}

.service-item.service-warning {
  border-color: rgba(255, 149, 0, 0.3);
}

.service-item.service-stopped {
  border-color: rgba(255, 59, 48, 0.3);
}

.service-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.service-running .service-icon {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.12), rgba(48, 209, 88, 0.12));
  color: #34C759;
}

.service-warning .service-icon {
  background: linear-gradient(135deg, rgba(255, 149, 0, 0.12), rgba(255, 107, 0, 0.12));
  color: #FF9500;
}

.service-stopped .service-icon {
  background: linear-gradient(135deg, rgba(255, 59, 48, 0.12), rgba(255, 69, 58, 0.12));
  color: #FF3B30;
}

.service-icon svg {
  width: 24px;
  height: 24px;
}

.service-info {
  flex: 1;
}

.service-name {
  font-size: 16px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 4px;
}

.service-desc {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 500;
}

.service-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
}

.service-uptime {
  font-size: 12px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.service-badge {
  padding: 5px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.3px;
}

.service-badge.running {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.12), rgba(48, 209, 88, 0.12));
  color: #34C759;
  border: 1px solid rgba(52, 199, 89, 0.3);
}

.service-badge.warning {
  background: linear-gradient(135deg, rgba(255, 149, 0, 0.12), rgba(255, 107, 0, 0.12));
  color: #FF9500;
  border: 1px solid rgba(255, 149, 0, 0.3);
}

.service-badge.stopped {
  background: linear-gradient(135deg, rgba(255, 59, 48, 0.12), rgba(255, 69, 58, 0.12));
  color: #FF3B30;
  border: 1px solid rgba(255, 59, 48, 0.3);
}

/* 数据库 */
.database-grid {
  display: grid;
  gap: 20px;
}

.database-item {
  padding: 24px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 14px;
  border: 1px solid rgba(0, 0, 0, 0.06);
}

.db-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.db-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.12), rgba(88, 86, 214, 0.12));
  color: #007AFF;
  display: flex;
  align-items: center;
  justify-content: center;
}

.db-icon svg {
  width: 24px;
  height: 24px;
}

.db-info {
  flex: 1;
}

.db-name {
  font-size: 16px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 4px;
}

.db-host {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-family: 'Courier New', monospace;
  font-weight: 600;
}

.db-status {
  padding: 6px 14px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 700;
}

.db-status.connected {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.12), rgba(48, 209, 88, 0.12));
  color: #34C759;
  border: 1px solid rgba(52, 199, 89, 0.3);
}

.db-status.disconnected {
  background: rgba(142, 142, 147, 0.12);
  color: #8E8E93;
  border: 1px solid rgba(142, 142, 147, 0.3);
}

.db-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.db-stat {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 12px;
  background: white;
  border-radius: 10px;
}

.stat-label {
  font-size: 12px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.stat-value {
  font-size: 16px;
  font-weight: 800;
  color: var(--color-text-primary);
}

/* 日志 */
.log-filters {
  display: flex;
  gap: 8px;
}

.filter-btn {
  padding: 8px 16px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  background: white;
  color: var(--color-text-secondary);
  cursor: pointer;
  transition: all 0.3s;
}

.filter-btn:hover {
  background: rgba(0, 122, 255, 0.05);
}

.filter-btn.active {
  background: linear-gradient(135deg, #007AFF, #0051D5);
  color: white;
  border-color: transparent;
}

.log-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.log-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 14px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 10px;
  border-left: 4px solid;
  transition: all 0.2s;
}

.log-item:hover {
  background: rgba(0, 0, 0, 0.04);
}

.log-item.log-error {
  border-left-color: #FF3B30;
}

.log-item.log-warning {
  border-left-color: #FF9500;
}

.log-item.log-info {
  border-left-color: #007AFF;
}

.log-level-badge {
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.5px;
  min-width: 60px;
  text-align: center;
}

.log-level-badge.error {
  background: rgba(255, 59, 48, 0.12);
  color: #FF3B30;
}

.log-level-badge.warning {
  background: rgba(255, 149, 0, 0.12);
  color: #FF9500;
}

.log-level-badge.info {
  background: rgba(0, 122, 255, 0.12);
  color: #007AFF;
}

.log-time {
  font-size: 13px;
  font-family: 'Courier New', monospace;
  color: var(--color-text-secondary);
  font-weight: 600;
  min-width: 90px;
}

.log-message {
  flex: 1;
  font-size: 14px;
  color: var(--color-text-primary);
  font-weight: 500;
}

.log-source {
  font-size: 12px;
  color: var(--color-text-secondary);
  font-weight: 600;
  padding: 4px 10px;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 6px;
}

/* 告警 */
.btn-secondary {
  padding: 10px 18px;
  background: rgba(28, 28, 30, 0.06);
  color: var(--color-text-primary);
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-secondary:hover {
  background: rgba(28, 28, 30, 0.12);
  transform: translateY(-1px);
}

.alerts-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.alert-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 20px;
  border-radius: 14px;
  border: 2px solid;
  position: relative;
}

.alert-item.alert-warning {
  background: linear-gradient(135deg, rgba(255, 149, 0, 0.05), rgba(255, 107, 0, 0.05));
  border-color: rgba(255, 149, 0, 0.3);
}

.alert-item.alert-critical {
  background: linear-gradient(135deg, rgba(255, 59, 48, 0.05), rgba(255, 69, 58, 0.05));
  border-color: rgba(255, 59, 48, 0.3);
}

.alert-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.alert-warning .alert-icon {
  background: #FF9500;
  color: white;
}

.alert-critical .alert-icon {
  background: #FF3B30;
  color: white;
}

.alert-icon svg {
  width: 24px;
  height: 24px;
}

.alert-content {
  flex: 1;
}

.alert-title {
  font-size: 16px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 6px;
}

.alert-message {
  font-size: 14px;
  color: var(--color-text-secondary);
  margin-bottom: 8px;
  line-height: 1.5;
}

.alert-time {
  font-size: 12px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.alert-close {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.8);
  color: var(--color-text-secondary);
  font-size: 24px;
  line-height: 1;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.alert-close:hover {
  background: rgba(255, 59, 48, 0.1);
  color: #FF3B30;
}

/* 响应式 */
@media (max-width: 1024px) {
  .metrics-row {
    grid-template-columns: repeat(2, 1fr);
  }

  .db-stats {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .metrics-row {
    grid-template-columns: 1fr;
  }

  .db-stats {
    grid-template-columns: 1fr;
  }

  .log-item {
    flex-direction: column;
    align-items: flex-start;
  }

  .status-summary {
    flex-direction: column;
    gap: 8px;
  }
}
</style>

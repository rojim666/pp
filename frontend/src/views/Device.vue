<template>
  <div class="page">
    <h1 class="page-title">充电设备状态</h1>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon online">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
            <path d="M9 12l2 2 4-4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ onlineCount }}</div>
          <div class="stat-label">在线设备</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon charging">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M13 2L3 14h8l-1 8 10-12h-8l1-8z" fill="currentColor"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ chargingCount }}</div>
          <div class="stat-label">充电中</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon power">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 2v6m0 4v10M6.34 6.34l4.24 4.24m7.07-4.24l-4.24 4.24" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ totalPower.toFixed(1) }} kW</div>
          <div class="stat-label">额定总功率</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon energy">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="3" y="6" width="18" height="12" rx="2" stroke="currentColor" stroke-width="2"/>
            <line x1="7" y1="12" x2="17" y2="12" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            <line x1="21" y1="10" x2="21" y2="14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ totalEnergy.toFixed(1) }} kWh</div>
          <div class="stat-label">累计充电量</div>
        </div>
      </div>
    </div>

    <!-- 设备列表 -->
    <div class="devices-container">
      <div 
        v-for="device in devices" 
        :key="device.id" 
        class="device-card"
        :class="device.status"
      >
        <!-- 设备头部 -->
        <div class="device-header">
          <div class="device-title-section">
            <div class="device-icon-wrapper">
              <svg viewBox="0 0 48 48" xmlns="http://www.w3.org/2000/svg">
                <defs>
                  <linearGradient :id="`deviceGradient${device.id}`" x1="0%" y1="0%" x2="100%" y2="100%">
                    <stop offset="0%" :style="`stop-color:${device.color1};stop-opacity:1`" />
                    <stop offset="100%" :style="`stop-color:${device.color2};stop-opacity:1`" />
                  </linearGradient>
                </defs>
                <rect x="14" y="8" width="20" height="28" rx="3" :fill="`url(#deviceGradient${device.id})`" />
                <rect x="18" y="12" width="4" height="8" rx="1" fill="white" opacity="0.9" />
                <rect x="26" y="12" width="4" height="8" rx="1" fill="white" opacity="0.9" />
                <path v-if="device.status === 'charging'" d="M24 24 L28 28 L25 28 L26 32 L22 28 L25 28 Z" fill="#FFD60A" />
              </svg>
            </div>
            <div>
              <h3 class="device-name">{{ device.name }}</h3>
              <span class="device-code">{{ device.code }}</span>
            </div>
          </div>
          <div class="device-status-badge" :class="device.status">
            {{ device.statusText }}
          </div>
        </div>

        <!-- 实时数据网格 -->
        <div class="metrics-grid">
          <!-- 电压 -->
          <div class="metric-item">
            <div class="metric-icon voltage">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M13 2L3 14h8l-1 8 10-12h-8l1-8z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <div class="metric-details">
              <div class="metric-label">电压</div>
              <div class="metric-value">{{ device.voltage }} <span class="metric-unit">V</span></div>
            </div>
          </div>

          <!-- 电流 -->
          <div class="metric-item">
            <div class="metric-icon current">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2v20M17 7l-5-5-5 5M7 17l5 5 5-5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <div class="metric-details">
              <div class="metric-label">电流</div>
              <div class="metric-value">{{ device.current }} <span class="metric-unit">A</span></div>
            </div>
          </div>

          <!-- 功率 -->
          <div class="metric-item">
            <div class="metric-icon power-item">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
                <path d="M12 6v6l4 4" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </div>
            <div class="metric-details">
              <div class="metric-label">功率</div>
              <div class="metric-value">{{ device.power }} <span class="metric-unit">kW</span></div>
            </div>
          </div>

          <!-- 电量 -->
          <div class="metric-item">
            <div class="metric-icon energy-item">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect x="6" y="6" width="12" height="14" rx="2" stroke="currentColor" stroke-width="2"/>
                <line x1="9" y1="11" x2="15" y2="11" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                <line x1="9" y1="15" x2="15" y2="15" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </div>
            <div class="metric-details">
              <div class="metric-label">已充电量</div>
              <div class="metric-value">{{ device.chargedEnergy }} <span class="metric-unit">kWh</span></div>
            </div>
          </div>

          <!-- 温度 -->
          <div class="metric-item">
            <div class="metric-icon temperature">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M14 14.76V3.5a2.5 2.5 0 0 0-5 0v11.26a4.5 4.5 0 1 0 5 0z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <div class="metric-details">
              <div class="metric-label">温度</div>
              <div class="metric-value">{{ device.temperature }} <span class="metric-unit">°C</span></div>
            </div>
          </div>

          <!-- 充电时长 -->
          <div class="metric-item">
            <div class="metric-icon duration">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
                <polyline points="12 6 12 12 16 14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </div>
            <div class="metric-details">
              <div class="metric-label">充电时长</div>
              <div class="metric-value">{{ device.chargingDuration }} <span class="metric-unit">min</span></div>
            </div>
          </div>
        </div>

        <!-- 进度条（仅充电中显示） -->
        <div v-if="device.status === 'charging'" class="charging-progress">
          <div class="progress-info">
            <span>充电进度</span>
            <span class="progress-percent">{{ device.batteryLevel }}%</span>
          </div>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: device.batteryLevel + '%' }"></div>
          </div>
        </div>

        <!-- 设备底部操作 -->
        <div class="device-footer">
          <div class="device-location">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 14px; height: 14px;">
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" stroke="currentColor" stroke-width="2"/>
              <circle cx="12" cy="10" r="3" stroke="currentColor" stroke-width="2"/>
            </svg>
            {{ device.location }}
          </div>
          <button class="btn-detail" @click="viewDetails(device)">查看详情</button>
        </div>
      </div>
    </div>
  </div>

  <!-- 设备详情弹窗 -->
  <div v-if="showDetailDialog" class="dialog-overlay" @click="closeDetail">
    <div class="dialog-content detail-dialog" @click.stop>
      <div class="dialog-header">
        <div class="dialog-title-section">
          <div class="dialog-icon-wrapper">
            <svg viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
              <defs>
                <linearGradient id="detail-gradient" x1="0%" y1="0%" x2="100%" y2="100%">
                  <stop offset="0%" style="stop-color:#34C759"/>
                  <stop offset="100%" style="stop-color:#30D158"/>
                </linearGradient>
              </defs>
              <rect x="20" y="10" width="60" height="80" rx="8" fill="url(#detail-gradient)" opacity="0.2"/>
              <rect x="20" y="10" width="60" height="80" rx="8" fill="none" stroke="url(#detail-gradient)" stroke-width="3"/>
              <circle cx="50" cy="50" r="15" fill="url(#detail-gradient)"/>
              <path d="M 45 50 L 48 53 L 55 46" fill="none" stroke="white" stroke-width="3" stroke-linecap="round"/>
            </svg>
          </div>
          <div>
            <h3 class="dialog-title">{{ selectedDevice?.name }}</h3>
            <p class="dialog-subtitle">设备编号: {{ selectedDevice?.code }}</p>
          </div>
        </div>
        <button class="btn-close" @click="closeDetail">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M18 6L6 18M6 6l12 12"/>
          </svg>
        </button>
      </div>

      <div class="dialog-body">
        <!-- 状态概览 -->
        <div class="detail-section">
          <h4 class="section-title">状态概览</h4>
          <div class="detail-grid">
            <div class="detail-item">
              <span class="detail-label">设备状态</span>
              <span :class="['device-status-badge', selectedDevice?.status]">
                {{ statusText[selectedDevice?.status] }}
              </span>
            </div>
            <div class="detail-item">
              <span class="detail-label">设备位置</span>
              <span class="detail-value">{{ selectedDevice?.location }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">设备型号</span>
              <span class="detail-value">DC-120kW-Pro</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">最大功率</span>
              <span class="detail-value">120 kW</span>
            </div>
          </div>
        </div>

        <!-- 实时数据 -->
        <div class="detail-section">
          <h4 class="section-title">实时数据</h4>
          <div class="metrics-detail-grid">
            <div class="metric-detail-card">
              <div class="metric-icon voltage">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M13 2L3 14h8l-1 8 10-12h-8l1-8z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <div class="metric-detail-info">
                <div class="metric-detail-label">输出电压</div>
                <div class="metric-detail-value">{{ selectedDevice?.voltage }} <span class="metric-unit">V</span></div>
              </div>
            </div>
            <div class="metric-detail-card">
              <div class="metric-icon current">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M12 2v20M17 7l-5-5-5 5M7 17l5 5 5-5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <div class="metric-detail-info">
                <div class="metric-detail-label">输出电流</div>
                <div class="metric-detail-value">{{ selectedDevice?.current }} <span class="metric-unit">A</span></div>
              </div>
            </div>
            <div class="metric-detail-card">
              <div class="metric-icon power-item">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
                  <path d="M12 6v6l4 4" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                </svg>
              </div>
              <div class="metric-detail-info">
                <div class="metric-detail-label">实时功率</div>
                <div class="metric-detail-value">{{ selectedDevice?.power }} <span class="metric-unit">kW</span></div>
              </div>
            </div>
            <div class="metric-detail-card">
              <div class="metric-icon energy-item">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <rect x="6" y="6" width="12" height="14" rx="2" stroke="currentColor" stroke-width="2"/>
                  <line x1="9" y1="11" x2="15" y2="11" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                  <line x1="9" y1="15" x2="15" y2="15" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                </svg>
              </div>
              <div class="metric-detail-info">
                <div class="metric-detail-label">已充电量</div>
                <div class="metric-detail-value">{{ selectedDevice?.chargedEnergy }} <span class="metric-unit">kWh</span></div>
              </div>
            </div>
            <div class="metric-detail-card">
              <div class="metric-icon temperature">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M14 14.76V3.5a2.5 2.5 0 0 0-5 0v11.26a4.5 4.5 0 1 0 5 0z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <div class="metric-detail-info">
                <div class="metric-detail-label">设备温度</div>
                <div class="metric-detail-value">{{ selectedDevice?.temperature }} <span class="metric-unit">°C</span></div>
              </div>
            </div>
            <div class="metric-detail-card">
              <div class="metric-icon duration">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
                  <polyline points="12 6 12 12 16 14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                </svg>
              </div>
              <div class="metric-detail-info">
                <div class="metric-detail-label">充电时长</div>
                <div class="metric-detail-value">{{ selectedDevice?.chargingDuration }} <span class="metric-unit">分钟</span></div>
              </div>
            </div>
          </div>
        </div>

        <!-- 充电进度 -->
        <div v-if="selectedDevice?.status === 'charging'" class="detail-section">
          <h4 class="section-title">充电进度</h4>
          <div class="charging-progress">
            <div class="progress-info">
              <span>电池电量</span>
              <span class="progress-percent">{{ selectedDevice?.batteryLevel }}%</span>
            </div>
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: selectedDevice?.batteryLevel + '%' }"></div>
            </div>
          </div>
          <div class="estimate-info">
            <div class="estimate-item">
              <span class="estimate-label">预计剩余时间</span>
              <span class="estimate-value">{{ Math.round((100 - selectedDevice?.batteryLevel) * 1.5) }} 分钟</span>
            </div>
            <div class="estimate-item">
              <span class="estimate-label">预计充满电量</span>
              <span class="estimate-value">{{ (selectedDevice?.chargedEnergy * 100 / selectedDevice?.batteryLevel).toFixed(1) }} kWh</span>
            </div>
          </div>
        </div>

        <!-- 设备信息 -->
        <div class="detail-section">
          <h4 class="section-title">设备信息</h4>
          <div class="info-table">
            <div class="info-row">
              <span class="info-label">厂商</span>
              <span class="info-value">特来电科技</span>
            </div>
            <div class="info-row">
              <span class="info-label">生产日期</span>
              <span class="info-value">2024-03-15</span>
            </div>
            <div class="info-row">
              <span class="info-label">安装日期</span>
              <span class="info-value">2024-05-20</span>
            </div>
            <div class="info-row">
              <span class="info-label">维护状态</span>
              <span class="info-value status-good">✓ 正常</span>
            </div>
            <div class="info-row">
              <span class="info-label">累计充电次数</span>
              <span class="info-value">1,284 次</span>
            </div>
            <div class="info-row">
              <span class="info-label">累计充电量</span>
              <span class="info-value">25,680 kWh</span>
            </div>
            <div class="info-row">
              <span class="info-label">本月收入</span>
              <span class="info-value">¥38,520</span>
            </div>
            <div class="info-row">
              <span class="info-label">下次保养</span>
              <span class="info-value">2025-01-15</span>
            </div>
          </div>
        </div>
      </div>

      <div class="dialog-footer">
        <button class="btn-secondary" @click="closeDetail">关闭</button>
        <button class="btn-primary">导出报告</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useStationStore } from '../stores/station'

const stationStore = useStationStore()

onMounted(() => {
  stationStore.fetchStations()
})

const statusText = {
  'charging': '充电中',
  'online': '空闲',
  'offline': '离线',
  'maintenance': '维护中'
}

const devices = computed(() => stationStore.stations.map(s => {
  let color1 = '#8E8E93'
  let color2 = '#636366'
  
  if (s.status === 'charging') {
    color1 = '#007AFF'
    color2 = '#5856D6'
  } else if (s.status === 'online') {
    color1 = '#34C759'
    color2 = '#30D158'
  }
  
  return {
    ...s,
    statusText: statusText[s.status] || s.status,
    color1,
    color2,
    voltage: s.voltage || 0,
    current: s.current || 0,
    power: s.power || 0,
    chargedEnergy: s.chargedEnergy || 0,
    temperature: s.temperature || 0,
    chargingDuration: s.chargingDuration || 0,
    batteryLevel: s.batteryLevel || 0,
    location: s.location
  }
}))

// 统计数据
const onlineCount = computed(() => stationStore.onlineCount)
const chargingCount = computed(() => stationStore.chargingCount)

const totalPower = computed(() => 
  stationStore.stations.reduce((sum, d) => sum + (d.power || 0), 0)
)

const totalEnergy = computed(() => stationStore.totalEnergy)

const selectedDevice = ref(null)
const showDetailDialog = ref(false)

const viewDetails = (device) => {
  selectedDevice.value = device
  showDetailDialog.value = true
}

const closeDetail = () => {
  showDetailDialog.value = false
}
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
  margin-bottom: 28px;
  background: linear-gradient(135deg, #1d1d1f 0%, #007AFF 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -1px;
}

/* 统计卡片 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
  margin-bottom: 28px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(40px);
  border-radius: 18px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  border: 1px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-icon svg {
  width: 28px;
  height: 28px;
}

.stat-icon.online {
  background: linear-gradient(135deg, #34C759, #30D158);
}

.stat-icon.charging {
  background: linear-gradient(135deg, #007AFF, #5856D6);
}

.stat-icon.power {
  background: linear-gradient(135deg, #FF9500, #FF6B00);
}

.stat-icon.energy {
  background: linear-gradient(135deg, #AF52DE, #BF5AF2);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 800;
  color: var(--color-text-primary);
  line-height: 1;
  margin-bottom: 6px;
}

.stat-label {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

/* 设备卡片 */
.devices-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(420px, 1fr));
  gap: 24px;
}

.device-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(40px);
  border-radius: 20px;
  padding: 24px;
  border: 2px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.04);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.device-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #34C759, #30D158);
  opacity: 0;
  transition: opacity 0.3s;
}

.device-card.charging::before {
  background: linear-gradient(90deg, #007AFF, #5856D6);
  opacity: 1;
}

.device-card.offline {
  opacity: 0.7;
}

.device-card.offline::before {
  background: linear-gradient(90deg, #8E8E93, #636366);
  opacity: 1;
}

.device-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.1);
  border-color: rgba(0, 122, 255, 0.2);
}

.device-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
}

.device-title-section {
  display: flex;
  align-items: center;
  gap: 14px;
}

.device-icon-wrapper {
  width: 56px;
  height: 56px;
  flex-shrink: 0;
}

.device-icon-wrapper svg {
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 2px 8px rgba(0, 0, 0, 0.15));
}

.device-name {
  font-size: 18px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0 0 4px 0;
  letter-spacing: -0.3px;
}

.device-code {
  font-size: 12px;
  color: var(--color-text-secondary);
  font-weight: 600;
  font-family: 'Courier New', monospace;
}

.device-status-badge {
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.3px;
}

.device-status-badge.online {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.15), rgba(48, 209, 88, 0.15));
  color: #34C759;
  border: 1px solid rgba(52, 199, 89, 0.3);
}

.device-status-badge.charging {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.15), rgba(88, 86, 214, 0.15));
  color: #007AFF;
  border: 1px solid rgba(0, 122, 255, 0.3);
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.7; }
}

.device-status-badge.offline {
  background: rgba(142, 142, 147, 0.15);
  color: #8E8E93;
  border: 1px solid rgba(142, 142, 147, 0.3);
}

/* 指标网格 */
.metrics-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.metric-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 12px;
  transition: all 0.3s;
}

.metric-item:hover {
  background: rgba(0, 122, 255, 0.06);
  transform: scale(1.02);
}

.metric-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.metric-icon svg {
  width: 20px;
  height: 20px;
}

.metric-icon.voltage {
  background: linear-gradient(135deg, #FF9500, #FF6B00);
}

.metric-icon.current {
  background: linear-gradient(135deg, #007AFF, #0051D5);
}

.metric-icon.power-item {
  background: linear-gradient(135deg, #5856D6, #AF52DE);
}

.metric-icon.energy-item {
  background: linear-gradient(135deg, #34C759, #30D158);
}

.metric-icon.temperature {
  background: linear-gradient(135deg, #FF3B30, #FF453A);
}

.metric-icon.duration {
  background: linear-gradient(135deg, #00C7BE, #00A89C);
}

.metric-details {
  flex: 1;
  min-width: 0;
}

.metric-label {
  font-size: 11px;
  color: var(--color-text-secondary);
  font-weight: 600;
  margin-bottom: 4px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.metric-value {
  font-size: 20px;
  font-weight: 800;
  color: var(--color-text-primary);
  line-height: 1;
  font-variant-numeric: tabular-nums;
}

.metric-unit {
  font-size: 12px;
  font-weight: 600;
  color: var(--color-text-secondary);
  margin-left: 2px;
}

/* 充电进度条 */
.charging-progress {
  margin-bottom: 20px;
  padding: 16px;
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.05), rgba(88, 86, 214, 0.05));
  border-radius: 12px;
  border: 1px solid rgba(0, 122, 255, 0.15);
}

.progress-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-size: 13px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.progress-percent {
  font-size: 16px;
  font-weight: 800;
  color: var(--color-primary);
}

.progress-bar {
  height: 10px;
  background: rgba(0, 0, 0, 0.08);
  border-radius: 5px;
  overflow: hidden;
  position: relative;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #007AFF, #5856D6);
  border-radius: 5px;
  transition: width 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.progress-fill::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  animation: shimmer 2s infinite;
}

@keyframes shimmer {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

/* 设备底部 */
.device-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.device-location {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 500;
}

.btn-detail {
  padding: 10px 20px;
  background: rgba(28, 28, 30, 0.06);
  color: var(--color-text-primary);
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.btn-detail:hover {
  background: rgba(28, 28, 30, 0.12);
  border-color: rgba(0, 0, 0, 0.2);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

/* 响应式 */
@media (max-width: 1200px) {
  .devices-container {
    grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .devices-container {
    grid-template-columns: 1fr;
  }

  .metrics-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .metric-value {
    font-size: 18px;
  }
}

/* ========== 详情弹窗样式 ========== */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  animation: fadeIn 0.2s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.dialog-content {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(40px);
  border-radius: 24px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  max-width: 900px;
  width: 90%;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  animation: slideUp 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(40px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 28px 32px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.05), rgba(48, 209, 88, 0.05));
}

.dialog-title-section {
  display: flex;
  align-items: center;
  gap: 16px;
}

.dialog-icon-wrapper {
  width: 56px;
  height: 56px;
  flex-shrink: 0;
}

.dialog-icon-wrapper svg {
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 4px 12px rgba(52, 199, 89, 0.3));
}

.dialog-title {
  font-size: 24px;
  font-weight: 800;
  color: var(--color-text-primary);
  margin: 0 0 4px 0;
  letter-spacing: -0.5px;
}

.dialog-subtitle {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 600;
  margin: 0;
  font-family: 'Courier New', monospace;
}

.btn-close {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  background: rgba(28, 28, 30, 0.06);
  color: var(--color-text-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-close:hover {
  background: rgba(28, 28, 30, 0.12);
  transform: scale(1.05);
}

.dialog-body {
  flex: 1;
  overflow-y: auto;
  padding: 32px;
}

.detail-section {
  margin-bottom: 32px;
}

.detail-section:last-child {
  margin-bottom: 0;
}

.section-title {
  font-size: 16px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0 0 16px 0;
  padding-bottom: 12px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
  letter-spacing: -0.3px;
}

/* 状态概览网格 */
.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 12px;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.detail-label {
  font-size: 12px;
  color: var(--color-text-secondary);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.detail-value {
  font-size: 16px;
  font-weight: 700;
  color: var(--color-text-primary);
}

/* 实时数据网格 */
.metrics-detail-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.metric-detail-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 18px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.8), rgba(255, 255, 255, 0.4));
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 14px;
  transition: all 0.3s;
}

.metric-detail-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

.metric-detail-info {
  flex: 1;
}

.metric-detail-label {
  font-size: 11px;
  color: var(--color-text-secondary);
  font-weight: 600;
  margin-bottom: 6px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.metric-detail-value {
  font-size: 24px;
  font-weight: 800;
  color: var(--color-text-primary);
  line-height: 1;
  font-variant-numeric: tabular-nums;
}

/* 预估信息 */
.estimate-info {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-top: 16px;
}

.estimate-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 14px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 10px;
}

.estimate-label {
  font-size: 12px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.estimate-value {
  font-size: 18px;
  font-weight: 800;
  color: var(--color-text-primary);
}

/* 信息表格 */
.info-table {
  display: flex;
  flex-direction: column;
  gap: 1px;
  background: rgba(0, 0, 0, 0.06);
  border-radius: 12px;
  overflow: hidden;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 18px;
  background: rgba(255, 255, 255, 0.8);
  transition: background 0.2s;
}

.info-row:hover {
  background: rgba(255, 255, 255, 1);
}

.info-label {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.info-value {
  font-size: 14px;
  font-weight: 700;
  color: var(--color-text-primary);
}

.info-value.status-good {
  color: #34C759;
}

/* 弹窗底部 */
.dialog-footer {
  display: flex;
  gap: 12px;
  padding: 24px 32px;
  border-top: 2px solid rgba(0, 0, 0, 0.06);
  background: rgba(0, 0, 0, 0.02);
}

.btn-secondary {
  flex: 1;
  padding: 14px 24px;
  background: rgba(28, 28, 30, 0.06);
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  font-size: 15px;
  font-weight: 600;
  color: var(--color-text-primary);
  cursor: pointer;
  transition: all 0.3s;
}

.btn-secondary:hover {
  background: rgba(28, 28, 30, 0.12);
  transform: translateY(-2px);
}

.btn-primary {
  flex: 1;
  padding: 14px 24px;
  background: linear-gradient(135deg, #34C759, #30D158);
  border: none;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 700;
  color: white;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(52, 199, 89, 0.3);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(52, 199, 89, 0.4);
}

/* 弹窗响应式 */
@media (max-width: 768px) {
  .dialog-content {
    width: 95%;
    max-height: 95vh;
  }

  .dialog-header {
    padding: 20px 24px;
  }

  .dialog-body {
    padding: 24px;
  }

  .detail-grid {
    grid-template-columns: 1fr;
  }

  .metrics-detail-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .estimate-info {
    grid-template-columns: 1fr;
  }

  .dialog-footer {
    padding: 20px 24px;
  }
}
</style>

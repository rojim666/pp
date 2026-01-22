<template>
  <div class="dashboard">
    <h1 class="page-title">充电桩记录展示</h1>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">平均充电时长 (分)</span>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ stats?.averageChargingTime?.toFixed(0) || 0 }}</div>
        </div>
        <div class="mini-chart">
          <svg viewBox="0 0 100 30" class="line-chart">
            <polyline 
              points="0,20 20,15 40,18 60,10 80,12 100,15" 
              fill="none" 
              stroke="#4a90e2" 
              stroke-width="2"
            />
          </svg>
        </div>
      </div>

      <div class="stat-card highlight-green">
        <div class="stat-header">
          <span class="stat-label">今日充电电量 (kWh)</span>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ stats?.todayEnergy?.toFixed(1) || 0 }}</div>
        </div>
        <div class="mini-chart">
          <svg viewBox="0 0 100 30" class="bar-chart">
            <rect x="5" y="15" width="8" height="15" fill="#5ec98f" opacity="0.6"/>
            <rect x="18" y="10" width="8" height="20" fill="#5ec98f" opacity="0.7"/>
            <rect x="31" y="8" width="8" height="22" fill="#5ec98f" opacity="0.8"/>
            <rect x="44" y="5" width="8" height="25" fill="#5ec98f"/>
            <rect x="57" y="12" width="8" height="18" fill="#5ec98f" opacity="0.7"/>
            <rect x="70" y="10" width="8" height="20" fill="#5ec98f" opacity="0.8"/>
          </svg>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">今日收入 (元)</span>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ stats?.todayRevenue?.toFixed(2) || 0 }}</div>
        </div>
        <div class="mini-chart">
          <svg viewBox="0 0 100 30" class="line-chart">
            <polyline 
              points="0,15 20,18 40,12 60,16 80,10 100,14" 
              fill="none" 
              stroke="#4a90e2" 
              stroke-width="2"
            />
          </svg>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">充电中桩数</span>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ stats?.chargingStations || 0 }}</div>
        </div>
        <div class="mini-chart">
          <svg viewBox="0 0 40 40" class="donut-chart">
            <circle cx="20" cy="20" r="15" fill="none" stroke="#e0e0e0" stroke-width="8"/>
            <circle 
              cx="20" 
              cy="20" 
              r="15" 
              fill="none" 
              stroke="#4a90e2" 
              stroke-width="8"
              stroke-dasharray="70 100"
              transform="rotate(-90 20 20)"
            />
          </svg>
        </div>
      </div>
    </div>

    <!-- 充电设备图表 -->
    <div class="chart-section">
      <div class="section-header">
        <h2>充电设备</h2>
      </div>
      <div class="chart-container" ref="deviceChartRef"></div>
    </div>

    <!-- 底部三个模块 -->
    <div class="bottom-grid">
      <!-- 快速操作 -->
      <div class="card">
        <h3 class="card-title">快速操作</h3>
        <div class="chart-container" ref="operationChartRef"></div>
      </div>

      <!-- 状态指示 -->
      <div class="card">
        <h3 class="card-title">状态指示灯</h3>
        <div class="chart-container" ref="statusChartRef"></div>
      </div>

      <!-- 帮助信息 -->
      <div class="card">
        <div class="card-header">
          <h3 class="card-title">帮助信息</h3>
          <a href="#" class="more-link">查看更多</a>
        </div>
        <div class="help-list">
          <div class="help-item" v-for="(item, index) in helpItems" :key="index">
            <span class="help-badge" :class="item.type">{{ item.badge }}</span>
            <span class="help-text">{{ item.text }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'
import * as echarts from 'echarts'
import { useDashboardStore } from '../stores/dashboard'
import { storeToRefs } from 'pinia'

const dashboardStore = useDashboardStore()
const { stats, loading } = storeToRefs(dashboardStore)

const deviceChartRef = ref(null)
const operationChartRef = ref(null)
const statusChartRef = ref(null)
let deviceChart = null
let statusChart = null

const helpItems = [
  { badge: '须知', type: 'warning', text: '识别用户前请出的充电桩' },
  { badge: '须知', type: 'info', text: '充电桩在维护前请提，将影响该车区小时内' },
  { badge: '须知', type: 'info', text: '断网门户遭到搁置，可适宜宣为处以提供该本' },
  { badge: '建议', type: 'success', text: '昨的充电桩故障，正在' },
  { badge: '建议', type: 'success', text: '最新充电桩操作统等' },
  { badge: '建议', type: 'success', text: 'T线充电桩故障，旧因解释' },
  { badge: '提醒', type: 'danger', text: '关注充电安全知识前，保留昨顿的一次布使' }
]

onMounted(() => {
  dashboardStore.fetchDashboardStats()
})

watch(stats, (newStats) => {
  if (newStats) {
    nextTick(() => {
      initDeviceChart()
      initOperationChart()
      initStatusChart()
    })
  }
})

const initDeviceChart = () => {
  if (!deviceChartRef.value || !stats.value) return
  
  if (deviceChart) deviceChart.dispose()
  deviceChart = echarts.init(deviceChartRef.value)
  
  const dates = stats.value.energyTrend.map(item => item.date.substring(5)) // MM-DD
  const energies = stats.value.energyTrend.map(item => item.energy)
  
  const option = {
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLine: {
        lineStyle: { color: '#ddd' }
      },
      axisLabel: {
        color: '#666'
      }
    },
    yAxis: {
      type: 'value',
      splitLine: {
        lineStyle: { color: '#f0f0f0' }
      },
      axisLabel: {
        color: '#666'
      }
    },
    series: [
      {
        data: energies,
        type: 'bar',
        itemStyle: {
          color: '#5ec98f',
          borderRadius: [4, 4, 0, 0]
        },
        barWidth: '40%'
      }
    ]
  }
  
  deviceChart.setOption(option)
}

const initOperationChart = () => {
    // Keep placeholder or implement if data available
    if (!operationChartRef.value) return
}

const initStatusChart = () => {
  if (!statusChartRef.value || !stats.value) return
  
  if (statusChart) statusChart.dispose()
  statusChart = echarts.init(statusChartRef.value)
  
  const data = stats.value.stationStatusDistribution.map(item => ({
      value: item.count,
      name: item.status
  }))

  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center'
    },
    series: [
      {
        name: '充电桩状态',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: data
      }
    ]
  }
  
  statusChart.setOption(option)
}
</script>

<style scoped>
.dashboard {
  max-width: 1400px;
  animation: fadeIn 0.4s ease-out;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: var(--spacing-lg);
  color: var(--color-text-primary);
  letter-spacing: -0.5px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.stat-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-radius: var(--radius-lg);
  padding: var(--spacing-lg);
  border: 1px solid var(--color-separator);
  position: relative;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: var(--shadow-sm);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: rgba(0, 122, 255, 0.2);
}

.stat-card.highlight-green {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.1) 0%, rgba(52, 199, 89, 0.05) 100%);
  border-color: rgba(52, 199, 89, 0.2);
}

.stat-card.highlight-green:hover {
  border-color: rgba(52, 199, 89, 0.4);
}

.stat-header {
  margin-bottom: 12px;
}

.stat-label {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 500;
}

.stat-content {
  margin-bottom: var(--spacing-md);
}

.stat-value {
  font-size: 36px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 8px;
  letter-spacing: -1px;
}

.stat-trend {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  padding: 4px 10px;
  border-radius: var(--radius-sm);
  font-weight: 500;
}

.stat-trend.positive {
  color: var(--color-success);
  background: rgba(52, 199, 89, 0.1);
}

.stat-trend.negative {
  color: var(--color-danger);
  background: rgba(255, 59, 48, 0.1);
}

.trend-value {
  font-weight: 600;
}

.mini-chart {
  height: 50px;
  opacity: 0.7;
  margin-top: 8px;
}

.mini-chart svg {
  width: 100%;
  height: 100%;
}

.chart-section {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-radius: var(--radius-lg);
  padding: var(--spacing-lg);
  margin-bottom: var(--spacing-lg);
  border: 1px solid var(--color-separator);
  box-shadow: var(--shadow-sm);
}

.section-header {
  margin-bottom: var(--spacing-lg);
}

.section-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: var(--color-text-primary);
  letter-spacing: -0.3px;
}

.chart-container {
  height: 320px;
  width: 100%;
}

.bottom-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: var(--spacing-md);
}

.card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-radius: var(--radius-lg);
  padding: var(--spacing-lg);
  border: 1px solid var(--color-separator);
  box-shadow: var(--shadow-sm);
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: var(--shadow-md);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
}

.card-title {
  font-size: 17px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: var(--spacing-md);
  letter-spacing: -0.3px;
}

.more-link {
  font-size: 13px;
  color: var(--color-primary);
  text-decoration: none;
  font-weight: 500;
  transition: all 0.2s;
}

.more-link:hover {
  opacity: 0.7;
}

.help-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.help-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  font-size: 13px;
  line-height: 1.6;
  padding: 10px;
  border-radius: var(--radius-sm);
  transition: all 0.2s;
}

.help-item:hover {
  background: rgba(0, 0, 0, 0.02);
}

.help-badge {
  padding: 3px 10px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 600;
  white-space: nowrap;
  letter-spacing: 0.3px;
}

.help-badge.warning {
  background: rgba(255, 149, 0, 0.15);
  color: var(--color-warning);
}

.help-badge.info {
  background: rgba(0, 122, 255, 0.15);
  color: var(--color-primary);
}

.help-badge.success {
  background: rgba(52, 199, 89, 0.15);
  color: var(--color-success);
}

.help-badge.danger {
  background: rgba(255, 59, 48, 0.15);
  color: var(--color-danger);
}

.help-text {
  flex: 1;
  color: var(--color-text-primary);
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .bottom-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>

<template>
  <div class="page">
    <h1 class="page-title">充电费用统计</h1>

    <!-- 日期范围选择 -->
    <div class="card date-filter">
      <div class="filter-group">
        <div class="filter-item">
          <label>开始日期</label>
          <input type="date" v-model="dateRange.start" class="date-input" />
        </div>
        <div class="filter-item">
          <label>结束日期</label>
          <input type="date" v-model="dateRange.end" class="date-input" />
        </div>
        <div class="filter-item">
          <label>快捷选择</label>
          <select v-model="quickSelect" @change="handleQuickSelect" class="quick-select">
            <option value="">自定义</option>
            <option value="today">今天</option>
            <option value="week">本周</option>
            <option value="month">本月</option>
            <option value="quarter">本季度</option>
            <option value="year">本年</option>
          </select>
        </div>
        <button class="btn-primary" @click="refreshData">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 18px; height: 18px; margin-right: 6px;">
            <path d="M21 10C21 10 18.995 7.26822 17.3662 5.63824C15.7373 4.00827 13.4864 3 11 3C6.02944 3 2 7.02944 2 12C2 16.9706 6.02944 21 11 21C15.1031 21 18.5649 18.2543 19.6482 14.5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M21 10V4M21 10H15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          刷新数据
        </button>
      </div>
    </div>

    <!-- 总览统计 -->
    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #007AFF, #0051D5);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM12 20C7.59 20 4 16.41 4 12C4 7.59 7.59 4 12 4C16.41 4 20 7.59 20 12C20 16.41 16.41 20 12 20Z" fill="currentColor"/>
            <path d="M12.5 7H11V13L16.25 16.15L17 14.92L12.5 12.25V7Z" fill="currentColor"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ totalStats.revenue.toFixed(2) }}</div>
          <div class="stat-label">总收入 (元)</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #34C759, #30D158);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M13 2L3 14H12L11 22L21 10H12L13 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ totalStats.energy.toFixed(1) }}</div>
          <div class="stat-label">总用电量 (kWh)</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #FF9500, #FF6B00);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="3" y="4" width="18" height="18" rx="2" stroke="currentColor" stroke-width="2" fill="none"/>
            <path d="M3 10H21M8 14H8.01M12 14H12.01M16 14H16.01M8 18H8.01M12 18H12.01M16 18H16.01" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ totalStats.sessions }}</div>
          <div class="stat-label">充电次数</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #5856D6, #AF52DE);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M20 21V19C20 17.9391 19.5786 16.9217 18.8284 16.1716C18.0783 15.4214 17.0609 15 16 15H8C6.93913 15 5.92172 15.4214 5.17157 16.1716C4.42143 16.9217 4 17.9391 4 19V21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>
            <circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="2" fill="none"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ totalStats.users }}</div>
          <div class="stat-label">活跃用户</div>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-row">
      <!-- 收入趋势图 -->
      <div class="card chart-card">
        <div class="chart-header">
          <h3 class="chart-title">收入趋势</h3>
          <div class="chart-legend">
            <span class="legend-item">
              <span class="legend-dot" style="background: #007AFF;"></span>
              每日收入
            </span>
          </div>
        </div>
        <div class="chart-container">
          <div class="chart-bars">
            <div 
              v-for="(item, index) in revenueData" 
              :key="index"
              class="bar-item"
            >
              <div 
                class="bar" 
                :style="{ height: getBarHeight(item.amount, maxRevenue) + '%' }"
              >
                <div class="bar-tooltip">¥{{ item.amount.toFixed(2) }}</div>
              </div>
              <div class="bar-label">{{ item.date }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 用电量分布 -->
      <div class="card chart-card">
        <div class="chart-header">
          <h3 class="chart-title">用电量分布</h3>
        </div>
        <div class="chart-container">
          <div class="pie-chart">
            <svg viewBox="0 0 200 200" class="pie-svg">
              <circle cx="100" cy="100" r="80" fill="none" stroke="#E5E5EA" stroke-width="40"/>
              <circle 
                v-for="(segment, index) in pieSegments" 
                :key="index"
                cx="100" 
                cy="100" 
                r="80"
                fill="none"
                :stroke="segment.color"
                stroke-width="40"
                :stroke-dasharray="`${segment.percentage * 502.4 / 100} 502.4`"
                :stroke-dashoffset="segment.offset"
                transform="rotate(-90 100 100)"
                class="pie-segment"
              />
            </svg>
            <div class="pie-center">
              <div class="pie-total">{{ totalStats.energy.toFixed(1) }}</div>
              <div class="pie-label">总用电量<br/>kWh</div>
            </div>
          </div>
          <div class="pie-legend">
            <div 
              v-for="(item, index) in energyDistribution" 
              :key="index"
              class="pie-legend-item"
            >
              <span class="legend-color" :style="{ background: item.color }"></span>
              <span class="legend-name">{{ item.name }}</span>
              <span class="legend-value">{{ item.value.toFixed(1) }} kWh</span>
              <span class="legend-percent">{{ item.percentage.toFixed(1) }}%</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 充电站收入排行 -->
    <div class="card">
      <div class="section-header">
        <h3 class="section-title">充电站收入排行</h3>
        <div class="toolbar-right">
          <select v-model="sortBy" class="sort-select">
            <option value="revenue">按收入排序</option>
            <option value="energy">按用电量排序</option>
            <option value="sessions">按充电次数排序</option>
          </select>
        </div>
      </div>

      <div class="ranking-list">
        <div 
          v-for="(station, index) in sortedStations" 
          :key="station.id"
          class="ranking-item"
        >
          <div class="rank-badge" :class="getRankClass(index)">
            {{ index + 1 }}
          </div>
          <div class="station-info">
            <div class="station-name">{{ station.name }}</div>
            <div class="station-location">{{ station.location }}</div>
          </div>
          <div class="station-stats">
            <div class="stat-item">
              <span class="stat-label">收入</span>
              <span class="stat-value revenue">¥{{ station.revenue.toFixed(2) }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">用电量</span>
              <span class="stat-value energy">{{ station.energy.toFixed(1) }} kWh</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">充电次数</span>
              <span class="stat-value sessions">{{ station.sessions }} 次</span>
            </div>
          </div>
          <div class="progress-bar">
            <div 
              class="progress-fill" 
              :style="{ width: getProgressWidth(station.revenue, maxStationRevenue) + '%' }"
            ></div>
          </div>
        </div>
      </div>
    </div>

    <!-- 详细数据表格 -->
    <div class="card">
      <div class="section-header">
        <h3 class="section-title">详细充电记录</h3>
        <div class="toolbar-right">
          <button class="btn-secondary" @click="exportData">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 16px; height: 16px; margin-right: 6px;">
              <path d="M21 15V19C21 19.5304 20.7893 20.0391 20.4142 20.4142C20.0391 20.7893 19.5304 21 19 21H5C4.46957 21 3.96086 20.7893 3.58579 20.4142C3.21071 20.0391 3 19.5304 3 19V15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M7 10L12 15L17 10" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M12 15V3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            导出数据
          </button>
        </div>
      </div>

      <div class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th>日期</th>
              <th>充电站</th>
              <th>用户</th>
              <th>充电时长</th>
              <th>用电量 (kWh)</th>
              <th>单价 (元/kWh)</th>
              <th>费用 (元)</th>
              <th>支付方式</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="record in detailedRecords" :key="record.id">
              <td class="date-col">{{ record.date }}</td>
              <td class="station-col">{{ record.station }}</td>
              <td class="user-col">{{ record.user }}</td>
              <td class="duration-col">{{ record.duration }}</td>
              <td class="energy-col">{{ record.energy.toFixed(2) }}</td>
              <td class="price-col">{{ record.price.toFixed(2) }}</td>
              <td class="cost-col">¥{{ record.cost.toFixed(2) }}</td>
              <td class="payment-col">
                <span class="payment-badge" :class="'payment-' + record.payment">
                  {{ getPaymentName(record.payment) }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRecordStore } from '../stores/record'
import { storeToRefs } from 'pinia'

const recordStore = useRecordStore()
const { records, loading } = storeToRefs(recordStore)

// 日期范围
const dateRange = ref({
  start: '',
  end: ''
})

const quickSelect = ref('month')

// 初始化日期为本月
const initDateRange = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  
  dateRange.value.start = `${year}-${month}-01`
  dateRange.value.end = `${year}-${month}-${day}`
}

// 快捷日期选择
const handleQuickSelect = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = now.getMonth()
  const day = now.getDate()
  
  switch (quickSelect.value) {
    case 'today':
      dateRange.value.start = dateRange.value.end = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      break
    case 'week':
      const weekStart = new Date(now.setDate(day - now.getDay() + 1))
      dateRange.value.start = `${weekStart.getFullYear()}-${String(weekStart.getMonth() + 1).padStart(2, '0')}-${String(weekStart.getDate()).padStart(2, '0')}`
      dateRange.value.end = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      break
    case 'month':
      dateRange.value.start = `${year}-${String(month + 1).padStart(2, '0')}-01`
      dateRange.value.end = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      break
    case 'quarter':
      const quarterMonth = Math.floor(month / 3) * 3
      dateRange.value.start = `${year}-${String(quarterMonth + 1).padStart(2, '0')}-01`
      dateRange.value.end = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      break
    case 'year':
      dateRange.value.start = `${year}-01-01`
      dateRange.value.end = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      break
  }
  refreshData()
}

// 刷新数据
const refreshData = async () => {
  await recordStore.fetchRecords({
    startTime: dateRange.value.start,
    endTime: dateRange.value.end,
    status: 'completed'
  })
}

// 总览统计数据
const totalStats = computed(() => {
  const completedRecords = records.value.filter(r => r.status === 'completed')
  return {
    revenue: completedRecords.reduce((sum, r) => sum + r.totalFee, 0),
    energy: completedRecords.reduce((sum, r) => sum + r.energy, 0),
    sessions: completedRecords.length,
    users: new Set(completedRecords.map(r => r.userId)).size
  }
})

// 收入趋势数据
const revenueData = computed(() => {
  const dailyMap = {}
  records.value.forEach(r => {
    if (r.status !== 'completed') return
    const date = r.startTime.substring(5, 10) // MM-DD
    if (!dailyMap[date]) dailyMap[date] = 0
    dailyMap[date] += r.totalFee
  })
  return Object.keys(dailyMap).sort().map(date => ({
    date,
    amount: dailyMap[date]
  }))
})

const maxRevenue = computed(() => {
  if (revenueData.value.length === 0) return 100
  return Math.max(...revenueData.value.map(d => d.amount))
})

const getBarHeight = (value, max) => {
  if (max === 0) return 0
  return (value / max) * 100
}

// 用电量分布
const energyDistribution = computed(() => {
  const total = totalStats.value.energy
  if (total === 0) return []
  
  return [
    { name: '全部充电', value: total, percentage: 100, color: '#007AFF' }
  ]
})

const pieSegments = computed(() => {
  let offset = 0
  return energyDistribution.value.map(item => {
    const segment = {
      percentage: item.percentage,
      color: item.color,
      offset: -offset
    }
    offset += item.percentage * 502.4 / 100
    return segment
  })
})

// 充电站排行
const sortBy = ref('revenue')

const stationData = computed(() => {
  const stationMap = {}
  records.value.forEach(r => {
    if (r.status !== 'completed') return
    if (!stationMap[r.stationId]) {
      stationMap[r.stationId] = {
        id: r.stationId,
        name: r.stationName,
        location: '未知位置',
        revenue: 0,
        energy: 0,
        sessions: 0
      }
    }
    stationMap[r.stationId].revenue += r.totalFee
    stationMap[r.stationId].energy += r.energy
    stationMap[r.stationId].sessions += 1
  })
  return Object.values(stationMap)
})

const sortedStations = computed(() => {
  return [...stationData.value].sort((a, b) => b[sortBy.value] - a[sortBy.value])
})

const maxStationRevenue = computed(() => {
  if (stationData.value.length === 0) return 100
  return Math.max(...stationData.value.map(s => s.revenue))
})

const getProgressWidth = (value, max) => {
  if (max === 0) return 0
  return (value / max) * 100
}

const getRankClass = (index) => {
  if (index === 0) return 'rank-1'
  if (index === 1) return 'rank-2'
  if (index === 2) return 'rank-3'
  return ''
}

// 详细记录
const detailedRecords = computed(() => {
  return records.value.map(r => ({
    id: r.id,
    date: r.startTime,
    station: r.stationName,
    user: r.userName,
    duration: r.duration + '分钟',
    energy: r.energy,
    price: r.totalFee / (r.energy || 1),
    cost: r.totalFee,
    payment: r.paymentMethod || 'wechat'
  }))
})

const getPaymentName = (type) => {
  const map = {
    wechat: '微信支付',
    alipay: '支付宝',
    card: '银行卡'
  }
  return map[type] || type
}

// 导出数据
const exportData = () => {
  console.log('导出数据')
  alert('导出功能开发中...')
}

onMounted(() => {
  initDateRange()
  refreshData()
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

/* 日期过滤器 */
.date-filter {
  margin-bottom: 24px;
}

.filter-group {
  display: flex;
  gap: 16px;
  align-items: flex-end;
  flex-wrap: wrap;
}

.filter-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.filter-item label {
  font-size: 13px;
  font-weight: 700;
  color: var(--color-text-primary);
  letter-spacing: 0.2px;
}

.date-input,
.quick-select {
  padding: 11px 16px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  background: rgba(255, 255, 255, 0.8);
  color: var(--color-text-primary);
  cursor: pointer;
  transition: all 0.3s;
  min-width: 160px;
}

.date-input:focus,
.quick-select:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(0, 122, 255, 0.1);
  background: white;
}

.btn-primary {
  padding: 12px 24px;
  background: linear-gradient(135deg, #007AFF, #0051D5);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 12px rgba(0, 122, 255, 0.3);
  display: flex;
  align-items: center;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 122, 255, 0.4);
}

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
  display: flex;
  align-items: center;
}

.btn-secondary:hover {
  background: rgba(28, 28, 30, 0.12);
  transform: translateY(-1px);
}

/* 统计卡片 */
.stats-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(40px);
  border-radius: 18px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  border: 1px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 13px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-icon svg {
  width: 26px;
  height: 26px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 26px;
  font-weight: 800;
  color: var(--color-text-primary);
  line-height: 1;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

/* 图表区域 */
.charts-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
}

.chart-card {
  min-height: 400px;
  display: flex;
  flex-direction: column;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
}

.chart-title {
  font-size: 18px;
  font-weight: 800;
  color: var(--color-text-primary);
  margin: 0;
  letter-spacing: -0.5px;
}

.chart-legend {
  display: flex;
  gap: 16px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  font-weight: 600;
  color: var(--color-text-secondary);
}

.legend-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}

.chart-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 柱状图 */
.chart-bars {
  width: 100%;
  display: flex;
  align-items: flex-end;
  justify-content: space-around;
  height: 280px;
  gap: 12px;
}

.bar-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
}

.bar {
  width: 100%;
  max-width: 60px;
  background: linear-gradient(180deg, #007AFF, #0051D5);
  border-radius: 8px 8px 0 0;
  position: relative;
  transition: all 0.3s;
  cursor: pointer;
  margin-top: auto;
}

.bar:hover {
  opacity: 0.8;
  transform: translateY(-4px);
}

.bar-tooltip {
  position: absolute;
  top: -32px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 700;
  white-space: nowrap;
  opacity: 0;
  transition: opacity 0.3s;
}

.bar:hover .bar-tooltip {
  opacity: 1;
}

.bar-label {
  margin-top: 8px;
  font-size: 12px;
  font-weight: 600;
  color: var(--color-text-secondary);
}

/* 饼图 */
.pie-chart {
  position: relative;
  width: 200px;
  height: 200px;
  margin-right: 32px;
}

.pie-svg {
  width: 100%;
  height: 100%;
}

.pie-segment {
  transition: all 0.3s;
  cursor: pointer;
}

.pie-segment:hover {
  opacity: 0.8;
}

.pie-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}

.pie-total {
  font-size: 28px;
  font-weight: 800;
  color: var(--color-text-primary);
  line-height: 1;
  margin-bottom: 4px;
}

.pie-label {
  font-size: 11px;
  color: var(--color-text-secondary);
  font-weight: 600;
  line-height: 1.3;
}

.pie-legend {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.pie-legend-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 8px;
  transition: all 0.2s;
}

.pie-legend-item:hover {
  background: rgba(0, 122, 255, 0.05);
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 4px;
  flex-shrink: 0;
}

.legend-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  flex: 1;
}

.legend-value {
  font-size: 13px;
  font-weight: 700;
  color: var(--color-text-primary);
}

.legend-percent {
  font-size: 12px;
  font-weight: 600;
  color: var(--color-text-secondary);
  min-width: 45px;
  text-align: right;
}

/* 排行榜 */
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

.toolbar-right {
  display: flex;
  gap: 12px;
}

.sort-select {
  padding: 10px 16px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  background: rgba(255, 255, 255, 0.8);
  color: var(--color-text-primary);
  cursor: pointer;
  transition: all 0.3s;
}

.sort-select:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(0, 122, 255, 0.1);
}

.ranking-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.ranking-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 14px;
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
}

.ranking-item:hover {
  background: rgba(0, 122, 255, 0.05);
  transform: translateX(4px);
}

.rank-badge {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 800;
  background: rgba(142, 142, 147, 0.12);
  color: #8E8E93;
  flex-shrink: 0;
}

.rank-badge.rank-1 {
  background: linear-gradient(135deg, #FFD700, #FFA500);
  color: white;
  box-shadow: 0 4px 12px rgba(255, 215, 0, 0.4);
}

.rank-badge.rank-2 {
  background: linear-gradient(135deg, #C0C0C0, #A8A8A8);
  color: white;
  box-shadow: 0 4px 12px rgba(192, 192, 192, 0.4);
}

.rank-badge.rank-3 {
  background: linear-gradient(135deg, #CD7F32, #B87333);
  color: white;
  box-shadow: 0 4px 12px rgba(205, 127, 50, 0.4);
}

.station-info {
  min-width: 200px;
}

.station-name {
  font-size: 16px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 4px;
}

.station-location {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 500;
}

.station-stats {
  display: flex;
  gap: 32px;
  flex: 1;
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-label {
  font-size: 12px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.stat-value {
  font-size: 16px;
  font-weight: 800;
}

.stat-value.revenue {
  color: #007AFF;
}

.stat-value.energy {
  color: #34C759;
}

.stat-value.sessions {
  color: #FF9500;
}

.progress-bar {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: rgba(0, 0, 0, 0.05);
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #007AFF, #0051D5);
  transition: width 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 数据表格 */
.table-container {
  overflow-x: auto;
  border-radius: 12px;
  border: 1px solid rgba(0, 0, 0, 0.06);
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

.data-table thead {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.05), rgba(88, 86, 214, 0.05));
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
}

.data-table th {
  padding: 16px 14px;
  text-align: left;
  font-size: 12px;
  font-weight: 700;
  color: var(--color-text-primary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  white-space: nowrap;
}

.data-table tbody tr {
  border-bottom: 1px solid rgba(0, 0, 0, 0.04);
  transition: all 0.2s;
}

.data-table tbody tr:hover {
  background: rgba(0, 122, 255, 0.03);
}

.data-table td {
  padding: 14px;
  font-size: 14px;
  color: var(--color-text-primary);
  vertical-align: middle;
}

.date-col {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 600;
  white-space: nowrap;
}

.station-col {
  font-weight: 600;
}

.user-col {
  font-weight: 600;
}

.duration-col {
  font-family: 'Courier New', monospace;
  font-weight: 600;
  color: var(--color-text-secondary);
}

.energy-col,
.price-col {
  font-variant-numeric: tabular-nums;
  font-weight: 700;
  text-align: right;
}

.cost-col {
  font-variant-numeric: tabular-nums;
  font-weight: 800;
  color: #007AFF;
  font-size: 15px;
  text-align: right;
}

.payment-col {
  text-align: center;
}

.payment-badge {
  display: inline-block;
  padding: 5px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.3px;
}

.payment-wechat {
  background: linear-gradient(135deg, rgba(9, 187, 7, 0.12), rgba(9, 187, 7, 0.12));
  color: #09BB07;
  border: 1px solid rgba(9, 187, 7, 0.3);
}

.payment-alipay {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.12), rgba(88, 86, 214, 0.12));
  color: #007AFF;
  border: 1px solid rgba(0, 122, 255, 0.3);
}

.payment-card {
  background: linear-gradient(135deg, rgba(175, 82, 222, 0.12), rgba(191, 90, 242, 0.12));
  color: #AF52DE;
  border: 1px solid rgba(175, 82, 222, 0.3);
}

/* 响应式 */
@media (max-width: 1024px) {
  .charts-row {
    grid-template-columns: 1fr;
  }

  .filter-group {
    flex-direction: column;
    align-items: stretch;
  }

  .date-input,
  .quick-select {
    min-width: 0;
  }
}

@media (max-width: 768px) {
  .stats-row {
    grid-template-columns: repeat(2, 1fr);
  }

  .station-stats {
    flex-direction: column;
    gap: 12px;
  }

  .chart-container {
    flex-direction: column;
  }

  .pie-chart {
    margin-right: 0;
    margin-bottom: 24px;
  }
}
</style>

<template>
  <div class="page">
    <h1 class="page-title">充电使用统计</h1>
    <div class="card">
      <div class="chart-container" ref="chartRef"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import * as echarts from 'echarts'
import { useRecordStore } from '../stores/record'

const chartRef = ref(null)
const recordStore = useRecordStore()
let chart = null

// Process data for the chart
const chartData = computed(() => {
  const records = recordStore.records
  const dailyData = {}
  
  // Initialize last 7 days with 0
  const dates = []
  for (let i = 6; i >= 0; i--) {
    const d = new Date()
    d.setDate(d.getDate() - i)
    const dateStr = d.toISOString().split('T')[0]
    dailyData[dateStr] = 0
    dates.push(dateStr)
  }

  // Aggregate energy
  records.forEach(record => {
    // Use endTime or createdAt if endTime is missing
    const timeStr = record.endTime || record.createdAt
    if (timeStr) {
      const dateStr = timeStr.substring(0, 10)
      // Only count if it's within our 7 day window
      if (dailyData.hasOwnProperty(dateStr)) {
        // Parse "12.5 kWh" -> 12.5
        const energy = parseFloat(record.energy) || 0
        dailyData[dateStr] += energy
      }
    }
  })

  return {
    dates,
    values: dates.map(d => parseFloat(dailyData[d].toFixed(2)))
  }
})

const initChart = () => {
  if (chartRef.value) {
    // Dispose existing instance if any
    if (chart) {
      chart.dispose()
    }
    chart = echarts.init(chartRef.value)
    updateChart()
    window.addEventListener('resize', () => chart && chart.resize())
  }
}

const updateChart = () => {
  if (!chart) return
  
  const option = {
    title: {
      text: '近7日充电电量统计 (kWh)',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: chartData.value.dates,
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      name: '电量 (kWh)'
    },
    series: [{
      data: chartData.value.values,
      type: 'bar',
      barWidth: '40%',
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#83bff6' },
          { offset: 0.5, color: '#188df0' },
          { offset: 1, color: '#188df0' }
        ])
      },
      label: {
        show: true,
        position: 'top'
      }
    }]
  }
  chart.setOption(option)
}

onMounted(async () => {
  await recordStore.fetchRecords()
  initChart()
})

// Watch for data changes to update chart
watch(() => recordStore.records, () => {
  updateChart()
}, { deep: true })
</script>

<style scoped>
.page {
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

.card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-radius: var(--radius-lg);
  padding: var(--spacing-lg);
  border: 1px solid var(--color-separator);
  box-shadow: var(--shadow-sm);
}

.chart-container {
  height: 450px;
}
</style>

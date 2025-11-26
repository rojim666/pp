<template>
  <div class="page">
    <h1 class="page-title">充电使用统计</h1>
    <div class="card">
      <div class="chart-container" ref="chartRef"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const chartRef = ref(null)

onMounted(() => {
  if (chartRef.value) {
    const chart = echarts.init(chartRef.value)
    const option = {
      title: {
        text: '每日充电统计',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        data: [120, 200, 150, 80, 70, 110, 130],
        type: 'bar',
        itemStyle: {
          color: '#4a90e2'
        }
      }]
    }
    chart.setOption(option)
    window.addEventListener('resize', () => chart.resize())
  }
})
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

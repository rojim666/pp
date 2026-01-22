<template>
  <div class="page">
    <h1 class="page-title">充电记录查看</h1>
    <div class="card">
      <table class="records-table">
        <thead>
          <tr>
            <th>记录ID</th>
            <th>充电桩</th>
            <th>开始时间</th>
            <th>结束时间</th>
            <th>充电量(kWh)</th>
            <th>费用(元)</th>
            <th>状态</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="loading">
            <td colspan="7" style="text-align: center; padding: 20px;">加载中...</td>
          </tr>
          <tr v-else-if="records.length === 0">
            <td colspan="7" style="text-align: center; padding: 20px;">暂无数据</td>
          </tr>
          <tr v-else v-for="record in records" :key="record.id">
            <td>{{ record.id }}</td>
            <td>{{ record.stationName }}</td>
            <td>{{ record.startTime }}</td>
            <td>{{ record.endTime || '-' }}</td>
            <td>{{ record.energy }}</td>
            <td>{{ record.totalFee }}</td>
            <td>
              <span class="status-badge" :class="record.status">
                {{ getStatusText(record.status) }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRecordStore } from '../stores/record'
import { storeToRefs } from 'pinia'

const recordStore = useRecordStore()
const { records, loading } = storeToRefs(recordStore)

onMounted(() => {
  recordStore.fetchRecords()
})

const getStatusText = (status) => {
  const map = {
    'completed': '已完成',
    'charging': '充电中',
    'cancelled': '已取消',
    'failed': '失败'
  }
  return map[status] || status
}
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
  overflow-x: auto;
}

.records-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
}

.records-table th {
  background: rgba(0, 0, 0, 0.02);
  padding: 14px 16px;
  text-align: left;
  font-weight: 600;
  color: var(--color-text-secondary);
  border-bottom: 1px solid var(--color-separator);
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.records-table th:first-child {
  border-top-left-radius: var(--radius-sm);
}

.records-table th:last-child {
  border-top-right-radius: var(--radius-sm);
}

.records-table td {
  padding: 16px;
  border-bottom: 1px solid var(--color-separator);
  color: var(--color-text-primary);
  font-size: 14px;
}

.records-table tr:last-child td {
  border-bottom: none;
}

.records-table tr:hover {
  background: rgba(0, 122, 255, 0.03);
}

.status-badge {
  padding: 5px 14px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  display: inline-block;
}

.status-badge.completed {
  background: rgba(52, 199, 89, 0.15);
  color: var(--color-success);
}

.status-badge.charging {
  background: rgba(0, 122, 255, 0.15);
  color: var(--color-primary);
}
</style>

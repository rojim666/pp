<template>
  <div class="page">
    <h1 class="page-title">设备状态监控</h1>
    
    <!-- Status Summary -->
    <div class="status-summary">
      <div class="summary-card total">
        <div class="label">总设备数</div>
        <div class="value">{{ stationStore.totalStations }}</div>
      </div>
      <div class="summary-card available">
        <div class="label">空闲</div>
        <div class="value">{{ stationStore.availableStations }}</div>
      </div>
      <div class="summary-card charging">
        <div class="label">充电中</div>
        <div class="value">{{ stationStore.chargingStations }}</div>
      </div>
      <div class="summary-card fault">
        <div class="label">故障</div>
        <div class="value">{{ stationStore.faultStations }}</div>
      </div>
      <div class="summary-card offline">
        <div class="label">离线</div>
        <div class="value">{{ stationStore.offlineStations }}</div>
      </div>
    </div>

    <!-- Station Grid -->
    <div class="station-grid">
      <div 
        v-for="station in stationStore.stations" 
        :key="station.id" 
        class="station-card"
        :class="station.status"
      >
        <div class="station-header">
          <span class="station-name">{{ station.name }}</span>
          <span class="station-status">{{ getStatusText(station.status) }}</span>
        </div>
        <div class="station-body">
          <div class="info-row">
            <span class="label">位置:</span>
            <span class="value">{{ station.location }}</span>
          </div>
          <div class="info-row">
            <span class="label">类型:</span>
            <span class="value">{{ station.type === 'fast' ? '快充' : '慢充' }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useStationStore } from '../stores/station'

const stationStore = useStationStore()

const getStatusText = (status) => {
  const map = {
    available: '空闲',
    charging: '充电中',
    fault: '故障',
    offline: '离线'
  }
  return map[status] || status
}

onMounted(() => {
  stationStore.fetchStations()
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
}

.status-summary {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.summary-card {
  background: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
  text-align: center;
}

.summary-card .label {
  color: #666;
  font-size: 14px;
  margin-bottom: 8px;
}

.summary-card .value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.summary-card.available .value { color: #52c41a; }
.summary-card.charging .value { color: #1890ff; }
.summary-card.fault .value { color: #ff4d4f; }
.summary-card.offline .value { color: #999; }

.station-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.station-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
  border-left: 4px solid transparent;
  transition: transform 0.2s;
}

.station-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0,0,0,0.1);
}

.station-card.available { border-left-color: #52c41a; }
.station-card.charging { border-left-color: #1890ff; }
.station-card.fault { border-left-color: #ff4d4f; }
.station-card.offline { border-left-color: #999; }

.station-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.station-name {
  font-weight: bold;
  font-size: 16px;
}

.station-status {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
  background: #f5f5f5;
}

.station-card.available .station-status { color: #52c41a; background: #f6ffed; }
.station-card.charging .station-status { color: #1890ff; background: #e6f7ff; }
.station-card.fault .station-status { color: #ff4d4f; background: #fff1f0; }

.info-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
}

.info-row .label {
  color: #999;
}
</style>

import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { DashboardStats, Statistics } from '../types'
import { ChargingStationStatus, ChargingRecordStatus } from '../types'
import { mockDB } from '../mock/database'
import { formatDate } from '../utils'

export const useDashboardStore = defineStore('dashboard', () => {
  // 状态
  const stats = ref<DashboardStats | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)
  
  // Actions
  async function fetchDashboardStats() {
    loading.value = true
    error.value = null
    try {
      await new Promise(resolve => setTimeout(resolve, 300))
      
      const stations = mockDB.getStations()
      const records = mockDB.getRecords()
      
      // 今日数据
      const today = formatDate(new Date())
      const todayRecords = records.filter(r => 
        r.startTime.startsWith(today) && 
        r.status === ChargingRecordStatus.COMPLETED
      )
      
      const todayEnergy = todayRecords.reduce((sum, r) => sum + r.energy, 0)
      const todayRevenue = todayRecords.reduce((sum, r) => sum + r.totalFee, 0)
      const todayChargingCount = todayRecords.length
      
      // 平均充电时长
      const completedRecords = records.filter(r => r.status === ChargingRecordStatus.COMPLETED)
      const averageChargingTime = completedRecords.length > 0
        ? completedRecords.reduce((sum, r) => sum + r.duration, 0) / completedRecords.length
        : 0
      
      // 充电桩状态分布
      const statusDistribution = [
        {
          status: ChargingStationStatus.ONLINE,
          count: stations.filter(s => s.status === ChargingStationStatus.ONLINE).length,
          percentage: 0
        },
        {
          status: ChargingStationStatus.CHARGING,
          count: stations.filter(s => s.status === ChargingStationStatus.CHARGING).length,
          percentage: 0
        },
        {
          status: ChargingStationStatus.OFFLINE,
          count: stations.filter(s => s.status === ChargingStationStatus.OFFLINE).length,
          percentage: 0
        },
        {
          status: ChargingStationStatus.MAINTENANCE,
          count: stations.filter(s => s.status === ChargingStationStatus.MAINTENANCE).length,
          percentage: 0
        }
      ]
      
      statusDistribution.forEach(item => {
        item.percentage = stations.length > 0 
          ? (item.count / stations.length) * 100 
          : 0
      })
      
      // 能量趋势（最近7天）
      const energyTrend: Array<{ date: string; energy: number; revenue: number }> = []
      for (let i = 6; i >= 0; i--) {
        const date = new Date()
        date.setDate(date.getDate() - i)
        const dateStr = formatDate(date)
        
        const dayRecords = records.filter(r => 
          r.startTime.startsWith(dateStr) && 
          r.status === ChargingRecordStatus.COMPLETED
        )
        
        energyTrend.push({
          date: dateStr,
          energy: dayRecords.reduce((sum, r) => sum + r.energy, 0),
          revenue: dayRecords.reduce((sum, r) => sum + r.totalFee, 0)
        })
      }
      
      stats.value = {
        totalStations: stations.length,
        onlineStations: stations.filter(s => s.status === ChargingStationStatus.ONLINE).length,
        chargingStations: stations.filter(s => s.status === ChargingStationStatus.CHARGING).length,
        offlineStations: stations.filter(s => s.status === ChargingStationStatus.OFFLINE).length,
        todayEnergy,
        todayRevenue,
        todayChargingCount,
        averageChargingTime,
        recentRecords: records.slice(0, 10),
        stationStatusDistribution: statusDistribution,
        energyTrend
      }
    } catch (e: any) {
      error.value = e.message
      console.error('Failed to fetch dashboard stats:', e)
    } finally {
      loading.value = false
    }
  }
  
  return {
    // State
    stats,
    loading,
    error,
    // Actions
    fetchDashboardStats
  }
})

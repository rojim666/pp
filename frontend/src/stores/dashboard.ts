import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { DashboardStats, Statistics } from '../types'
import { ChargingStationStatus, ChargingRecordStatus } from '../types'
import { dashboardAPI } from '../api'
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
      const res = await dashboardAPI.getStats()
      stats.value = res
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

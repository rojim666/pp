import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { ChargingStation, DashboardStats } from '../types'
import { ChargingStationStatus } from '../types'
import { stationAPI } from '../api'

export const useStationStore = defineStore('station', () => {
  // 状态
  const stations = ref<ChargingStation[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  
  // 计算属性
  const totalCount = computed(() => stations.value.length)
  
  const onlineCount = computed(() => 
    stations.value.filter(s => s.status === ChargingStationStatus.ONLINE).length
  )
  
  const chargingCount = computed(() => 
    stations.value.filter(s => s.status === ChargingStationStatus.CHARGING).length
  )
  
  const offlineCount = computed(() => 
    stations.value.filter(s => s.status === ChargingStationStatus.OFFLINE).length
  )
  
  const maintenanceCount = computed(() => 
    stations.value.filter(s => s.status === ChargingStationStatus.MAINTENANCE).length
  )
  
  const totalEnergy = computed(() => 
    stations.value.reduce((sum, s) => sum + s.totalEnergy, 0)
  )
  
  const totalRevenue = computed(() => 
    stations.value.reduce((sum, s) => sum + s.totalRevenue, 0)
  )
  
  // Actions
  async function fetchStations() {
    loading.value = true
    error.value = null
    try {
      const res = await stationAPI.list()
      stations.value = res?.records ?? []
    } catch (e: any) {
      error.value = e.message
      console.error('Failed to fetch stations:', e)
    } finally {
      loading.value = false
    }
  }
  
  async function getStationById(id: string): Promise<ChargingStation | null> {
    try {
      const res = await stationAPI.get(id)
      return res
    } catch (e: any) {
      error.value = e.message
      return null
    }
  }
  
  async function createStation(station: ChargingStation): Promise<ChargingStation | null> {
    loading.value = true
    try {
      const res = await stationAPI.create(station)
      const created = res
      stations.value.push(created)
      return created
    } catch (e: any) {
      error.value = e.message
      return null
    } finally {
      loading.value = false
    }
  }
  
  async function updateStation(id: string, data: Partial<ChargingStation>): Promise<boolean> {
    loading.value = true
    try {
      const res = await stationAPI.update(id, data)
      const updated = res
      if (updated) {
        const index = stations.value.findIndex(s => s.id === id)
        if (index !== -1) {
          stations.value[index] = updated
        }
        return true
      }
      return false
    } catch (e: any) {
      error.value = e.message
      return false
    } finally {
      loading.value = false
    }
  }
  
  async function deleteStation(id: string): Promise<boolean> {
    loading.value = true
    try {
      await stationAPI.delete(id)
      const index = stations.value.findIndex(s => s.id === id)
      if (index !== -1) {
        stations.value.splice(index, 1)
      }
      return true
    } catch (e: any) {
      error.value = e.message
      return false
    } finally {
      loading.value = false
    }
  }
  
  return {
    // State
    stations,
    loading,
    error,
    // Getters
    totalCount,
    onlineCount,
    chargingCount,
    offlineCount,
    maintenanceCount,
    totalEnergy,
    totalRevenue,
    // Actions
    fetchStations,
    getStationById,
    createStation,
    updateStation,
    deleteStation
  }
})

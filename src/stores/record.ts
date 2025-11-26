import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { ChargingRecord } from '../types'
import { ChargingRecordStatus } from '../types'
import { mockDB } from '../mock/database'

export const useRecordStore = defineStore('record', () => {
  // 状态
  const records = ref<ChargingRecord[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  
  // 计算属性
  const totalCount = computed(() => records.value.length)
  
  const chargingCount = computed(() => 
    records.value.filter(r => r.status === ChargingRecordStatus.CHARGING).length
  )
  
  const completedCount = computed(() => 
    records.value.filter(r => r.status === ChargingRecordStatus.COMPLETED).length
  )
  
  const totalEnergy = computed(() => 
    records.value.reduce((sum, r) => sum + r.energy, 0)
  )
  
  const totalRevenue = computed(() => 
    records.value.reduce((sum, r) => sum + r.totalFee, 0)
  )
  
  const recentRecords = computed(() => 
    records.value.slice(0, 10)
  )
  
  // Actions
  async function fetchRecords() {
    loading.value = true
    error.value = null
    try {
      await new Promise(resolve => setTimeout(resolve, 300))
      records.value = mockDB.getRecords()
    } catch (e: any) {
      error.value = e.message
      console.error('Failed to fetch records:', e)
    } finally {
      loading.value = false
    }
  }
  
  async function getRecordById(id: string): Promise<ChargingRecord | null> {
    try {
      await new Promise(resolve => setTimeout(resolve, 100))
      return mockDB.getRecordById(id)
    } catch (e: any) {
      error.value = e.message
      return null
    }
  }
  
  async function getRecordsByUserId(userId: string): Promise<ChargingRecord[]> {
    try {
      await new Promise(resolve => setTimeout(resolve, 100))
      return mockDB.getRecordsByUserId(userId)
    } catch (e: any) {
      error.value = e.message
      return []
    }
  }
  
  async function getRecordsByStationId(stationId: string): Promise<ChargingRecord[]> {
    try {
      await new Promise(resolve => setTimeout(resolve, 100))
      return mockDB.getRecordsByStationId(stationId)
    } catch (e: any) {
      error.value = e.message
      return []
    }
  }
  
  async function createRecord(record: ChargingRecord): Promise<ChargingRecord | null> {
    loading.value = true
    try {
      await new Promise(resolve => setTimeout(resolve, 300))
      const created = mockDB.createRecord(record)
      records.value.unshift(created)
      return created
    } catch (e: any) {
      error.value = e.message
      return null
    } finally {
      loading.value = false
    }
  }
  
  async function updateRecord(id: string, data: Partial<ChargingRecord>): Promise<boolean> {
    loading.value = true
    try {
      await new Promise(resolve => setTimeout(resolve, 300))
      const updated = mockDB.updateRecord(id, data)
      if (updated) {
        const index = records.value.findIndex(r => r.id === id)
        if (index !== -1) {
          records.value[index] = updated
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
  
  async function deleteRecord(id: string): Promise<boolean> {
    loading.value = true
    try {
      await new Promise(resolve => setTimeout(resolve, 300))
      const success = mockDB.deleteRecord(id)
      if (success) {
        const index = records.value.findIndex(r => r.id === id)
        if (index !== -1) {
          records.value.splice(index, 1)
        }
      }
      return success
    } catch (e: any) {
      error.value = e.message
      return false
    } finally {
      loading.value = false
    }
  }
  
  return {
    // State
    records,
    loading,
    error,
    // Getters
    totalCount,
    chargingCount,
    completedCount,
    totalEnergy,
    totalRevenue,
    recentRecords,
    // Actions
    fetchRecords,
    getRecordById,
    getRecordsByUserId,
    getRecordsByStationId,
    createRecord,
    updateRecord,
    deleteRecord
  }
})

import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { ChargingRecord } from '../types'
import { ChargingRecordStatus } from '../types'
import { recordAPI } from '../api'

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
      const res = await recordAPI.list()
      records.value = res?.records ?? []
    } catch (e: any) {
      error.value = e.message
      console.error('Failed to fetch records:', e)
    } finally {
      loading.value = false
    }
  }
  
  async function getRecordById(id: string): Promise<ChargingRecord | null> {
    try {
      const res = await recordAPI.get(id)
      return res
    } catch (e: any) {
      error.value = e.message
      return null
    }
  }
  
  async function getRecordsByUserId(userId: string): Promise<ChargingRecord[]> {
    try {
      const res = await recordAPI.list({ userId })
      return res?.records ?? res ?? []
    } catch (e: any) {
      error.value = e.message
      return []
    }
  }
  
  async function getRecordsByStationId(stationId: string): Promise<ChargingRecord[]> {
    try {
      const res = await recordAPI.list({ stationId })
      return res?.records ?? res ?? []
    } catch (e: any) {
      error.value = e.message
      return []
    }
  }
  
  async function createRecord(record: ChargingRecord): Promise<ChargingRecord | null> {
    loading.value = true
    try {
      const res = await recordAPI.start(record)
      const created = res
      records.value.unshift(created)
      return created
    } catch (e: any) {
      error.value = e.message
      return null
    } finally {
      loading.value = false
    }
  }
  
  // updateRecord removed as it is not supported by API

  
  // deleteRecord removed as it is not supported by API

  
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
    createRecord
  }
})

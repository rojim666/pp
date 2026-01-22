import type {
  ChargingStation,
  ChargingRecord,
  User,
  TariffConfig,
  SystemLog,
  PaginationParams,
  PaginationResponse
} from '../types'
import {
  generateMockStations,
  generateMockRecords,
  generateMockUsers,
  generateMockTariffs,
  generateMockLogs
} from './generator'
import { storage, deepClone } from '../utils'

/**
 * Mock数据库类
 */
class MockDatabase {
  private stations: ChargingStation[] = []
  private records: ChargingRecord[] = []
  private users: User[] = []
  private tariffs: TariffConfig[] = []
  private logs: SystemLog[] = []
  
  private readonly STORAGE_KEY = 'charging_station_db'
  
  constructor() {
    this.loadFromStorage()
    // If either stations or users are missing in storage, re-initialize.
    // (Previously we only checked stations; if storage had stations but no users,
    // quick-login could fail because the users array was empty.)
    if (this.stations.length === 0 || this.users.length === 0) {
      this.initialize()
    }
  }
  
  /**
   * 初始化数据库
   */
  private initialize(): void {
    console.log('Initializing mock database...')
    this.users = generateMockUsers(50)
    this.stations = generateMockStations(50)
    this.records = generateMockRecords(200, this.stations, this.users)
    this.tariffs = generateMockTariffs()
    this.logs = generateMockLogs(100)
    this.saveToStorage()
  }
  
  /**
   * 从LocalStorage加载数据
   */
  private loadFromStorage(): void {
    const data = storage.get<any>(this.STORAGE_KEY)
    if (data) {
      this.stations = data.stations || []
      this.records = data.records || []
      this.users = data.users || []
      this.tariffs = data.tariffs || []
      this.logs = data.logs || []
      console.log('Loaded data from storage')
    }
  }
  
  /**
   * 保存数据到LocalStorage
   */
  private saveToStorage(): void {
    storage.set(this.STORAGE_KEY, {
      stations: this.stations,
      records: this.records,
      users: this.users,
      tariffs: this.tariffs,
      logs: this.logs
    })
  }
  
  /**
   * 清空数据库
   */
  clear(): void {
    this.stations = []
    this.records = []
    this.users = []
    this.tariffs = []
    this.logs = []
    storage.remove(this.STORAGE_KEY)
  }
  
  /**
   * 重置数据库
   */
  reset(): void {
    this.clear()
    this.initialize()
  }
  
  // ==================== 充电桩相关 ====================
  
  /**
   * 获取所有充电桩
   */
  getStations(): ChargingStation[] {
    return deepClone(this.stations)
  }
  
  /**
   * 分页获取充电桩
   */
  getStationsPaginated(params: PaginationParams): PaginationResponse<ChargingStation> {
    const { page, pageSize } = params
    const start = (page - 1) * pageSize
    const end = start + pageSize
    const data = this.stations.slice(start, end)
    
    return {
      data: deepClone(data),
      total: this.stations.length,
      page,
      pageSize,
      totalPages: Math.ceil(this.stations.length / pageSize)
    }
  }
  
  /**
   * 根据ID获取充电桩
   */
  getStationById(id: string): ChargingStation | null {
    const station = this.stations.find(s => s.id === id)
    return station ? deepClone(station) : null
  }
  
  /**
   * 创建充电桩
   */
  createStation(station: ChargingStation): ChargingStation {
    this.stations.push(station)
    this.saveToStorage()
    return deepClone(station)
  }
  
  /**
   * 更新充电桩
   */
  updateStation(id: string, data: Partial<ChargingStation>): ChargingStation | null {
    const index = this.stations.findIndex(s => s.id === id)
    if (index === -1) return null
    
    this.stations[index] = { ...this.stations[index], ...data }
    this.saveToStorage()
    return deepClone(this.stations[index])
  }
  
  /**
   * 删除充电桩
   */
  deleteStation(id: string): boolean {
    const index = this.stations.findIndex(s => s.id === id)
    if (index === -1) return false
    
    this.stations.splice(index, 1)
    this.saveToStorage()
    return true
  }
  
  // ==================== 充电记录相关 ====================
  
  /**
   * 获取所有充电记录
   */
  getRecords(): ChargingRecord[] {
    return deepClone(this.records)
  }
  
  /**
   * 分页获取充电记录
   */
  getRecordsPaginated(params: PaginationParams): PaginationResponse<ChargingRecord> {
    const { page, pageSize } = params
    const start = (page - 1) * pageSize
    const end = start + pageSize
    const data = this.records.slice(start, end)
    
    return {
      data: deepClone(data),
      total: this.records.length,
      page,
      pageSize,
      totalPages: Math.ceil(this.records.length / pageSize)
    }
  }
  
  /**
   * 根据ID获取充电记录
   */
  getRecordById(id: string): ChargingRecord | null {
    const record = this.records.find(r => r.id === id)
    return record ? deepClone(record) : null
  }
  
  /**
   * 根据用户ID获取充电记录
   */
  getRecordsByUserId(userId: string): ChargingRecord[] {
    return deepClone(this.records.filter(r => r.userId === userId))
  }
  
  /**
   * 根据充电桩ID获取充电记录
   */
  getRecordsByStationId(stationId: string): ChargingRecord[] {
    return deepClone(this.records.filter(r => r.stationId === stationId))
  }
  
  /**
   * 创建充电记录
   */
  createRecord(record: ChargingRecord): ChargingRecord {
    this.records.unshift(record)
    this.saveToStorage()
    return deepClone(record)
  }
  
  /**
   * 更新充电记录
   */
  updateRecord(id: string, data: Partial<ChargingRecord>): ChargingRecord | null {
    const index = this.records.findIndex(r => r.id === id)
    if (index === -1) return null
    
    this.records[index] = { ...this.records[index], ...data }
    this.saveToStorage()
    return deepClone(this.records[index])
  }
  
  /**
   * 删除充电记录
   */
  deleteRecord(id: string): boolean {
    const index = this.records.findIndex(r => r.id === id)
    if (index === -1) return false
    
    this.records.splice(index, 1)
    this.saveToStorage()
    return true
  }
  
  // ==================== 用户相关 ====================
  
  /**
   * 获取所有用户
   */
  getUsers(): User[] {
    return deepClone(this.users)
  }
  
  /**
   * 分页获取用户
   */
  getUsersPaginated(params: PaginationParams): PaginationResponse<User> {
    const { page, pageSize } = params
    const start = (page - 1) * pageSize
    const end = start + pageSize
    const data = this.users.slice(start, end)
    
    return {
      data: deepClone(data),
      total: this.users.length,
      page,
      pageSize,
      totalPages: Math.ceil(this.users.length / pageSize)
    }
  }
  
  /**
   * 根据ID获取用户
   */
  getUserById(id: string): User | null {
    const user = this.users.find(u => u.id === id)
    return user ? deepClone(user) : null
  }
  
  /**
   * 根据手机号获取用户
   */
  getUserByPhone(phone: string): User | null {
    const user = this.users.find(u => u.phone === phone)
    return user ? deepClone(user) : null
  }
  
  /**
   * 创建用户
   */
  createUser(user: User): User {
    this.users.push(user)
    this.saveToStorage()
    return deepClone(user)
  }
  
  /**
   * 更新用户
   */
  updateUser(id: string, data: Partial<User>): User | null {
    const index = this.users.findIndex(u => u.id === id)
    if (index === -1) return null
    
    this.users[index] = { ...this.users[index], ...data }
    this.saveToStorage()
    return deepClone(this.users[index])
  }
  
  /**
   * 删除用户
   */
  deleteUser(id: string): boolean {
    const index = this.users.findIndex(u => u.id === id)
    if (index === -1) return false
    
    this.users.splice(index, 1)
    this.saveToStorage()
    return true
  }
  
  // ==================== 费率相关 ====================
  
  /**
   * 获取所有费率配置
   */
  getTariffs(): TariffConfig[] {
    return deepClone(this.tariffs)
  }
  
  /**
   * 获取默认费率配置
   */
  getDefaultTariff(): TariffConfig | null {
    const tariff = this.tariffs.find(t => t.isDefault)
    return tariff ? deepClone(tariff) : null
  }
  
  /**
   * 根据ID获取费率配置
   */
  getTariffById(id: string): TariffConfig | null {
    const tariff = this.tariffs.find(t => t.id === id)
    return tariff ? deepClone(tariff) : null
  }
  
  /**
   * 创建费率配置
   */
  createTariff(tariff: TariffConfig): TariffConfig {
    if (tariff.isDefault) {
      // 取消其他默认费率
      this.tariffs.forEach(t => t.isDefault = false)
    }
    this.tariffs.push(tariff)
    this.saveToStorage()
    return deepClone(tariff)
  }
  
  /**
   * 更新费率配置
   */
  updateTariff(id: string, data: Partial<TariffConfig>): TariffConfig | null {
    const index = this.tariffs.findIndex(t => t.id === id)
    if (index === -1) return null
    
    if (data.isDefault) {
      // 取消其他默认费率
      this.tariffs.forEach(t => t.isDefault = false)
    }
    
    this.tariffs[index] = { ...this.tariffs[index], ...data }
    this.saveToStorage()
    return deepClone(this.tariffs[index])
  }
  
  /**
   * 删除费率配置
   */
  deleteTariff(id: string): boolean {
    const index = this.tariffs.findIndex(t => t.id === id)
    if (index === -1) return false
    
    this.tariffs.splice(index, 1)
    this.saveToStorage()
    return true
  }
  
  // ==================== 日志相关 ====================
  
  /**
   * 获取所有日志
   */
  getLogs(): SystemLog[] {
    return deepClone(this.logs)
  }
  
  /**
   * 分页获取日志
   */
  getLogsPaginated(params: PaginationParams): PaginationResponse<SystemLog> {
    const { page, pageSize } = params
    const start = (page - 1) * pageSize
    const end = start + pageSize
    const data = this.logs.slice(start, end)
    
    return {
      data: deepClone(data),
      total: this.logs.length,
      page,
      pageSize,
      totalPages: Math.ceil(this.logs.length / pageSize)
    }
  }
  
  /**
   * 创建日志
   */
  createLog(log: SystemLog): SystemLog {
    this.logs.unshift(log)
    // 限制日志数量
    if (this.logs.length > 1000) {
      this.logs = this.logs.slice(0, 1000)
    }
    this.saveToStorage()
    return deepClone(log)
  }
  
  /**
   * 清空日志
   */
  clearLogs(): void {
    this.logs = []
    this.saveToStorage()
  }
}

// 导出单例
export const mockDB = new MockDatabase()

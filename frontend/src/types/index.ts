/**
 * 充电桩状态枚举
 */
export enum ChargingStationStatus {
  ONLINE = 'online',
  OFFLINE = 'offline',
  CHARGING = 'charging',
  MAINTENANCE = 'maintenance',
  ERROR = 'error'
}

/**
 * 充电桩类型
 */
export enum ChargingStationType {
  AC = 'ac', // 交流慢充
  DC = 'dc', // 直流快充
  SUPER = 'super' // 超级快充
}

/**
 * 充电记录状态
 */
export enum ChargingRecordStatus {
  CHARGING = 'charging',
  COMPLETED = 'completed',
  CANCELLED = 'cancelled',
  FAILED = 'failed'
}

/**
 * 用户角色
 */
export enum UserRole {
  ADMIN = 'admin',
  OPERATOR = 'operator',
  USER = 'user'
}

/**
 * 充电桩接口
 */
export interface ChargingStation {
  id: string
  name: string
  code: string
  type: ChargingStationType
  status: ChargingStationStatus
  power: number // 功率 kW
  location: string
  latitude?: number
  longitude?: number
  voltage: number // 电压 V
  current: number // 电流 A
  temperature: number // 温度 °C
  batteryLevel?: number // 电池电量 %
  chargedEnergy?: number // 本次充电量 kWh
  chargingDuration?: number // 本次充电时长 分钟
  usageCount: number // 使用次数
  totalEnergy: number // 总充电量 kWh
  totalRevenue: number // 总收入 元
  lastMaintenanceDate: string
  installDate: string
  manufacturer?: string
  model?: string
  serialNumber?: string
  createdAt: string
  updatedAt: string
}

/**
 * 充电记录接口
 */
export interface ChargingRecord {
  id: string
  stationId: string
  stationName: string
  userId: string
  userName: string
  userPhone: string
  startTime: string
  endTime?: string
  duration: number // 时长 分钟
  energy: number // 充电量 kWh
  startSOC: number // 开始电量 %
  endSOC?: number // 结束电量 %
  peakEnergy: number // 峰时电量 kWh
  valleyEnergy: number // 谷时电量 kWh
  normalEnergy: number // 平时电量 kWh
  electricityFee: number // 电费 元
  serviceFee: number // 服务费 元
  totalFee: number // 总费用 元
  status: ChargingRecordStatus
  paymentStatus: 'unpaid' | 'paid' | 'refunded'
  paymentMethod?: 'wechat' | 'alipay' | 'card'
  paymentTime?: string
  createdAt: string
  updatedAt: string
}

/**
 * 用户接口
 */
export interface User {
  id: string
  username: string
  phone: string
  email?: string
  avatar?: string
  realName?: string
  idCard?: string
  role: UserRole
  balance: number // 余额 元
  totalChargingCount: number
  totalChargingEnergy: number
  totalChargingFee: number
  vehiclePlate?: string // 车牌号
  vehicleModel?: string // 车型
  vehicleBrand?: string // 品牌
  isVIP: boolean
  vipExpireDate?: string
  status: 'active' | 'inactive' | 'blocked'
  lastLoginTime?: string
  createdAt: string
  updatedAt: string
}

/**
 * 费率配置接口
 */
export interface TariffConfig {
  id: string
  name: string
  peakPrice: number // 峰时电价 元/kWh
  valleyPrice: number // 谷时电价 元/kWh
  normalPrice: number // 平时电价 元/kWh
  serviceFee: number // 服务费 元/kWh
  peakHours: string[] // 峰时时段 ['08:00-12:00', '18:00-22:00']
  valleyHours: string[] // 谷时时段 ['00:00-06:00']
  isDefault: boolean
  effectiveDate: string
  expiryDate?: string
  description?: string
  createdAt: string
  updatedAt: string
}

/**
 * 统计数据接口
 */
export interface Statistics {
  date: string
  totalChargingCount: number
  totalEnergy: number
  totalRevenue: number
  averageChargingTime: number
  peakPower: number
  stationUsageRate: number
}

/**
 * 系统日志接口
 */
export interface SystemLog {
  id: string
  type: 'info' | 'warning' | 'error' | 'success'
  module: string
  action: string
  message: string
  userId?: string
  userName?: string
  details?: any
  ipAddress?: string
  userAgent?: string
  createdAt: string
}

/**
 * 分页请求参数
 */
export interface PaginationParams {
  page: number
  pageSize: number
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}

/**
 * 分页响应数据
 */
export interface PaginationResponse<T> {
  data: T[]
  total: number
  page: number
  pageSize: number
  totalPages: number
}

/**
 * API响应接口
 */
export interface ApiResponse<T = any> {
  success: boolean
  data?: T
  message?: string
  code?: number
  timestamp?: number
}

/**
 * 仪表板统计数据
 */
export interface DashboardStats {
  totalStations: number
  onlineStations: number
  chargingStations: number
  offlineStations: number
  todayEnergy: number
  todayRevenue: number
  todayChargingCount: number
  averageChargingTime: number
  recentRecords: ChargingRecord[]
  stationStatusDistribution: {
    status: ChargingStationStatus
    count: number
    percentage: number
  }[]
  energyTrend: {
    date: string
    energy: number
    revenue: number
  }[]
}

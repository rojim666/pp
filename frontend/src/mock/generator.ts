import type {
  ChargingStation,
  ChargingRecord,
  User,
  TariffConfig,
  SystemLog
} from '../types'
import {
  ChargingStationStatus,
  ChargingStationType,
  ChargingRecordStatus,
  UserRole
} from '../types'
import { generateId, formatDateTime, random, randomFloat, randomItem } from '../utils'

/**
 * 生成模拟充电桩数据
 */
export function generateMockStations(count: number = 50): ChargingStation[] {
  const stations: ChargingStation[] = []
  const locations = [
    '北京市朝阳区', '上海市浦东新区', '广州市天河区', '深圳市南山区',
    '杭州市西湖区', '成都市武侯区', '重庆市渝北区', '武汉市洪山区'
  ]
  const manufacturers = ['特来电', '星星充电', '国家电网', '南方电网', '小鹏汽车']
  
  for (let i = 0; i < count; i++) {
    const type = randomItem([
      ChargingStationType.AC,
      ChargingStationType.DC,
      ChargingStationType.SUPER
    ])
    
    let power = 7
    let voltage = 220
    let current = 32
    
    if (type === ChargingStationType.DC) {
      power = randomItem([60, 120, 180])
      voltage = randomItem([380, 500, 750])
      current = power * 1000 / voltage
    } else if (type === ChargingStationType.SUPER) {
      power = randomItem([250, 350, 480])
      voltage = randomItem([500, 800, 1000])
      current = power * 1000 / voltage
    }
    
    const status = randomItem([
      ChargingStationStatus.ONLINE,
      ChargingStationStatus.ONLINE,
      ChargingStationStatus.ONLINE,
      ChargingStationStatus.CHARGING,
      ChargingStationStatus.CHARGING,
      ChargingStationStatus.OFFLINE,
      ChargingStationStatus.MAINTENANCE
    ])
    
    const installDate = new Date(2020 + random(0, 4), random(0, 11), random(1, 28))
    const lastMaintenanceDate = new Date(2024, random(0, 9), random(1, 28))
    
    stations.push({
      id: generateId('station_'),
      name: `充电桩 #${String(i + 1).padStart(3, '0')}`,
      code: `CS${String(i + 1).padStart(5, '0')}`,
      type,
      status,
      power,
      location: randomItem(locations),
      latitude: 39.9 + randomFloat(-0.5, 0.5),
      longitude: 116.4 + randomFloat(-0.5, 0.5),
      voltage,
      current: Math.round(current),
      temperature: status === ChargingStationStatus.CHARGING 
        ? randomFloat(35, 45, 1) 
        : randomFloat(20, 30, 1),
      usageCount: random(0, 1000),
      totalEnergy: randomFloat(0, 50000, 2),
      totalRevenue: randomFloat(0, 100000, 2),
      lastMaintenanceDate: formatDateTime(lastMaintenanceDate, 'YYYY-MM-DD'),
      installDate: formatDateTime(installDate, 'YYYY-MM-DD'),
      manufacturer: randomItem(manufacturers),
      model: `Model-${type.toUpperCase()}-${random(100, 999)}`,
      serialNumber: `SN${Date.now()}${random(1000, 9999)}`,
      createdAt: formatDateTime(installDate),
      updatedAt: formatDateTime(new Date())
    })
  }
  
  return stations
}

/**
 * 生成模拟充电记录
 */
export function generateMockRecords(
  count: number = 100,
  stations: ChargingStation[],
  users: User[]
): ChargingRecord[] {
  const records: ChargingRecord[] = []
  
  for (let i = 0; i < count; i++) {
    const station = randomItem(stations)
    const user = randomItem(users)
    
    const startTime = new Date(
      2024,
      9,
      random(1, 19),
      random(0, 23),
      random(0, 59)
    )
    
    const duration = random(15, 240) // 15分钟到4小时
    const endTime = new Date(startTime.getTime() + duration * 60 * 1000)
    
    const energy = randomFloat(5, 80, 2)
    const peakEnergy = randomFloat(0, energy * 0.4, 2)
    const valleyEnergy = randomFloat(0, energy * 0.3, 2)
    const normalEnergy = energy - peakEnergy - valleyEnergy
    
    const electricityFee = 
      peakEnergy * 1.2 + 
      valleyEnergy * 0.6 + 
      normalEnergy * 0.8
    
    const serviceFee = energy * 0.8
    const totalFee = electricityFee + serviceFee
    
    const status = i < count - 3 
      ? ChargingRecordStatus.COMPLETED 
      : randomItem([ChargingRecordStatus.CHARGING, ChargingRecordStatus.COMPLETED])
    
    records.push({
      id: generateId('record_'),
      stationId: station.id,
      stationName: station.name,
      userId: user.id,
      userName: user.username,
      userPhone: user.phone,
      startTime: formatDateTime(startTime),
      endTime: status === ChargingRecordStatus.COMPLETED 
        ? formatDateTime(endTime) 
        : undefined,
      duration,
      energy,
      startSOC: random(10, 40),
      endSOC: status === ChargingRecordStatus.COMPLETED 
        ? random(80, 100) 
        : undefined,
      peakEnergy,
      valleyEnergy,
      normalEnergy,
      electricityFee: parseFloat(electricityFee.toFixed(2)),
      serviceFee: parseFloat(serviceFee.toFixed(2)),
      totalFee: parseFloat(totalFee.toFixed(2)),
      status,
      paymentStatus: status === ChargingRecordStatus.COMPLETED ? 'paid' : 'unpaid',
      paymentMethod: status === ChargingRecordStatus.COMPLETED 
        ? randomItem(['wechat', 'alipay', 'card'] as const)
        : undefined,
      paymentTime: status === ChargingRecordStatus.COMPLETED 
        ? formatDateTime(endTime) 
        : undefined,
      createdAt: formatDateTime(startTime),
      updatedAt: formatDateTime(new Date())
    })
  }
  
  return records.sort((a, b) => 
    new Date(b.startTime).getTime() - new Date(a.startTime).getTime()
  )
}

/**
 * 生成模拟用户数据
 */
export function generateMockUsers(count: number = 50): User[] {
  const users: User[] = []
  const surnames = ['王', '李', '张', '刘', '陈', '杨', '黄', '赵', '周', '吴']
  const names = ['伟', '芳', '娜', '秀英', '敏', '静', '丽', '强', '磊', '军']
  const brands = ['特斯拉', '比亚迪', '蔚来', '小鹏', '理想', '极氪', '问界']
  
  // 创建固定的测试账号
  const testAccounts = [
    {
      id: 'user_admin',
      username: 'admin',
      phone: '13800138000',
      email: 'admin@example.com',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin',
      realName: '系统管理员',
      role: UserRole.ADMIN,
      balance: 10000,
      totalChargingCount: 50,
      totalChargingEnergy: 1250.50,
      totalChargingFee: 2500.00,
      vehiclePlate: '京A88888',
      vehicleModel: 'Model S',
      vehicleBrand: '特斯拉',
      isVIP: true,
      vipExpireDate: '2025-12-31',
      status: 'active' as const,
      lastLoginTime: formatDateTime(new Date()),
      createdAt: '2024-01-01 00:00:00',
      updatedAt: formatDateTime(new Date())
    },
    {
      id: 'user_operator',
      username: 'operator',
      phone: '13900139000',
      email: 'operator@example.com',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=operator',
      realName: '运营人员',
      role: UserRole.OPERATOR,
      balance: 5000,
      totalChargingCount: 30,
      totalChargingEnergy: 750.30,
      totalChargingFee: 1500.00,
      vehiclePlate: '京B66666',
      vehicleModel: 'Model 3',
      vehicleBrand: '特斯拉',
      isVIP: true,
      vipExpireDate: '2025-06-30',
      status: 'active' as const,
      lastLoginTime: formatDateTime(new Date()),
      createdAt: '2024-01-01 00:00:00',
      updatedAt: formatDateTime(new Date())
    },
    {
      id: 'user_test',
      username: 'user',
      phone: '13700137000',
      email: 'user@example.com',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=user',
      realName: '测试用户',
      role: UserRole.USER,
      balance: 1000,
      totalChargingCount: 10,
      totalChargingEnergy: 250.10,
      totalChargingFee: 500.00,
      vehiclePlate: '京C12345',
      vehicleModel: 'Model Y',
      vehicleBrand: '特斯拉',
      isVIP: false,
      vipExpireDate: undefined,
      status: 'active' as const,
      lastLoginTime: formatDateTime(new Date()),
      createdAt: '2024-01-01 00:00:00',
      updatedAt: formatDateTime(new Date())
    }
  ]
  
  users.push(...testAccounts)
  
  // 生成其他随机用户
  for (let i = 3; i < count; i++) {
    const createdAt = new Date(2022 + random(0, 2), random(0, 11), random(1, 28))
    const isVIP = random(0, 100) < 20
    
    users.push({
      id: generateId('user_'),
      username: `${randomItem(surnames)}${randomItem(names)}${random(100, 999)}`,
      phone: `1${random(3, 9)}${random(0, 9)}${String(random(10000000, 99999999))}`,
      email: `user${i + 1}@example.com`,
      avatar: `https://api.dicebear.com/7.x/avataaars/svg?seed=${i}`,
      realName: `${randomItem(surnames)}${randomItem(names)}`,
      role: UserRole.USER,
      balance: randomFloat(0, 5000, 2),
      totalChargingCount: random(0, 200),
      totalChargingEnergy: randomFloat(0, 5000, 2),
      totalChargingFee: randomFloat(0, 10000, 2),
      vehiclePlate: `京A${String(random(10000, 99999))}`,
      vehicleModel: `Model ${randomItem(['3', 'Y', 'S', 'X', 'E'])}`,
      vehicleBrand: randomItem(brands),
      isVIP,
      vipExpireDate: isVIP 
        ? formatDateTime(new Date(2025, random(0, 11), random(1, 28)), 'YYYY-MM-DD')
        : undefined,
      status: 'active',
      lastLoginTime: formatDateTime(new Date(2024, 9, random(1, 19), random(0, 23), random(0, 59))),
      createdAt: formatDateTime(createdAt),
      updatedAt: formatDateTime(new Date())
    })
  }
  
  return users
}

/**
 * 生成模拟费率配置
 */
export function generateMockTariffs(): TariffConfig[] {
  return [
    {
      id: generateId('tariff_'),
      name: '标准费率',
      peakPrice: 1.2,
      valleyPrice: 0.6,
      normalPrice: 0.8,
      serviceFee: 0.8,
      peakHours: ['08:00-12:00', '18:00-22:00'],
      valleyHours: ['00:00-06:00'],
      isDefault: true,
      effectiveDate: '2024-01-01',
      description: '工作日标准费率',
      createdAt: formatDateTime(new Date('2024-01-01')),
      updatedAt: formatDateTime(new Date())
    },
    {
      id: generateId('tariff_'),
      name: '周末费率',
      peakPrice: 1.0,
      valleyPrice: 0.5,
      normalPrice: 0.7,
      serviceFee: 0.6,
      peakHours: ['10:00-14:00', '17:00-21:00'],
      valleyHours: ['00:00-07:00'],
      isDefault: false,
      effectiveDate: '2024-01-01',
      description: '周末优惠费率',
      createdAt: formatDateTime(new Date('2024-01-01')),
      updatedAt: formatDateTime(new Date())
    }
  ]
}

/**
 * 生成模拟系统日志
 */
export function generateMockLogs(count: number = 50): SystemLog[] {
  const logs: SystemLog[] = []
  const modules = ['充电桩', '用户', '订单', '系统', '支付']
  const actions = ['创建', '更新', '删除', '查询', '登录', '登出', '支付']
  const types: Array<'info' | 'warning' | 'error' | 'success'> = ['info', 'warning', 'error', 'success']
  
  for (let i = 0; i < count; i++) {
    const createdAt = new Date(2024, 9, random(1, 19), random(0, 23), random(0, 59))
    
    logs.push({
      id: generateId('log_'),
      type: randomItem(types),
      module: randomItem(modules),
      action: randomItem(actions),
      message: `${randomItem(modules)}${randomItem(actions)}操作成功`,
      userId: `user_${random(1, 50)}`,
      userName: `用户${random(1, 50)}`,
      ipAddress: `192.168.${random(0, 255)}.${random(0, 255)}`,
      userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)',
      createdAt: formatDateTime(createdAt)
    })
  }
  
  return logs.sort((a, b) => 
    new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
  )
}

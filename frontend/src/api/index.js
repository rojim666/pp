import api from './request'

/**
 * 认证API
 */
export const authAPI = {
  // 登录
  login: (data) => api.post('/auth/login', data),
  
  // 注册
  register: (data) => api.post('/auth/register', data)
}

/**
 * 用户API
 */
export const userAPI = {
  // 获取用户列表
  list: (params) => api.get('/users', { params }),
  
  // 获取用户详情
  get: (id) => api.get(`/users/${id}`),
  
  // 更新用户
  update: (id, data) => api.put(`/users/${id}`, data),
  
  // 充值
  recharge: (id, amount) => api.post(`/users/${id}/recharge`, null, { params: { amount } }),
  
  // 删除用户
  delete: (id) => api.delete(`/users/${id}`)
}

/**
 * 充电桩API
 */
export const stationAPI = {
  // 获取充电桩列表
  list: (params) => api.get('/stations', { params }),
  
  // 获取充电桩详情
  get: (id) => api.get(`/stations/${id}`),
  
  // 创建充电桩
  create: (data) => api.post('/stations', data),
  
  // 更新充电桩
  update: (id, data) => api.put(`/stations/${id}`, data),
  
  // 删除充电桩
  delete: (id) => api.delete(`/stations/${id}`),
  
  // 更新充电桩状态
  updateStatus: (id, status) => api.patch(`/stations/${id}/status`, null, {
    params: { status }
  })
}

/**
 * 充电记录API
 */
export const recordAPI = {
  // 获取充电记录列表
  list: (params) => api.get('/records', { params }),
  
  // 获取充电记录详情
  get: (id) => api.get(`/records/${id}`),
  
  // 开始充电
  start: (data) => api.post('/records/start', data),
  
  // 结束充电
  end: (id, params) => api.post(`/records/${id}/end`, null, { params })
}

/**
 * 仪表盘API
 */
export const dashboardAPI = {
  // 获取统计数据
  getStats: () => api.get('/dashboard/stats')
}

/**
 * 费率配置API
 */
export const tariffAPI = {
  // 获取当前配置
  getCurrent: () => api.get('/tariff/current'),
  
  // 更新配置
  update: (data) => api.put('/tariff/update', data)
}


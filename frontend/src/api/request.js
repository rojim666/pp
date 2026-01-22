import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果是文件下载，直接返回
    if (response.config.responseType === 'blob') {
      return response
    }
    
    // 正常响应
    if (res.code === 200) {
      return res.data
    }
    
    // 业务错误
    console.error('API Error:', res.message)
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  error => {
    console.error('Request Error:', error)
    
    if (error.response) {
      const { status, data } = error.response
      
      // 401 未授权
      if (status === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        window.location.href = '/login'
        return Promise.reject(new Error('请先登录'))
      }
      
      // 403 无权限
      if (status === 403) {
        return Promise.reject(new Error('无权限访问'))
      }
      
      // 其他错误
      const message = data?.message || `请求失败(${status})`
      return Promise.reject(new Error(message))
    }
    
    // 网络错误
    if (error.code === 'ECONNABORTED') {
      return Promise.reject(new Error('请求超时'))
    }
    
    return Promise.reject(new Error('网络错误，请检查网络连接'))
  }
)

export default api

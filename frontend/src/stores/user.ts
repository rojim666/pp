import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User } from '../types'
import { userAPI, authAPI } from '../api'

export const useUserStore = defineStore('user', () => {
  // 状态
  const users = ref<User[]>([])
  const currentUser = ref<User | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)
  
  // 计算属性
  const totalCount = computed(() => users.value.length)
  
  const vipCount = computed(() => 
    users.value.filter(u => u.isVIP).length
  )
  
  const activeCount = computed(() => 
    users.value.filter(u => u.status === 'active').length
  )
  
  const isLoggedIn = computed(() => currentUser.value !== null)
  
  const isAdmin = computed(() => 
    currentUser.value?.role === 'admin'
  )
  
  // Actions
  async function fetchUsers() {
    loading.value = true
    error.value = null
    try {
      const res = await userAPI.list()
      users.value = res.records || []
    } catch (e: any) {
      error.value = e.message
      console.error('Failed to fetch users:', e)
    } finally {
      loading.value = false
    }
  }
  
  async function getUserById(id: string): Promise<User | null> {
    try {
      const res = await userAPI.get(id)
      return res
    } catch (e: any) {
      error.value = e.message
      return null
    }
  }
  
  async function login(credentials: { username: string; password: string }): Promise<boolean> {
    loading.value = true
    error.value = null
    try {
      // 调用真正的后端API
      const response = await authAPI.login(credentials) as any
      
      // 根据后端响应格式处理
      // 通常后端返回: { token: '...', user: {...}, code: 200, message: 'success' }
      const token = response?.token
      const user = response?.user || response
      
      if (!token) {
        throw new Error('登录失败：未收到 token')
      }
      
      // 保存登录状态
      currentUser.value = user
      localStorage.setItem('currentUser', JSON.stringify(user))
      localStorage.setItem('token', token)
      
      return true
    } catch (e: any) {
      error.value = e.message || '登录失败，请检查后端是否启动'
      console.error('Login error:', e)
      throw e
    } finally {
      loading.value = false
    }
  }
  
  async function logout() {
    currentUser.value = null
    localStorage.removeItem('currentUser')
    localStorage.removeItem('token')
  }
  
  // 初始化时检查本地存储的登录状态
  function initAuth() {
    const savedUser = localStorage.getItem('currentUser')
    if (savedUser) {
      try {
        currentUser.value = JSON.parse(savedUser)
      } catch (e) {
        console.error('Failed to parse saved user:', e)
        localStorage.removeItem('currentUser')
      }
    }
  }
  
  async function createUser(user: User): Promise<User | null> {
    loading.value = true
    try {
      const res = await authAPI.register(user)
      const created = res
      users.value.push(created)
      return created
    } catch (e: any) {
      error.value = e.message
      return null
    } finally {
      loading.value = false
    }
  }
  
  async function updateUser(id: string, data: Partial<User>): Promise<boolean> {
    loading.value = true
    try {
      const res = await userAPI.update(id, data)
      const updated = res
      if (updated) {
        const index = users.value.findIndex(u => u.id === id)
        if (index !== -1) {
          users.value[index] = updated
        }
        if (currentUser.value?.id === id) {
          currentUser.value = updated
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
  
  async function deleteUser(id: string): Promise<boolean> {
    loading.value = true
    try {
      await userAPI.delete(id)
      const index = users.value.findIndex(u => u.id === id)
      if (index !== -1) {
        users.value.splice(index, 1)
      }
      return true
    } catch (e: any) {
      error.value = e.message
      return false
    } finally {
      loading.value = false
    }
  }

  function setUser(user: User) {
    currentUser.value = user
    localStorage.setItem('currentUser', JSON.stringify(user))
  }

  async function fetchCurrentUser() {
    if (!currentUser.value?.id) return
    try {
      const res = await userAPI.get(currentUser.value.id)
      setUser(res)
    } catch (e) {
      console.error('Failed to fetch current user:', e)
    }
  }
  
  // 初始化
  initAuth()
  
  return {
    // State
    users,
    currentUser,
    loading,
    error,
    // Getters
    totalCount,
    vipCount,
    activeCount,
    isLoggedIn,
    isAdmin,
    // Actions
    fetchUsers,
    getUserById,
    login,
    logout,
    createUser,
    updateUser,
    deleteUser,
    setUser,
    fetchCurrentUser,
    initAuth
  }
})

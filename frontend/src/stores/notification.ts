import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { recordAPI } from '../api'

export const useNotificationStore = defineStore('notification', () => {
  const notifications = ref<any[]>([])
  const loading = ref(false)

  const unreadCount = computed(() => notifications.value.filter(n => !n.read).length)
  const readCount = computed(() => notifications.value.filter(n => n.read).length)
  const todayCount = computed(() => notifications.value.filter(n => {
    const time = n.time || ''
    return time.includes('分钟前') || time.includes('小时前') || time.includes(new Date().toLocaleDateString())
  }).length)

  async function fetchNotifications() {
    loading.value = true
    try {
      const res = await recordAPI.list({ page: 1, size: 50 })
      const records = res.records || []
      if (records.length > 0) {
        notifications.value = records.map((r: any) => {
          let type = 'info'
          let title = '充电通知'
          let message = ''
          let tab = 'business'
          
          if (r.status === 'charging') {
            type = 'info'
            title = '开始充电'
            message = `用户 ${r.userName || '未知用户'} 在 ${r.stationName || '未知站点'} 开始充电`
          } else if (r.status === 'completed') {
            type = 'success'
            title = '充电结束'
            message = `用户 ${r.userName || '未知用户'} 在 ${r.stationName || '未知站点'} 完成充电，消费 ${r.totalFee} 元`
          } else if (r.status === 'failed') {
            type = 'warning'
            title = '充电异常'
            message = `用户 ${r.userName || '未知用户'} 在 ${r.stationName || '未知站点'} 充电失败`
            tab = 'device'
          }

          return {
            id: r.id,
            type,
            title,
            message,
            time: r.updatedAt ? new Date(r.updatedAt).toLocaleString() : '刚刚',
            read: false,
            category: tab === 'device' ? '设备' : '业务',
            tab
          }
        })
      } else {
        notifications.value = []
      }
    } catch (error) {
      console.error('Failed to fetch notifications:', error)
    } finally {
      loading.value = false
    }
  }

  function markAsRead(id: number) {
    const notification = notifications.value.find(n => n.id === id)
    if (notification) {
      notification.read = true
    }
  }

  function markAllRead() {
    notifications.value.forEach(n => n.read = true)
  }

  function deleteNotification(id: number) {
    const index = notifications.value.findIndex(n => n.id === id)
    if (index > -1) {
      notifications.value.splice(index, 1)
    }
  }

  return {
    notifications,
    loading,
    unreadCount,
    readCount,
    todayCount,
    fetchNotifications,
    markAsRead,
    markAllRead,
    deleteNotification
  }
})

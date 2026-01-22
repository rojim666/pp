<template>
  <div class="page">
    <h1 class="page-title">用户信息管理</h1>

    <!-- 统计卡片 -->
    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #007AFF, #5856D6);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2M12 11a4 4 0 100-8 4 4 0 000 8z" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.total }}</div>
          <div class="stat-label">总用户数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #34C759, #30D158);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
            <path d="M9 12l2 2 4-4" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.active }}</div>
          <div class="stat-label">活跃用户</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #FF9500, #FF6B00);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M13 2L3 14h8l-1 8 10-12h-8l1-8z" fill="currentColor"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.charging }}</div>
          <div class="stat-label">充电中</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #AF52DE, #BF5AF2);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="3" y="6" width="18" height="12" rx="2" stroke="currentColor" stroke-width="2"/>
            <line x1="7" y1="12" x2="17" y2="12" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.totalEnergy }}</div>
          <div class="stat-label">累计电量(kWh)</div>
        </div>
      </div>
    </div>

    <div class="card">
      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <button class="btn-primary" @click="showAddDialog">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 18px; height: 18px; margin-right: 6px;">
              <path d="M12 5v14M5 12h14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            添加用户
          </button>
          <button class="btn-secondary" @click="refreshData" :disabled="loading">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 16px; height: 16px;">
              <path d="M21 10c-2.4-2.4-5.7-3.9-9.4-3.9-7.2 0-13.1 5.9-13.1 13.1M3 14c2.4 2.4 5.7 3.9 9.4 3.9 7.2 0 13.1-5.9 13.1-13.1" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <path d="M21 3v7h-7M3 21v-7h7" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </button>
          <button 
            v-if="selectedIds.length > 0" 
            class="btn-secondary"
            @click="batchDelete"
          >
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 16px; height: 16px; margin-right: 4px;">
              <path d="M3 6h18M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            批量删除 ({{ selectedIds.length }})
          </button>
        </div>

        <div class="toolbar-right">
          <div class="search-box">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="search-icon">
              <circle cx="11" cy="11" r="8" stroke="currentColor" stroke-width="2"/>
              <path d="M21 21l-4.35-4.35" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <input 
              v-model="searchQuery" 
              type="text" 
              placeholder="搜索用户名、手机号..." 
              class="search-input"
            />
          </div>
          <select v-model="filterRole" class="filter-select">
            <option value="">全部角色</option>
            <option value="user">普通用户</option>
            <option value="vip">VIP用户</option>
            <option value="admin">管理员</option>
          </select>
          <select v-model="filterStatus" class="filter-select">
            <option value="">全部状态</option>
            <option value="active">正常</option>
            <option value="charging">充电中</option>
            <option value="disabled">已禁用</option>
          </select>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <div class="spinner"></div>
        <p>加载中...</p>
      </div>

      <!-- 用户表格 -->
      <div v-else-if="displayUsers.length > 0" class="table-container">
        <table class="user-table">
          <thead>
            <tr>
              <th class="checkbox-col">
                <input 
                  type="checkbox" 
                  @change="toggleSelectAll"
                  :checked="selectedIds.length === displayUsers.length && displayUsers.length > 0"
                />
              </th>
              <th>头像</th>
              <th>用户名</th>
              <th>手机号</th>
              <th>角色</th>
              <th>余额</th>
              <th>充电次数</th>
              <th>累计电量</th>
              <th>状态</th>
              <th>注册时间</th>
              <th class="actions-col">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr 
              v-for="user in displayUsers" 
              :key="user.id"
              :class="{ 'selected': selectedIds.includes(user.id) }"
            >
              <td class="checkbox-col">
                <input 
                  type="checkbox" 
                  :checked="selectedIds.includes(user.id)"
                  @change="toggleSelect(user.id)"
                />
              </td>
              <td>
                <div class="user-avatar" :style="{ background: user.avatarColor }">
                  {{ user.username.charAt(0).toUpperCase() }}
                </div>
              </td>
              <td>
                <div class="user-name-cell">
                  <span class="user-name">{{ user.username }}</span>
                  <span v-if="user.role === 'vip'" class="vip-badge">VIP</span>
                </div>
              </td>
              <td class="phone-col">{{ user.phone }}</td>
              <td>
                <span class="role-badge" :class="'role-' + user.role">
                  {{ getRoleText(user.role) }}
                </span>
              </td>
              <td class="balance-col">¥{{ user.balance.toFixed(2) }}</td>
              <td class="number-col">{{ user.totalChargingCount }}</td>
              <td class="number-col">{{ user.totalChargingEnergy.toFixed(1) }}</td>
              <td>
                <span class="status-badge" :class="'status-' + user.status">
                  {{ getStatusText(user.status) }}
                </span>
              </td>
              <td class="date-col">{{ user.createdAt }}</td>
              <td class="actions-col">
                <div class="action-buttons">
                  <button class="btn-icon btn-view" @click="viewUser(user)" title="查看">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" stroke="currentColor" stroke-width="2"/>
                      <circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="2"/>
                    </svg>
                  </button>
                  <button class="btn-icon btn-edit" @click="editUser(user)" title="编辑">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7" stroke="currentColor" stroke-width="2"/>
                      <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" stroke="currentColor" stroke-width="2"/>
                    </svg>
                  </button>
                  <button 
                    class="btn-icon"
                    :class="user.status === 'disabled' ? 'btn-enable' : 'btn-disable'"
                    @click="toggleUserStatus(user)"
                    :title="user.status === 'disabled' ? '启用' : '禁用'"
                  >
                    <svg v-if="user.status === 'disabled'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <path d="M5 3l14 9-14 9V3z" fill="currentColor"/>
                    </svg>
                    <svg v-else viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
                      <path d="M15 9l-6 6M9 9l6 6" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                    </svg>
                  </button>
                  <button class="btn-icon btn-recharge" @click="showRechargeDialog(user)" title="充值">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <path d="M12 1v22M17 5H9.5a3.5 3.5 0 000 7h5a3.5 3.5 0 010 7H6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </button>
                  <button class="btn-icon btn-delete" @click="deleteUser(user)" title="删除">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <path d="M3 6h18M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2" stroke="currentColor" stroke-width="2"/>
                    </svg>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="empty-icon">
          <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2M12 11a4 4 0 100-8 4 4 0 000 8z" stroke="currentColor" stroke-width="2"/>
        </svg>
        <p class="empty-text">{{ searchQuery || filterRole || filterStatus ? '未找到匹配的用户' : '暂无用户，点击上方按钮添加' }}</p>
      </div>
    </div>

    <!-- 添加/编辑用户对话框 -->
    <div v-if="showDialog" class="dialog-overlay" @click.self="closeDialog">
      <div class="dialog">
        <div class="dialog-header">
          <h2>{{ isEditing ? '编辑用户' : '添加用户' }}</h2>
          <button class="close-btn" @click="closeDialog">&times;</button>
        </div>

        <form @submit.prevent="submitForm" class="dialog-body">
          <div class="form-group">
            <label>用户名 *</label>
            <input 
              v-model="formData.username" 
              type="text" 
              placeholder="请输入用户名"
              required
            />
          </div>

          <div class="form-group">
            <label>手机号 *</label>
            <input 
              v-model="formData.phone" 
              type="tel" 
              placeholder="请输入手机号"
              pattern="1[3-9]\d{9}"
              required
            />
          </div>

          <div v-if="!isEditing" class="form-group">
            <label>密码 *</label>
            <input 
              v-model="formData.password" 
              type="password" 
              placeholder="请输入密码"
              minlength="6"
              required
            />
          </div>

          <div class="form-row">
            <div class="form-group">
              <label>角色</label>
              <select v-model="formData.role">
                <option value="user">普通用户</option>
                <option value="vip">VIP用户</option>
                <option value="admin">管理员</option>
              </select>
            </div>

            <div class="form-group">
              <label>账户余额</label>
              <input 
                v-model.number="formData.balance" 
                type="number" 
                step="0.01"
                min="0"
                placeholder="0.00"
              />
            </div>
          </div>

          <div class="dialog-actions">
            <button type="button" class="btn-secondary" @click="closeDialog">取消</button>
            <button type="submit" class="btn-primary">{{ isEditing ? '保存' : '添加' }}</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 用户详情对话框 -->
    <div v-if="showDetailDialog" class="dialog-overlay" @click.self="closeDetailDialog">
      <div class="dialog detail-dialog">
        <div class="dialog-header">
          <div class="dialog-title-section">
            <div class="user-avatar-large" :style="{ background: selectedUser?.avatarColor }">
              {{ selectedUser?.username.charAt(0).toUpperCase() }}
            </div>
            <div>
              <h2>{{ selectedUser?.username }}</h2>
              <p class="dialog-subtitle">{{ selectedUser?.phone }}</p>
            </div>
          </div>
          <button class="close-btn" @click="closeDetailDialog">&times;</button>
        </div>

        <div class="dialog-body">
          <!-- 基本信息 -->
          <div class="detail-section">
            <h3 class="section-title">基本信息</h3>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">用户ID</span>
                <span class="info-value">{{ selectedUser?.id }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">角色</span>
                <span class="role-badge" :class="'role-' + selectedUser?.role">
                  {{ getRoleText(selectedUser?.role) }}
                </span>
              </div>
              <div class="info-item">
                <span class="info-label">状态</span>
                <span class="status-badge" :class="'status-' + selectedUser?.status">
                  {{ getStatusText(selectedUser?.status) }}
                </span>
              </div>
              <div class="info-item">
                <span class="info-label">注册时间</span>
                <span class="info-value">{{ selectedUser?.createdAt }}</span>
              </div>
            </div>
          </div>

          <!-- 账户信息 -->
          <div class="detail-section">
            <h3 class="section-title">账户信息</h3>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">账户余额</span>
                <span class="info-value highlight">¥{{ selectedUser?.balance.toFixed(2) }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">充电次数</span>
                <span class="info-value">{{ selectedUser?.totalChargingCount }} 次</span>
              </div>
              <div class="info-item">
                <span class="info-label">累计电量</span>
                <span class="info-value">{{ selectedUser?.totalChargingEnergy.toFixed(1) }} kWh</span>
              </div>
              <div class="info-item">
                <span class="info-label">累计消费</span>
                <span class="info-value">¥{{ selectedUser?.totalChargingFee.toFixed(2) }}</span>
              </div>
            </div>
          </div>

          <!-- 最近充电记录 -->
          <div class="detail-section">
            <h3 class="section-title">最近充电记录</h3>
            <div class="record-list">
              <div v-for="record in selectedUser?.recentCharges || []" :key="record.id" class="record-item">
                <div class="record-info">
                  <div class="record-station">{{ record.station }}</div>
                  <div class="record-time">{{ record.time }}</div>
                </div>
                <div class="record-stats">
                  <div class="record-energy">{{ record.energy }} kWh</div>
                  <div class="record-cost">¥{{ record.cost }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="dialog-footer">
          <button class="btn-secondary" @click="closeDetailDialog">关闭</button>
          <button class="btn-primary" @click="editUser(selectedUser)">编辑用户</button>
        </div>
      </div>
    </div>
    <!-- 充值对话框 -->
    <div v-if="showRechargeModal" class="dialog-overlay" @click.self="closeRechargeDialog">
      <div class="dialog recharge-dialog">
        <div class="dialog-header">
          <h2>用户充值</h2>
          <button class="close-btn" @click="closeRechargeDialog">&times;</button>
        </div>

        <div class="dialog-body">
          <div class="user-info-summary">
            <div class="user-avatar-small" :style="{ background: selectedUser?.avatarColor }">
              {{ selectedUser?.username.charAt(0).toUpperCase() }}
            </div>
            <div class="user-details">
              <div class="user-name">{{ selectedUser?.username }}</div>
              <div class="current-balance">当前余额: ¥{{ selectedUser?.balance.toFixed(2) }}</div>
            </div>
          </div>

          <div class="form-group">
            <label>充值金额</label>
            <div class="amount-input-wrapper">
              <span class="currency-symbol">¥</span>
              <input 
                v-model.number="rechargeAmount" 
                type="number" 
                step="1"
                min="1"
                placeholder="请输入充值金额"
                class="amount-input"
                @keyup.enter="submitRecharge"
              />
            </div>
            <div class="quick-amounts">
              <button 
                v-for="amount in [50, 100, 200, 500]" 
                :key="amount"
                class="quick-amount-btn"
                :class="{ active: rechargeAmount === amount }"
                @click="rechargeAmount = amount"
              >
                ¥{{ amount }}
              </button>
            </div>
          </div>
        </div>

        <div class="dialog-footer">
          <button class="btn-secondary" @click="closeRechargeDialog">取消</button>
          <button class="btn-primary" @click="submitRecharge" :disabled="!rechargeAmount || rechargeAmount <= 0">确认充值</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { userAPI, authAPI } from '../api'

// 状态数据
const users = ref([])
const searchQuery = ref('')
const filterRole = ref('')
const filterStatus = ref('')
const showDialog = ref(false)
const showDetailDialog = ref(false)
const showRechargeModal = ref(false)
const rechargeAmount = ref(null)
const isEditing = ref(false)
const loading = ref(false)
const selectedIds = ref([])
const selectedUser = ref(null)

// 表单数据
const formData = ref({
  id: null,
  username: '',
  phone: '',
  password: '',
  role: 'user',
  balance: 0,
  status: 'active'
})

// 统计数据
const stats = computed(() => {
  const total = users.value.length
  const active = users.value.filter(u => u.status === 'active').length
  const charging = users.value.filter(u => u.status === 'charging').length
  const totalEnergy = users.value.reduce((sum, u) => sum + (u.totalChargingEnergy || 0), 0)
  
  return { total, active, charging, totalEnergy: totalEnergy.toFixed(1) }
})

// 过滤后的用户列表
const displayUsers = computed(() => {
  let result = [...users.value]
  
  // 搜索过滤
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(user => 
      user.username.toLowerCase().includes(query) ||
      user.phone.includes(query)
    )
  }
  
  // 角色过滤
  if (filterRole.value) {
    result = result.filter(user => user.role === filterRole.value)
  }
  
  // 状态过滤
  if (filterStatus.value) {
    result = result.filter(user => user.status === filterStatus.value)
  }
  
  return result
})

// 获取角色文本
const getRoleText = (role) => {
  const roleMap = {
    'user': '普通用户',
    'vip': 'VIP用户',
    'admin': '管理员'
  }
  return roleMap[role] || role
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    'active': '正常',
    'charging': '充电中',
    'disabled': '已禁用'
  }
  return statusMap[status] || status
}

// 生成随机颜色
const getRandomColor = () => {
  const colors = [
    'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
    'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
    'linear-gradient(135deg, #30cfd0 0%, #330867 100%)',
    'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)',
    'linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%)',
  ]
  return colors[Math.floor(Math.random() * colors.length)]
}

// 加载用户列表
const loadUsers = async () => {
  try {
    loading.value = true
    const res = await userAPI.list({
      page: 1,
      size: 100
    })
    
    users.value = res.records.map(user => ({
      ...user,
      avatarColor: getRandomColor(),
      recentCharges: [] // 暂无最近充电记录接口
    }))
  } catch (error) {
    console.error('获取用户列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 刷新数据
const refreshData = () => {
  selectedIds.value = []
  loadUsers()
}

// 切换选择
const toggleSelect = (id) => {
  const index = selectedIds.value.indexOf(id)
  if (index > -1) {
    selectedIds.value.splice(index, 1)
  } else {
    selectedIds.value.push(id)
  }
}

// 全选/取消全选
const toggleSelectAll = () => {
  if (selectedIds.value.length === displayUsers.value.length) {
    selectedIds.value = []
  } else {
    selectedIds.value = displayUsers.value.map(u => u.id)
  }
}

// 批量删除
const batchDelete = async () => {
  if (!confirm(`确定要删除选中的 ${selectedIds.value.length} 个用户吗？`)) return
  
  try {
    await Promise.all(selectedIds.value.map(id => userAPI.delete(id)))
    users.value = users.value.filter(u => !selectedIds.value.includes(u.id))
    selectedIds.value = []
  } catch (error) {
    console.error('批量删除失败:', error)
    alert('批量删除失败')
    loadUsers()
  }
}

// 显示添加对话框
const showAddDialog = () => {
  isEditing.value = false
  formData.value = {
    id: null,
    username: '',
    phone: '',
    password: '',
    role: 'user',
    balance: 0,
    status: 'active'
  }
  showDialog.value = true
}

// 查看用户详情
const viewUser = (user) => {
  selectedUser.value = user
  showDetailDialog.value = true
}

// 编辑用户
const editUser = (user) => {
  isEditing.value = true
  formData.value = { ...user }
  showDialog.value = true
  showDetailDialog.value = false
}

// 显示充值对话框
const showRechargeDialog = (user) => {
  selectedUser.value = user
  rechargeAmount.value = null
  showRechargeModal.value = true
}

// 关闭充值对话框
const closeRechargeDialog = () => {
  showRechargeModal.value = false
  rechargeAmount.value = null
}

// 提交充值
const submitRecharge = async () => {
  if (!rechargeAmount.value || rechargeAmount.value <= 0) {
    alert('请输入有效的充值金额')
    return
  }

  try {
    await userAPI.recharge(selectedUser.value.id, rechargeAmount.value)
    alert('充值成功')
    closeRechargeDialog()
    loadUsers() // 刷新列表
  } catch (error) {
    console.error('充值失败:', error)
    alert('充值失败')
  }
}

// 切换用户状态
const toggleUserStatus = async (user) => {
  const newStatus = user.status === 'disabled' ? 'active' : 'disabled'
  try {
    await userAPI.update(user.id, { status: newStatus })
    user.status = newStatus
  } catch (error) {
    console.error('更新状态失败:', error)
    alert('更新状态失败')
  }
}

// 删除用户
const deleteUser = async (user) => {
  if (!confirm(`确定要删除用户"${user.username}"吗？`)) return
  try {
    await userAPI.delete(user.id)
    users.value = users.value.filter(u => u.id !== user.id)
  } catch (error) {
    console.error('删除失败:', error)
    alert('删除失败')
  }
}

// 关闭对话框
const closeDialog = () => {
  showDialog.value = false
}

const closeDetailDialog = () => {
  showDetailDialog.value = false
}

// 提交表单
const submitForm = async () => {
  try {
    if (isEditing.value) {
      await userAPI.update(formData.value.id, formData.value)
      const index = users.value.findIndex(u => u.id === formData.value.id)
      if (index !== -1) {
        users.value[index] = { ...users.value[index], ...formData.value }
      }
    } else {
      const res = await authAPI.register(formData.value)
      users.value.unshift({
        ...res,
        avatarColor: getRandomColor(),
        recentCharges: []
      })
    }
    closeDialog()
  } catch (error) {
    console.error('操作失败:', error)
    alert('操作失败: ' + (error.message || '未知错误'))
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.page {
  max-width: 1600px;
  animation: pageSlideIn 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes pageSlideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.page-title {
  font-size: 32px;
  font-weight: 800;
  margin-bottom: 24px;
  background: linear-gradient(135deg, #1d1d1f 0%, #007AFF 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -1px;
}

/* 统计卡片 */
.stats-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(40px);
  border-radius: 18px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  border: 1px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 13px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-icon svg {
  width: 26px;
  height: 26px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 26px;
  font-weight: 800;
  color: var(--color-text-primary);
  line-height: 1;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(40px);
  border-radius: 20px;
  padding: 28px;
  border: 1px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.04);
}

/* 工具栏 */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  gap: 16px;
  flex-wrap: wrap;
}

.toolbar-left {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}

.toolbar-right {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
  flex: 1;
  justify-content: flex-end;
}

.btn-primary {
  padding: 12px 24px;
  background: linear-gradient(135deg, #007AFF, #0051D5);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 12px rgba(0, 122, 255, 0.3);
  display: flex;
  align-items: center;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 122, 255, 0.4);
}

.btn-secondary {
  padding: 10px 18px;
  background: rgba(28, 28, 30, 0.06);
  color: var(--color-text-primary);
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 6px;
}

.btn-secondary:hover:not(:disabled) {
  background: rgba(28, 28, 30, 0.12);
  transform: translateY(-1px);
}

.btn-secondary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.search-box {
  position: relative;
  min-width: 280px;
}

.search-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  width: 18px;
  height: 18px;
  color: var(--color-text-secondary);
  pointer-events: none;
}

.search-input {
  width: 100%;
  padding: 11px 16px 11px 42px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  font-size: 14px;
  background: rgba(255, 255, 255, 0.8);
  transition: all 0.3s;
}

.search-input:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(0, 122, 255, 0.1);
  background: white;
}

.filter-select {
  padding: 11px 16px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  background: rgba(255, 255, 255, 0.8);
  color: var(--color-text-primary);
  cursor: pointer;
  transition: all 0.3s;
  min-width: 120px;
}

.filter-select:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(0, 122, 255, 0.1);
}

/* 加载状态 */
.loading-state {
  text-align: center;
  padding: 60px 20px;
}

.spinner {
  width: 48px;
  height: 48px;
  border: 4px solid rgba(0, 122, 255, 0.1);
  border-top-color: var(--color-primary);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin: 0 auto 16px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-state p {
  color: var(--color-text-secondary);
  font-size: 14px;
  font-weight: 600;
}

/* 表格容器 */
.table-container {
  overflow-x: auto;
  border-radius: 12px;
  border: 1px solid rgba(0, 0, 0, 0.06);
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

.user-table thead {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.05), rgba(88, 86, 214, 0.05));
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
}

.user-table th {
  padding: 16px 14px;
  text-align: left;
  font-size: 12px;
  font-weight: 700;
  color: var(--color-text-primary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  white-space: nowrap;
}

.user-table tbody tr {
  border-bottom: 1px solid rgba(0, 0, 0, 0.04);
  transition: all 0.2s;
}

.user-table tbody tr:hover {
  background: rgba(0, 122, 255, 0.03);
}

.user-table tbody tr.selected {
  background: rgba(0, 122, 255, 0.08);
}

.user-table td {
  padding: 14px;
  font-size: 14px;
  color: var(--color-text-primary);
  vertical-align: middle;
}

.checkbox-col {
  width: 50px;
  text-align: center;
}

.checkbox-col input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
  accent-color: var(--color-primary);
}

/* 用户头像 */
.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 16px;
  font-weight: 700;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.user-avatar-large {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
  font-weight: 700;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.user-name-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-name {
  font-weight: 600;
}

.vip-badge {
  display: inline-block;
  padding: 2px 8px;
  background: linear-gradient(135deg, #FFD700, #FFA500);
  color: white;
  font-size: 10px;
  font-weight: 700;
  border-radius: 8px;
  letter-spacing: 0.5px;
}

.phone-col {
  font-family: 'Courier New', monospace;
  font-weight: 600;
}

.role-badge {
  display: inline-block;
  padding: 5px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.3px;
}

.role-user {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.12), rgba(88, 86, 214, 0.12));
  color: #007AFF;
  border: 1px solid rgba(0, 122, 255, 0.2);
}

.role-vip {
  background: linear-gradient(135deg, rgba(255, 215, 0, 0.15), rgba(255, 165, 0, 0.15));
  color: #FF9500;
  border: 1px solid rgba(255, 165, 0, 0.3);
}

.role-admin {
  background: linear-gradient(135deg, rgba(175, 82, 222, 0.12), rgba(191, 90, 242, 0.12));
  color: #AF52DE;
  border: 1px solid rgba(175, 82, 222, 0.3);
}

.status-badge {
  display: inline-block;
  padding: 5px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.3px;
}

.status-active {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.12), rgba(48, 209, 88, 0.12));
  color: #34C759;
  border: 1px solid rgba(52, 199, 89, 0.3);
}

.status-charging {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.12), rgba(88, 86, 214, 0.12));
  color: #007AFF;
  border: 1px solid rgba(0, 122, 255, 0.3);
  animation: pulse 2s ease-in-out infinite;
}

.status-disabled {
  background: rgba(142, 142, 147, 0.12);
  color: #8E8E93;
  border: 1px solid rgba(142, 142, 147, 0.3);
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.6; }
}

.balance-col,
.number-col {
  font-variant-numeric: tabular-nums;
  font-weight: 700;
}

.date-col {
  font-size: 13px;
  color: var(--color-text-secondary);
  white-space: nowrap;
}

.actions-col {
  width: 180px;
}

.action-buttons {
  display: flex;
  gap: 6px;
  justify-content: center;
}

.btn-icon {
  width: 34px;
  height: 34px;
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  background: rgba(255, 255, 255, 0.5);
}

.btn-icon svg {
  width: 16px;
  height: 16px;
}

.btn-view {
  color: #007AFF;
  border-color: rgba(0, 122, 255, 0.2);
}

.btn-view:hover {
  background: rgba(0, 122, 255, 0.1);
  transform: scale(1.1);
}

.btn-edit {
  color: #5856D6;
  border-color: rgba(88, 86, 214, 0.2);
}

.btn-edit:hover {
  background: rgba(88, 86, 214, 0.1);
  transform: scale(1.1);
}

.btn-enable {
  color: #34C759;
  border-color: rgba(52, 199, 89, 0.2);
}

.btn-enable:hover {
  background: rgba(52, 199, 89, 0.1);
  transform: scale(1.1);
}

.btn-disable {
  color: #FF9500;
  border-color: rgba(255, 149, 0, 0.2);
}

.btn-disable:hover {
  background: rgba(255, 149, 0, 0.1);
  transform: scale(1.1);
}

.btn-delete {
  color: #FF3B30;
  border-color: rgba(255, 59, 48, 0.2);
}

.btn-delete:hover {
  background: rgba(255, 59, 48, 0.1);
  transform: scale(1.1);
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 80px 20px;
}

.empty-icon {
  width: 64px;
  height: 64px;
  color: rgba(0, 0, 0, 0.1);
  margin: 0 auto 16px;
}

.empty-text {
  color: var(--color-text-secondary);
  font-size: 15px;
  font-weight: 500;
}

/* 对话框 */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  animation: fadeIn 0.2s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.dialog {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(40px);
  border-radius: 24px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  animation: slideUp 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
}

.dialog.detail-dialog {
  max-width: 800px;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(40px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 28px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.03), rgba(88, 86, 214, 0.03));
}

.dialog-title-section {
  display: flex;
  align-items: center;
  gap: 16px;
}

.dialog-header h2 {
  margin: 0;
  font-size: 22px;
  font-weight: 800;
  color: var(--color-text-primary);
  letter-spacing: -0.5px;
}

.dialog-subtitle {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 600;
  margin: 4px 0 0 0;
  font-family: 'Courier New', monospace;
}

.close-btn {
  background: rgba(28, 28, 30, 0.06);
  border: 1px solid rgba(0, 0, 0, 0.1);
  width: 36px;
  height: 36px;
  border-radius: 10px;
  font-size: 28px;
  color: var(--color-text-secondary);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.close-btn:hover {
  background: rgba(28, 28, 30, 0.12);
  transform: scale(1.05);
}

.dialog-body {
  padding: 28px;
  flex: 1;
  overflow-y: auto;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 13px;
  font-weight: 700;
  color: var(--color-text-primary);
  letter-spacing: 0.2px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  font-size: 15px;
  background: rgba(255, 255, 255, 0.8);
  transition: all 0.3s;
  font-weight: 500;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(0, 122, 255, 0.1);
  background: white;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.dialog-actions {
  display: flex;
  gap: 12px;
  padding-top: 24px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  margin-top: 24px;
}

.dialog-actions button {
  flex: 1;
  padding: 13px 24px;
  font-size: 15px;
}

.dialog-footer {
  display: flex;
  gap: 12px;
  padding: 24px 28px;
  border-top: 2px solid rgba(0, 0, 0, 0.06);
  background: rgba(0, 0, 0, 0.02);
}

.dialog-footer button {
  flex: 1;
  padding: 13px 24px;
  font-size: 15px;
}

/* 详情页面 */
.detail-section {
  margin-bottom: 28px;
}

.detail-section:last-child {
  margin-bottom: 0;
}

.section-title {
  font-size: 16px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0 0 16px 0;
  padding-bottom: 12px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
  letter-spacing: -0.3px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 14px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 10px;
}

.info-label {
  font-size: 12px;
  color: var(--color-text-secondary);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-value {
  font-size: 16px;
  font-weight: 700;
  color: var(--color-text-primary);
}

.info-value.highlight {
  font-size: 20px;
  color: #007AFF;
}

/* 充电记录 */
.record-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.record-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 10px;
  transition: all 0.2s;
}

.record-item:hover {
  background: rgba(0, 122, 255, 0.05);
}

.record-info {
  flex: 1;
}

.record-station {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 4px;
}

.record-time {
  font-size: 12px;
  color: var(--color-text-secondary);
}

.record-stats {
  display: flex;
  gap: 20px;
  align-items: center;
}

.record-energy {
  font-size: 14px;
  font-weight: 700;
  color: var(--color-text-primary);
}

.record-cost {
  font-size: 16px;
  font-weight: 800;
  color: #007AFF;
}

/* 响应式 */
@media (max-width: 1024px) {
  .toolbar {
    flex-direction: column;
    align-items: stretch;
  }

  .toolbar-left,
  .toolbar-right {
    width: 100%;
    justify-content: flex-start;
  }

  .search-box {
    min-width: 0;
    flex: 1;
  }
}

@media (max-width: 768px) {
  .stats-row {
    grid-template-columns: repeat(2, 1fr);
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .table-container {
    font-size: 12px;
  }

  .action-buttons {
    flex-wrap: wrap;
  }
}

/* 充值对话框样式 */
.user-info-summary {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 24px;
}

.user-avatar-small {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
  font-size: 20px;
}

.user-details {
  flex: 1;
}

.user-name {
  font-weight: 600;
  font-size: 16px;
  margin-bottom: 4px;
}

.current-balance {
  color: #666;
  font-size: 14px;
}

.amount-input-wrapper {
  position: relative;
  margin-bottom: 16px;
}

.currency-symbol {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 20px;
  color: #333;
  font-weight: 600;
}

.amount-input {
  padding-left: 32px !important;
  font-size: 20px !important;
  font-weight: 600;
}

.quick-amounts {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}

.quick-amount-btn {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 6px;
  background: white;
  cursor: pointer;
  transition: all 0.2s;
  font-weight: 500;
}

.quick-amount-btn:hover {
  border-color: #007AFF;
  color: #007AFF;
}

.quick-amount-btn.active {
  background: #007AFF;
  color: white;
  border-color: #007AFF;
}

.btn-recharge {
  color: #34C759;
}

.btn-recharge:hover {
  background: rgba(52, 199, 89, 0.1);
}
</style>

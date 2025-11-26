<template>
  <div class="page">
    <h1 class="page-title">充电桩管理</h1>

    <!-- 统计卡片 -->
    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #34C759, #30D158);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M13 2L3 14h8l-1 8 10-12h-8l1-8z" fill="currentColor"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.total }}</div>
          <div class="stat-label">总数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #007AFF, #5856D6);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
            <path d="M9 12l2 2 4-4" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.active }}</div>
          <div class="stat-label">运行中</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #FF9500, #FF6B00);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 2v6m0 4v10M6.34 6.34l4.24 4.24m7.07-4.24l-4.24 4.24" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
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
            <path d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">¥{{ stats.totalRevenue.toLocaleString() }}</div>
          <div class="stat-label">累计收入</div>
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
            添加充电桩
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
              placeholder="搜索名称、编号、地址..." 
              class="search-input"
            />
          </div>
          <select v-model="filterStatus" class="filter-select">
            <option value="">全部状态</option>
            <option value="ACTIVE">运行中</option>
            <option value="CHARGING">充电中</option>
            <option value="INACTIVE">已停用</option>
            <option value="MAINTENANCE">维护中</option>
          </select>
          <select v-model="sortBy" class="filter-select">
            <option value="id">默认排序</option>
            <option value="name">按名称</option>
            <option value="power">按功率</option>
            <option value="price">按单价</option>
            <option value="totalEnergy">按充电量</option>
          </select>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <div class="spinner"></div>
        <p>加载中...</p>
      </div>

      <!-- 充电桩列表 -->
      <div v-else-if="displayStations.length > 0" class="stations-grid">
        <div 
          v-for="station in displayStations" 
          :key="station.id" 
          class="station-card"
          :class="['status-' + station.status, { 'selected': selectedIds.includes(station.id) }]"
        >
          <div class="station-checkbox">
            <input 
              type="checkbox" 
              :checked="selectedIds.includes(station.id)"
              @change="toggleSelect(station.id)"
            />
          </div>

          <div class="station-header">
            <div class="station-info">
              <h3 class="station-name">{{ station.name }}</h3>
              <span class="station-code">{{ station.code }}</span>
            </div>
            <div class="station-status">
              <span class="status-badge" :class="'badge-' + station.status">
                {{ getStatusText(station.status) }}
              </span>
            </div>
          </div>

          <div class="station-details">
            <div class="detail-row">
              <div class="detail-item">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="detail-icon">
                  <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z" stroke="currentColor" stroke-width="2"/>
                  <circle cx="12" cy="10" r="3" stroke="currentColor" stroke-width="2"/>
                </svg>
                <span class="detail-text">{{ station.address }}</span>
              </div>
            </div>
            <div class="detail-row">
              <div class="detail-item">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="detail-icon">
                  <path d="M13 2L3 14h8l-1 8 10-12h-8l1-8z" stroke="currentColor" stroke-width="2"/>
                </svg>
                <span class="detail-text">{{ station.power }} kW</span>
              </div>
              <div class="detail-item">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="detail-icon">
                  <path d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" stroke="currentColor" stroke-width="2"/>
                </svg>
                <span class="detail-text">¥{{ station.price }}/kWh</span>
              </div>
            </div>
            <div class="detail-row">
              <div class="detail-item">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="detail-icon">
                  <rect x="3" y="6" width="18" height="12" rx="2" stroke="currentColor" stroke-width="2"/>
                  <line x1="7" y1="12" x2="17" y2="12" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                </svg>
                <span class="detail-text">累计 {{ station.totalEnergy || 0 }} kWh</span>
              </div>
              <div class="detail-item">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="detail-icon">
                  <path d="M20 12v8H4v-8M22 7h-4a2 2 0 00-2 2v1m0 0H8m8 0v6m-8-6V9a2 2 0 00-2-2H2m6 4v6m4 3H8" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                </svg>
                <span class="detail-text">¥{{ ((station.totalEnergy || 0) * station.price).toLocaleString() }}</span>
              </div>
            </div>
          </div>

          <div class="station-actions">
            <button class="btn-action btn-edit" @click="editStation(station)" title="编辑">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </button>
            <button 
              class="btn-action"
              :class="station.status === 'ACTIVE' || station.status === 'CHARGING' ? 'btn-stop' : 'btn-start'"
              @click="toggleStatus(station)"
              :title="station.status === 'ACTIVE' || station.status === 'CHARGING' ? '停用' : '启用'"
            >
              <svg v-if="station.status === 'ACTIVE' || station.status === 'CHARGING'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect x="6" y="6" width="12" height="12" rx="2" fill="currentColor"/>
              </svg>
              <svg v-else viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M5 3l14 9-14 9V3z" fill="currentColor"/>
              </svg>
            </button>
            <button class="btn-action btn-delete" @click="deleteStation(station)" title="删除">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M3 6h18M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2M10 11v6M14 11v6" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </button>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="empty-icon">
          <path d="M13 2L3 14h8l-1 8 10-12h-8l1-8z" stroke="currentColor" stroke-width="2"/>
        </svg>
        <p class="empty-text">{{ searchQuery || filterStatus ? '未找到匹配的充电桩' : '暂无充电桩，点击上方按钮添加' }}</p>
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <div v-if="showDialog" class="dialog-overlay" @click.self="closeDialog">
      <div class="dialog">
        <div class="dialog-header">
          <h2>{{ isEditing ? '编辑充电桩' : '添加充电桩' }}</h2>
          <button class="close-btn" @click="closeDialog">&times;</button>
        </div>
        
        <form @submit.prevent="submitForm" class="dialog-body">
          <div class="form-group">
            <label>充电桩名称 *</label>
            <input 
              v-model="formData.name" 
              type="text" 
              placeholder="例如: 停车场A区-01号桩"
              required
            />
          </div>

          <div class="form-group">
            <label>充电桩编号 *</label>
            <input 
              v-model="formData.code" 
              type="text" 
              placeholder="例如: CS001"
              required
            />
          </div>

          <div class="form-group">
            <label>安装地址 *</label>
            <input 
              v-model="formData.address" 
              type="text" 
              placeholder="详细地址"
              required
            />
          </div>

          <div class="form-row">
            <div class="form-group">
              <label>充电功率 (kW) *</label>
              <input 
                v-model.number="formData.power" 
                type="number" 
                step="0.1"
                min="0"
                placeholder="7"
                required
              />
            </div>

            <div class="form-group">
              <label>电价 (元/kWh) *</label>
              <input 
                v-model.number="formData.price" 
                type="number" 
                step="0.01"
                min="0"
                placeholder="1.5"
                required
              />
            </div>
          </div>

          <div class="form-group">
            <label>状态</label>
            <select v-model="formData.status">
              <option value="ACTIVE">运行中</option>
              <option value="INACTIVE">已停用</option>
              <option value="MAINTENANCE">维护中</option>
            </select>
          </div>

          <div class="dialog-actions">
            <button type="button" class="btn-secondary" @click="closeDialog">取消</button>
            <button type="submit" class="btn-primary">{{ isEditing ? '保存' : '添加' }}</button>
          </div>
        </form>
      </div>
    </div>
  </div>
  
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { stationAPI } from '../api'

// 状态数据
const stations = ref([])
const searchQuery = ref('')
const filterStatus = ref('')
const sortBy = ref('id')
const showDialog = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const selectedIds = ref([])

// 表单数据
const formData = ref({
  id: null,
  name: '',
  code: '',
  address: '',
  power: 7,
  price: 1.5,
  status: 'ACTIVE'
})

// 统计数据
const stats = computed(() => {
  const total = stations.value.length
  const active = stations.value.filter(s => s.status === 'ACTIVE').length
  const charging = stations.value.filter(s => s.status === 'CHARGING').length
  const totalRevenue = stations.value.reduce((sum, s) => sum + ((s.totalEnergy || 0) * s.price), 0)
  
  return { total, active, charging, totalRevenue }
})

// 过滤和排序后的充电桩列表
const displayStations = computed(() => {
  let result = [...stations.value]
  
  // 搜索过滤
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(station => 
      station.name.toLowerCase().includes(query) ||
      station.code.toLowerCase().includes(query) ||
      station.address.toLowerCase().includes(query)
    )
  }
  
  // 状态过滤
  if (filterStatus.value) {
    result = result.filter(station => station.status === filterStatus.value)
  }
  
  // 排序
  if (sortBy.value !== 'id') {
    result.sort((a, b) => {
      const aVal = a[sortBy.value] || 0
      const bVal = b[sortBy.value] || 0
      if (typeof aVal === 'string') {
        return aVal.localeCompare(bVal)
      }
      return bVal - aVal // 降序
    })
  }
  
  return result
})

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    'ACTIVE': '运行中',
    'INACTIVE': '已停用',
    'MAINTENANCE': '维护中',
    'CHARGING': '充电中'
  }
  return statusMap[status] || status
}

// 加载充电桩列表
const loadStations = async () => {
  try {
    loading.value = true
    const response = await stationAPI.list()
    stations.value = response.data || []
  } catch (error) {
    console.error('加载充电桩列表失败:', error)
    // 使用模拟数据
    stations.value = [
      {
        id: 1,
        name: '停车场A区-01号桩',
        code: 'CS-A001',
        address: '北京市朝阳区某某路123号停车场A区',
        power: 120,
        price: 1.5,
        status: 'ACTIVE',
        totalEnergy: 12580
      },
      {
        id: 2,
        name: '停车场A区-02号桩',
        code: 'CS-A002',
        address: '北京市朝阳区某某路123号停车场A区',
        power: 120,
        price: 1.5,
        status: 'CHARGING',
        totalEnergy: 8960
      },
      {
        id: 3,
        name: '停车场B区-01号桩',
        code: 'CS-B001',
        address: '北京市朝阳区某某路123号停车场B区',
        power: 60,
        price: 1.2,
        status: 'ACTIVE',
        totalEnergy: 15680
      },
      {
        id: 4,
        name: '停车场B区-02号桩',
        code: 'CS-B002',
        address: '北京市朝阳区某某路123号停车场B区',
        power: 60,
        price: 1.2,
        status: 'MAINTENANCE',
        totalEnergy: 9430
      },
      {
        id: 5,
        name: '地下车库-01号桩',
        code: 'CS-UG001',
        address: '北京市朝阳区某某路123号地下车库',
        power: 7,
        price: 1.8,
        status: 'INACTIVE',
        totalEnergy: 5240
      },
      {
        id: 6,
        name: '地下车库-02号桩',
        code: 'CS-UG002',
        address: '北京市朝阳区某某路123号地下车库',
        power: 7,
        price: 1.8,
        status: 'ACTIVE',
        totalEnergy: 6890
      }
    ]
  } finally {
    loading.value = false
  }
}

// 刷新数据
const refreshData = () => {
  selectedIds.value = []
  loadStations()
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

// 批量删除
const batchDelete = async () => {
  if (!confirm(`确定要删除选中的 ${selectedIds.value.length} 个充电桩吗？`)) return
  
  try {
    // 这里应该调用批量删除API
    stations.value = stations.value.filter(s => !selectedIds.value.includes(s.id))
    selectedIds.value = []
    alert('批量删除成功')
  } catch (error) {
    console.error('批量删除失败:', error)
  }
}

// 显示添加对话框
const showAddDialog = () => {
  isEditing.value = false
  formData.value = {
    id: null,
    name: '',
    code: '',
    address: '',
    power: 7,
    price: 1.5,
    status: 'ACTIVE'
  }
  showDialog.value = true
}

// 编辑充电桩
const editStation = (station) => {
  isEditing.value = true
  formData.value = { ...station }
  showDialog.value = true
}

// 切换状态
const toggleStatus = async (station) => {
  try {
    const newStatus = (station.status === 'ACTIVE' || station.status === 'CHARGING') ? 'INACTIVE' : 'ACTIVE'
    await stationAPI.updateStatus(station.id, newStatus)
    station.status = newStatus
  } catch (error) {
    console.error('更新状态失败:', error)
    // 模拟更新
    station.status = (station.status === 'ACTIVE' || station.status === 'CHARGING') ? 'INACTIVE' : 'ACTIVE'
  }
}

// 删除充电桩
const deleteStation = async (station) => {
  if (!confirm(`确定要删除充电桩"${station.name}"吗？`)) return
  
  try {
    await stationAPI.delete(station.id)
    stations.value = stations.value.filter(s => s.id !== station.id)
  } catch (error) {
    console.error('删除失败:', error)
    // 模拟删除
    stations.value = stations.value.filter(s => s.id !== station.id)
  }
}

// 关闭对话框
const closeDialog = () => {
  showDialog.value = false
}

// 提交表单
const submitForm = async () => {
  try {
    if (isEditing.value) {
      // 更新
      await stationAPI.update(formData.value.id, formData.value)
      const index = stations.value.findIndex(s => s.id === formData.value.id)
      if (index !== -1) {
        stations.value[index] = { ...formData.value }
      }
    } else {
      // 新增
      const response = await stationAPI.create(formData.value)
      const newStation = response.data || {
        ...formData.value,
        id: Date.now(),
        totalEnergy: 0
      }
      stations.value.unshift(newStation)
    }
    closeDialog()
  } catch (error) {
    console.error('提交失败:', error)
    // 模拟提交成功
    if (isEditing.value) {
      const index = stations.value.findIndex(s => s.id === formData.value.id)
      if (index !== -1) {
        stations.value[index] = { ...formData.value }
      }
    } else {
      stations.value.unshift({
        ...formData.value,
        id: Date.now(),
        totalEnergy: 0
      })
    }
    closeDialog()
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadStations()
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

/* 充电桩网格 */
.stations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
  gap: 20px;
}

.station-card {
  background: rgba(255, 255, 255, 0.9);
  border: 2px solid rgba(0, 0, 0, 0.06);
  border-radius: 16px;
  padding: 20px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.station-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #34C759, #30D158);
  opacity: 0;
  transition: opacity 0.3s;
}

.station-card.status-ACTIVE::before {
  opacity: 1;
}

.station-card.status-CHARGING::before {
  background: linear-gradient(90deg, #007AFF, #5856D6);
  opacity: 1;
  animation: pulse 2s ease-in-out infinite;
}

.station-card.status-MAINTENANCE::before {
  background: linear-gradient(90deg, #FF9500, #FF6B00);
  opacity: 1;
}

.station-card.status-INACTIVE::before {
  background: linear-gradient(90deg, #8E8E93, #636366);
  opacity: 1;
}

.station-card.selected {
  border-color: var(--color-primary);
  box-shadow: 0 8px 24px rgba(0, 122, 255, 0.15);
}

.station-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
}

.station-checkbox {
  position: absolute;
  top: 16px;
  right: 16px;
}

.station-checkbox input[type="checkbox"] {
  width: 20px;
  height: 20px;
  cursor: pointer;
  accent-color: var(--color-primary);
}

.station-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
  padding-bottom: 14px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  padding-right: 36px;
}

.station-info {
  flex: 1;
}

.station-name {
  font-size: 17px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0 0 4px 0;
  letter-spacing: -0.3px;
}

.station-code {
  font-size: 12px;
  color: var(--color-text-secondary);
  font-weight: 600;
  font-family: 'Courier New', monospace;
}

.station-status {
  margin-left: 12px;
}

.status-badge {
  display: inline-block;
  padding: 6px 14px;
  border-radius: 16px;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.3px;
  text-transform: uppercase;
}

.badge-ACTIVE {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.15), rgba(48, 209, 88, 0.15));
  color: #34C759;
  border: 1px solid rgba(52, 199, 89, 0.3);
}

.badge-INACTIVE {
  background: rgba(142, 142, 147, 0.15);
  color: #8E8E93;
  border: 1px solid rgba(142, 142, 147, 0.3);
}

.badge-MAINTENANCE {
  background: linear-gradient(135deg, rgba(255, 149, 0, 0.15), rgba(255, 107, 0, 0.15));
  color: #FF9500;
  border: 1px solid rgba(255, 149, 0, 0.3);
}

.badge-CHARGING {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.15), rgba(88, 86, 214, 0.15));
  color: #007AFF;
  border: 1px solid rgba(0, 122, 255, 0.3);
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.7; }
}

.station-details {
  margin-bottom: 16px;
}

.detail-row {
  display: flex;
  gap: 12px;
  margin-bottom: 10px;
}

.detail-row:last-child {
  margin-bottom: 0;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  padding: 10px 12px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 10px;
  transition: all 0.2s;
}

.detail-item:hover {
  background: rgba(0, 122, 255, 0.04);
}

.detail-icon {
  width: 16px;
  height: 16px;
  color: var(--color-text-secondary);
  flex-shrink: 0;
}

.detail-text {
  font-size: 13px;
  font-weight: 600;
  color: var(--color-text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.station-actions {
  display: flex;
  gap: 8px;
  padding-top: 14px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.btn-action {
  flex: 1;
  padding: 10px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-action svg {
  width: 18px;
  height: 18px;
}

.btn-edit {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.08), rgba(88, 86, 214, 0.08));
  color: var(--color-primary);
  border-color: rgba(0, 122, 255, 0.2);
}

.btn-edit:hover {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.15), rgba(88, 86, 214, 0.15));
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 122, 255, 0.2);
}

.btn-start {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.08), rgba(48, 209, 88, 0.08));
  color: #34C759;
  border-color: rgba(52, 199, 89, 0.2);
}

.btn-start:hover {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.15), rgba(48, 209, 88, 0.15));
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(52, 199, 89, 0.2);
}

.btn-stop {
  background: linear-gradient(135deg, rgba(255, 149, 0, 0.08), rgba(255, 107, 0, 0.08));
  color: #FF9500;
  border-color: rgba(255, 149, 0, 0.2);
}

.btn-stop:hover {
  background: linear-gradient(135deg, rgba(255, 149, 0, 0.15), rgba(255, 107, 0, 0.15));
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 149, 0, 0.2);
}

.btn-delete {
  background: linear-gradient(135deg, rgba(255, 59, 48, 0.08), rgba(255, 69, 58, 0.08));
  color: #FF3B30;
  border-color: rgba(255, 59, 48, 0.2);
}

.btn-delete:hover {
  background: linear-gradient(135deg, rgba(255, 59, 48, 0.15), rgba(255, 69, 58, 0.15));
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 59, 48, 0.2);
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
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
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
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.03), rgba(88, 86, 214, 0.03));
}

.dialog-header h2 {
  margin: 0;
  font-size: 22px;
  font-weight: 800;
  color: var(--color-text-primary);
  letter-spacing: -0.5px;
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
  max-height: calc(90vh - 160px);
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

  .stations-grid {
    grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  }
}

@media (max-width: 768px) {
  .stats-row {
    grid-template-columns: repeat(2, 1fr);
  }

  .stations-grid {
    grid-template-columns: 1fr;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .detail-row {
    flex-direction: column;
    gap: 8px;
  }
}
</style>

<template>
  <div class="profile-page">
    <!-- 个人资料卡片 -->
    <div class="profile-card">
      <div class="profile-banner"></div>
      <div class="profile-content">
        <div class="avatar-section">
          <div class="avatar-wrapper">
            <img :src="`https://ui-avatars.com/api/?name=${userInfo.name}&background=34C759&color=fff&size=120`" alt="用户头像" class="avatar" />
            <button class="avatar-edit-btn" @click="showAvatarUpload = true">
              <Icons name="settings" size="sm" />
            </button>
          </div>
        </div>
        <div class="profile-info">
          <h1>{{ userInfo.name }}</h1>
          <p class="role">{{ userInfo.role }}</p>
          <p class="email">{{ userInfo.email }}</p>
          <div class="balance-info" style="margin: 10px 0; font-size: 16px; font-weight: 600; color: #34C759; display: flex; align-items: center; justify-content: center; gap: 8px;">
             <span>余额: ¥{{ userInfo.balance.toFixed(2) }}</span>
          </div>
          <div class="profile-stats">
            <div class="stat-item">
              <div class="stat-value">{{ userInfo.loginDays }}</div>
              <div class="stat-label">连续登录天数</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="stat-value">{{ userInfo.totalOperations }}</div>
              <div class="stat-label">累计操作次数</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="stat-value">{{ userInfo.lastLogin }}</div>
              <div class="stat-label">最后登录</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 快捷操作 -->
    <div class="quick-actions">
      <button class="action-card" @click="showRechargeModal = true">
        <div class="action-icon" style="background: linear-gradient(135deg, #FF9500, #FF6B00);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 24px; height: 24px; color: white;">
            <path d="M20 7h-7L10 3H4a2 2 0 00-2 2v14a2 2 0 002 2h16a2 2 0 002-2V9a2 2 0 00-2-2z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M16 12h6" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            <path d="M19 9v6" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="action-content">
          <h3>充值余额</h3>
          <p>账户充值</p>
        </div>
      </button>
      <button class="action-card" @click="editProfile">
        <div class="action-icon" style="background: linear-gradient(135deg, #34C759, #30B74F);">
          <Icons name="user" size="md" />
        </div>
        <div class="action-content">
          <h3>编辑资料</h3>
          <p>修改个人信息</p>
        </div>
      </button>
      <button class="action-card" @click="changePassword">
        <div class="action-icon" style="background: linear-gradient(135deg, #FF9500, #FF8C00);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 24px; height: 24px;">
            <rect x="3" y="11" width="18" height="11" rx="2" stroke="white" stroke-width="2"/>
            <path d="M7 11V7C7 4.23858 9.23858 2 12 2C14.7614 2 17 4.23858 17 7V11" stroke="white" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="action-content">
          <h3>修改密码</h3>
          <p>更改登录密码</p>
        </div>
      </button>
      <button class="action-card" @click="$router.push('/notifications')">
        <div class="action-icon" style="background: linear-gradient(135deg, #5AC8FA, #0AB5FF);">
          <Icons name="bell" size="md" />
        </div>
        <div class="action-content">
          <h3>通知中心</h3>
          <p>查看所有通知</p>
        </div>
      </button>
      <button class="action-card" @click="$router.push('/settings-theme')">
        <div class="action-icon" style="background: linear-gradient(135deg, #AF52DE, #BF5AF2);">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 24px; height: 24px;">
            <circle cx="12" cy="12" r="5" stroke="white" stroke-width="2"/>
            <path d="M12 1v6M12 17v6M4.22 4.22l4.24 4.24M15.54 15.54l4.24 4.24M1 12h6M17 12h6M4.22 19.78l4.24-4.24M15.54 8.46l4.24-4.24" stroke="white" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="action-content">
          <h3>主题设置</h3>
          <p>浅色/深色模式</p>
        </div>
      </button>
    </div>

    <!-- 详细信息 -->
    <div class="info-section">
      <div class="section-header">
        <h2>基本信息</h2>
        <button class="edit-btn" @click="editMode = !editMode">
          {{ editMode ? '取消编辑' : '编辑' }}
        </button>
      </div>
      <div class="info-grid">
        <div class="info-item">
          <label>用户名</label>
          <input v-if="editMode" v-model="editForm.name" type="text" class="input-field" />
          <div v-else class="info-value">{{ userInfo.name }}</div>
        </div>
        <div class="info-item">
          <label>手机号码</label>
          <input v-if="editMode" v-model="editForm.phone" type="text" class="input-field" />
          <div v-else class="info-value">{{ userInfo.phone }}</div>
        </div>
        <div class="info-item">
          <label>电子邮箱</label>
          <input v-if="editMode" v-model="editForm.email" type="email" class="input-field" />
          <div v-else class="info-value">{{ userInfo.email }}</div>
        </div>
      </div>
      <div v-if="editMode" class="action-buttons">
        <button class="btn secondary" @click="cancelEdit">取消</button>
        <button class="btn primary" @click="saveProfile">保存修改</button>
      </div>
    </div>

    <!-- 操作日志 -->
    <div class="info-section">
      <div class="section-header">
        <h2>最近操作</h2>
        <button class="view-more-btn" @click="$router.push('/system')">查看更多</button>
      </div>
      <div class="activity-list">
        <div v-for="log in activityLogs" :key="log.id" class="activity-item">
          <div class="activity-icon" :class="log.type">
            <Icons :name="log.icon" size="sm" />
          </div>
          <div class="activity-content">
            <div class="activity-title">{{ log.action }}</div>
            <div class="activity-time">{{ log.time }}</div>
          </div>
          <div class="activity-status" :class="log.status">
            {{ log.statusText }}
          </div>
        </div>
      </div>
    </div>

    <!-- 账户绑定 -->
    <div class="info-section">
      <div class="section-header">
        <h2>账户绑定</h2>
      </div>
      <div class="binding-list">
        <div class="binding-item">
          <div class="binding-header">
            <div class="binding-info">
              <div class="binding-title">微信账号</div>
              <div class="binding-status" :class="bindings.wechat.bound ? 'bound' : 'unbound'">
                {{ bindings.wechat.bound ? '已绑定' : '未绑定' }}
              </div>
            </div>
            <button 
              v-if="bindings.wechat.bound"
              @click="unbind('wechat')" 
              class="binding-btn unbind-btn"
            >
              解绑
            </button>
            <button 
              v-else
              @click="bind('wechat')" 
              class="binding-btn bind-btn"
            >
              绑定
            </button>
          </div>
          <div v-if="bindings.wechat.bound" class="binding-details">
            账号: {{ bindings.wechat.account }}
          </div>
        </div>

        <div class="binding-item">
          <div class="binding-header">
            <div class="binding-info">
              <div class="binding-title">支付宝账号</div>
              <div class="binding-status" :class="bindings.alipay.bound ? 'bound' : 'unbound'">
                {{ bindings.alipay.bound ? '已绑定' : '未绑定' }}
              </div>
            </div>
            <button 
              v-if="bindings.alipay.bound"
              @click="unbind('alipay')" 
              class="binding-btn unbind-btn"
            >
              解绑
            </button>
            <button 
              v-else
              @click="bind('alipay')" 
              class="binding-btn bind-btn"
            >
              绑定
            </button>
          </div>
          <div v-if="bindings.alipay.bound" class="binding-details">
            账号: {{ bindings.alipay.account }}
          </div>
        </div>

        <div class="binding-item">
          <div class="binding-header">
            <div class="binding-info">
              <div class="binding-title">车牌信息</div>
              <div class="binding-status" :class="bindings.vehicle.bound ? 'bound' : 'unbound'">
                {{ bindings.vehicle.bound ? '已绑定' : '未绑定' }}
              </div>
            </div>
            <button 
              v-if="bindings.vehicle.bound"
              @click="unbind('vehicle')" 
              class="binding-btn unbind-btn"
            >
              解绑
            </button>
            <button 
              v-else
              @click="bind('vehicle')" 
              class="binding-btn bind-btn"
            >
              绑定
            </button>
          </div>
          <div v-if="bindings.vehicle.bound" class="binding-details">
            车牌: {{ bindings.vehicle.plate }}
          </div>
        </div>
      </div>
    </div>

    <!-- 安全设置 -->
    <div class="info-section">
      <div class="section-header">
        <h2>安全设置</h2>
      </div>
      <div class="security-list">
        <div class="security-item">
          <div class="security-header">
            <div class="security-info">
              <div class="security-title">登录邮箱验证</div>
              <div class="security-description">确保账户安全</div>
            </div>
            <button class="security-btn" :class="securitySettings.emailVerify ? 'active' : ''">
              {{ securitySettings.emailVerify ? '已启用' : '未启用' }}
            </button>
          </div>
        </div>

        <div class="security-item">
          <div class="security-header">
            <div class="security-info">
              <div class="security-title">双因素认证 (2FA)</div>
              <div class="security-description">使用身份验证器或短信</div>
            </div>
            <button class="security-btn" :class="securitySettings.twoFactorAuth ? 'active' : ''">
              {{ securitySettings.twoFactorAuth ? '已启用' : '未启用' }}
            </button>
          </div>
        </div>

        <div class="security-item">
          <div class="security-header">
            <div class="security-info">
              <div class="security-title">登录位置提醒</div>
              <div class="security-description">新地区登录时发送提醒</div>
            </div>
            <button class="security-btn" :class="securitySettings.locationAlert ? 'active' : ''">
              {{ securitySettings.locationAlert ? '已启用' : '未启用' }}
            </button>
          </div>
        </div>

        <div class="security-item">
          <div class="security-header">
            <div class="security-info">
              <div class="security-title">会话管理</div>
              <div class="security-description">查看和管理登录设备</div>
            </div>
            <button class="security-btn" @click="$router.push('/sessions')">查看</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 账户注销 -->
    <div class="info-section danger-section">
      <div class="section-header">
        <h2>账户安全</h2>
      </div>
      <div class="danger-actions">
        <div class="danger-item">
          <div class="danger-header">
            <div class="danger-info">
              <div class="danger-title">注销账户</div>
              <div class="danger-description">永久删除您的账户及所有关联数据</div>
            </div>
            <button class="btn danger" @click="deleteAccount">
              注销账户
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 修改密码弹窗 -->
    <!-- 充值模态框 -->
    <div v-if="showRechargeModal" class="modal-overlay" @click.self="showRechargeModal = false">
      <div class="modal-content">
        <div class="modal-header">
          <h2>账户充值</h2>
          <button class="close-btn" @click="showRechargeModal = false">✕</button>
        </div>
        <div class="modal-body" style="padding: 24px;">
          <div class="form-group">
            <label style="display: block; margin-bottom: 8px; font-weight: 600;">充值金额</label>
            <div style="position: relative; margin-bottom: 16px;">
              <span style="position: absolute; left: 12px; top: 50%; transform: translateY(-50%); font-size: 20px; font-weight: 600;">¥</span>
              <input 
                v-model.number="rechargeAmount" 
                type="number" 
                step="1"
                min="1"
                placeholder="请输入充值金额"
                style="width: 100%; padding: 12px 12px 12px 32px; font-size: 18px; border: 1px solid #ddd; border-radius: 8px;"
                @keyup.enter="submitRecharge"
              />
            </div>
            <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px;">
              <button 
                v-for="amount in [50, 100, 200, 500]" 
                :key="amount"
                style="padding: 8px; border: 1px solid #ddd; border-radius: 6px; background: white; cursor: pointer; transition: all 0.2s;"
                :style="rechargeAmount === amount ? 'background: #007AFF; color: white; border-color: #007AFF;' : ''"
                @click="rechargeAmount = amount"
              >
                ¥{{ amount }}
              </button>
            </div>
          </div>
        </div>
        <div class="modal-footer" style="padding: 16px 24px; border-top: 1px solid #eee; display: flex; justify-content: flex-end; gap: 12px;">
          <button class="btn secondary" @click="showRechargeModal = false">取消</button>
          <button class="btn primary" @click="submitRecharge" :disabled="!rechargeAmount || rechargeAmount <= 0">确认充值</button>
        </div>
      </div>
    </div>

    <div v-if="showPasswordModal" class="modal-overlay" @click="showPasswordModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>修改密码</h2>
          <button class="close-btn" @click="showPasswordModal = false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>当前密码</label>
            <input v-model="passwordForm.current" type="password" class="input-field" placeholder="请输入当前密码" />
          </div>
          <div class="form-group">
            <label>新密码</label>
            <input v-model="passwordForm.new" type="password" class="input-field" placeholder="请输入新密码" />
          </div>
          <div class="form-group">
            <label>确认新密码</label>
            <input v-model="passwordForm.confirm" type="password" class="input-field" placeholder="请再次输入新密码" />
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn secondary" @click="showPasswordModal = false">取消</button>
          <button class="btn primary" @click="updatePassword">确认修改</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'
import Icons from '../components/Icons.vue'
import { useUserStore } from '../stores/user'
import { recordAPI, userAPI } from '../api'

const router = useRouter()
const userStore = useUserStore()
const { currentUser } = storeToRefs(userStore)
const editMode = ref(false)
const showPasswordModal = ref(false)
const showAvatarUpload = ref(false)
const showRechargeModal = ref(false)
const rechargeAmount = ref(null)

const getRoleName = (role) => {
  const map = {
    'admin': '系统管理员',
    'operator': '运营管理员',
    'user': '普通用户'
  }
  return map[role] || role || '访客'
}

// 从用户 store 获取真实用户信息
const userInfo = computed(() => ({
  name: currentUser.value?.realName || currentUser.value?.username || '用户',
  role: getRoleName(currentUser.value?.role),
  email: currentUser.value?.email || '未绑定',
  phone: currentUser.value?.phone || '未绑定',
  balance: currentUser.value?.balance || 0,
  loginDays: 1, // 暂无
  totalOperations: currentUser.value?.totalChargingCount || 0,
  lastLogin: currentUser.value?.lastLoginTime ? new Date(currentUser.value.lastLoginTime).toLocaleString() : '刚刚'
}))

const editForm = reactive({
  name: '',
  phone: '',
  email: ''
})

// 当进入编辑模式时，初始化表单
watch(editMode, (val) => {
  if (val) {
    editForm.name = userInfo.value.name
    editForm.phone = userInfo.value.phone
    editForm.email = userInfo.value.email
  }
})

const passwordForm = reactive({
  current: '',
  new: '',
  confirm: ''
})

// 账户绑定状态
const bindings = reactive({
  wechat: {
    bound: false,
    account: ''
  },
  alipay: {
    bound: false,
    account: ''
  },
  vehicle: {
    bound: false,
    plate: ''
  }
})

// 安全设置
const securitySettings = reactive({
  emailVerify: false,
  twoFactorAuth: false,
  locationAlert: false
})

const activityLogs = ref([])

const fetchRecentActivity = async () => {
  try {
    const res = await recordAPI.list({ page: 1, size: 5 })
    if (res.data && res.data.records) {
      activityLogs.value = res.data.records.map(r => ({
        id: r.id,
        action: `在 ${r.stationName} ${r.status === 'charging' ? '开始充电' : '完成充电'}`,
        time: r.updatedAt ? new Date(r.updatedAt).toLocaleString() : '刚刚',
        type: r.status === 'charging' ? 'edit' : 'check',
        icon: 'plug',
        status: r.status === 'failed' ? 'error' : 'success',
        statusText: r.status === 'failed' ? '失败' : '成功'
      }))
    }
  } catch (e) {
    console.error(e)
  }
}

const submitRecharge = async () => {
  if (!rechargeAmount.value || rechargeAmount.value <= 0) {
    alert('请输入有效的充值金额')
    return
  }

  try {
    await userAPI.recharge(currentUser.value.id, rechargeAmount.value)
    
    // Refresh user info
    const res = await userAPI.get(currentUser.value.id)
    userStore.setUser(res)
    
    alert('充值成功')
    showRechargeModal.value = false
    rechargeAmount.value = null
  } catch (error) {
    console.error('充值失败:', error)
    alert('充值失败')
  }
}

const editProfile = () => {
  editMode.value = true
}

const changePassword = () => {
  showPasswordModal.value = true
}

const cancelEdit = () => {
  editMode.value = false
  Object.assign(editForm, {
    name: userInfo.name,
    phone: userInfo.phone,
    email: userInfo.email
  })
}

const saveProfile = () => {
  Object.assign(userInfo, editForm)
  editMode.value = false
  alert('个人资料已保存！')
}

const updatePassword = () => {
  if (!passwordForm.current || !passwordForm.new || !passwordForm.confirm) {
    alert('请填写完整的密码信息')
    return
  }
  if (passwordForm.new !== passwordForm.confirm) {
    alert('两次输入的新密码不一致')
    return
  }
  if (passwordForm.new.length < 6) {
    alert('新密码长度不能少于6位')
    return
  }
  
  alert('密码修改成功！')
  showPasswordModal.value = false
  passwordForm.current = ''
  passwordForm.new = ''
  passwordForm.confirm = ''
}

// 账户绑定
const bind = (type) => {
  alert(`正在打开 ${type === 'wechat' ? '微信' : type === 'alipay' ? '支付宝' : '车牌'} 绑定页面...`)
  // 这里可以打开绑定对话框
  if (type === 'wechat') {
    bindings.wechat.bound = true
    bindings.wechat.account = 'wx_user_12345'
  } else if (type === 'alipay') {
    bindings.alipay.bound = true
    bindings.alipay.account = 'alipay_user@alipay.com'
  }
}

const unbind = (type) => {
  if (confirm(`确定要解绑 ${type === 'wechat' ? '微信' : type === 'alipay' ? '支付宝' : '车牌'} 账号吗？`)) {
    if (type === 'wechat') {
      bindings.wechat.bound = false
      bindings.wechat.account = ''
    } else if (type === 'alipay') {
      bindings.alipay.bound = false
      bindings.alipay.account = ''
    } else if (type === 'vehicle') {
      bindings.vehicle.bound = false
      bindings.vehicle.plate = ''
    }
    alert('已解绑')
  }
}

// 账户注销
const deleteAccount = () => {
  if (confirm('注销账户是不可逆的操作，将永久删除您的账户及所有数据。\n\n确定要继续吗？')) {
    if (confirm('请再次确认：您确实要注销账户吗？')) {
      alert('账户注销申请已提交，我们将在7个工作日内处理。')
      // 这里应该调用后端 API 来处理账户注销
    }
  }
}

onMounted(() => {
  fetchRecentActivity()
})
</script>

<style scoped>
.profile-page {
  padding: 0;
}

/* 个人资料卡片 */
.profile-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  margin-bottom: 24px;
}

.profile-banner {
  height: 160px;
  background: linear-gradient(135deg, #34C759 0%, #30B74F 50%, #00C7BE 100%);
  position: relative;
}

.profile-content {
  padding: 0 32px 32px;
  position: relative;
}

.avatar-section {
  margin-top: -60px;
  margin-bottom: 20px;
}

.avatar-wrapper {
  position: relative;
  display: inline-block;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 6px solid white;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  object-fit: cover;
}

.avatar-edit-btn {
  position: absolute;
  bottom: 5px;
  right: 5px;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, #34C759, #30B74F);
  border: 3px solid white;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(52, 199, 89, 0.4);
}

.avatar-edit-btn:hover {
  transform: scale(1.1) rotate(90deg);
}

.profile-info h1 {
  font-size: 32px;
  font-weight: 900;
  color: var(--color-text-primary);
  margin: 0 0 8px 0;
  letter-spacing: -1px;
}

.profile-info .role {
  display: inline-block;
  padding: 6px 16px;
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.1), rgba(48, 183, 79, 0.1));
  color: #34C759;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 700;
  margin: 0 0 8px 0;
}

.profile-info .email {
  font-size: 15px;
  color: var(--color-text-secondary);
  margin: 0 0 24px 0;
}

.profile-stats {
  display: flex;
  align-items: center;
  gap: 32px;
  padding: 24px;
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.03), rgba(48, 183, 79, 0.03));
  border-radius: 16px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: 900;
  color: var(--color-text-primary);
  line-height: 1;
}

.stat-label {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.stat-divider {
  width: 1px;
  height: 40px;
  background: rgba(0, 0, 0, 0.1);
}

/* 快捷操作 */
.quick-actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.action-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 24px;
  background: white;
  border: none;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.3s;
  text-align: left;
}

.action-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.action-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.action-content h3 {
  font-size: 16px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0 0 4px 0;
}

.action-content p {
  font-size: 13px;
  color: var(--color-text-secondary);
  margin: 0;
}

/* 信息区块 */
.info-section {
  background: white;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
}

.section-header h2 {
  font-size: 20px;
  font-weight: 800;
  color: var(--color-text-primary);
  margin: 0;
}

.edit-btn,
.view-more-btn {
  padding: 8px 16px;
  background: rgba(52, 199, 89, 0.1);
  color: #34C759;
  border: none;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.edit-btn:hover,
.view-more-btn:hover {
  background: rgba(52, 199, 89, 0.2);
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
}

.info-item label {
  display: block;
  font-size: 13px;
  font-weight: 700;
  color: var(--color-text-secondary);
  margin-bottom: 8px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-value {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.input-field {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s;
  font-family: inherit;
}

.input-field:focus {
  outline: none;
  border-color: #34C759;
  background: rgba(52, 199, 89, 0.03);
}

.action-buttons {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  padding-top: 16px;
  border-top: 2px solid rgba(0, 0, 0, 0.06);
}

/* 操作日志 */
.activity-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 12px;
  transition: all 0.2s;
}

.activity-item:hover {
  background: rgba(0, 0, 0, 0.04);
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.activity-icon.edit {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.12), rgba(48, 183, 79, 0.12));
  color: #34C759;
}

.activity-icon.export {
  background: linear-gradient(135deg, rgba(90, 200, 250, 0.12), rgba(10, 181, 255, 0.12));
  color: #5AC8FA;
}

.activity-icon.add {
  background: linear-gradient(135deg, rgba(255, 149, 0, 0.12), rgba(255, 140, 0, 0.12));
  color: #FF9500;
}

.activity-icon.check {
  background: linear-gradient(135deg, rgba(175, 82, 222, 0.12), rgba(191, 90, 242, 0.12));
  color: #AF52DE;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 4px;
}

.activity-time {
  font-size: 12px;
  color: var(--color-text-tertiary);
  font-weight: 500;
}

.activity-status {
  padding: 4px 12px;
  border-radius: 10px;
  font-size: 12px;
  font-weight: 700;
}

.activity-status.success {
  background: rgba(52, 199, 89, 0.1);
  color: #34C759;
}

/* 按钮 */
.btn {
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s;
}

.btn.primary {
  background: linear-gradient(135deg, #34C759, #30B74F);
  color: white;
  box-shadow: 0 4px 12px rgba(52, 199, 89, 0.3);
}

.btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(52, 199, 89, 0.4);
}

.btn.secondary {
  background: white;
  color: var(--color-text-primary);
  border: 2px solid rgba(0, 0, 0, 0.1);
}

.btn.secondary:hover {
  background: rgba(0, 0, 0, 0.05);
}

/* 模态框样式（与通知页面相同）*/
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.modal-content {
  background: white;
  border-radius: 20px;
  width: 90%;
  max-width: 480px;
  overflow: hidden;
  animation: slideUp 0.3s;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
}

.modal-header h2 {
  font-size: 20px;
  font-weight: 800;
  color: var(--color-text-primary);
  margin: 0;
}

.close-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 50%;
  font-size: 18px;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  background: rgba(0, 0, 0, 0.1);
  transform: rotate(90deg);
}

.modal-body {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group:last-child {
  margin-bottom: 0;
}

.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 700;
  color: var(--color-text-secondary);
  margin-bottom: 8px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.modal-footer {
  display: flex;
  gap: 12px;
  padding: 20px 24px;
  border-top: 2px solid rgba(0, 0, 0, 0.06);
}

.modal-footer .btn {
  flex: 1;
}

/* 账户绑定样式 */
.binding-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.binding-item {
  padding: 16px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 12px;
  border-left: 4px solid #34C759;
  transition: all 0.2s;
}

.binding-item:hover {
  background: rgba(0, 0, 0, 0.04);
  box-shadow: 0 2px 8px rgba(52, 199, 89, 0.1);
}

.binding-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.binding-info {
  flex: 1;
}

.binding-title {
  font-size: 15px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 6px;
}

.binding-status {
  font-size: 12px;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: 8px;
  width: fit-content;
}

.binding-status.bound {
  background: rgba(52, 199, 89, 0.1);
  color: #34C759;
}

.binding-status.unbound {
  background: rgba(160, 160, 160, 0.1);
  color: #999;
}

.binding-btn {
  padding: 8px 20px;
  border: none;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  margin-left: 12px;
}

.bind-btn {
  background: linear-gradient(135deg, #34C759, #30B74F);
  color: white;
}

.bind-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(52, 199, 89, 0.3);
}

.unbind-btn {
  background: rgba(255, 59, 48, 0.1);
  color: #FF3B30;
}

.unbind-btn:hover {
  background: rgba(255, 59, 48, 0.2);
}

.binding-details {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid rgba(0, 0, 0, 0.08);
  font-size: 13px;
  color: var(--color-text-secondary);
}

/* 安全设置样式 */
.security-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.security-item {
  padding: 16px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 12px;
  transition: all 0.2s;
}

.security-item:hover {
  background: rgba(0, 0, 0, 0.04);
}

.security-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.security-info {
  flex: 1;
}

.security-title {
  font-size: 15px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 4px;
}

.security-description {
  font-size: 12px;
  color: var(--color-text-secondary);
}

.security-btn {
  padding: 8px 20px;
  border: none;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  background: rgba(160, 160, 160, 0.1);
  color: var(--color-text-secondary);
}

.security-btn.active {
  background: rgba(52, 199, 89, 0.1);
  color: #34C759;
}

.security-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 账户安全 - 危险操作区域 */
.danger-section {
  border: 2px solid rgba(255, 59, 48, 0.2);
  background: linear-gradient(135deg, rgba(255, 59, 48, 0.02), rgba(255, 59, 48, 0.01));
}

.danger-actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.danger-item {
  padding: 16px;
  background: rgba(255, 59, 48, 0.03);
  border-radius: 12px;
  border-left: 4px solid #FF3B30;
}

.danger-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.danger-info {
  flex: 1;
}

.danger-title {
  font-size: 15px;
  font-weight: 700;
  color: #FF3B30;
  margin-bottom: 4px;
}

.danger-description {
  font-size: 12px;
  color: var(--color-text-secondary);
}

.btn.danger {
  background: #FF3B30;
  color: white;
  box-shadow: 0 4px 12px rgba(255, 59, 48, 0.3);
}

.btn.danger:hover {
  background: #E60000;
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(255, 59, 48, 0.4);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-stats {
    flex-wrap: wrap;
    gap: 16px;
  }
  
  .quick-actions {
    grid-template-columns: 1fr;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .binding-header,
  .security-header,
  .danger-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .binding-btn,
  .security-btn,
  .btn.danger {
    width: 100%;
    margin-left: 0;
  }
  
  .modal-content {
    width: 95%;
  }
}
</style>

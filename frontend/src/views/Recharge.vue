<template>
  <div class="page recharge-page">
    <h1 class="page-title">充值中心</h1>

    <div class="recharge-container">
      <!-- 余额卡片 -->
      <div class="balance-card">
        <div class="balance-header">
          <span class="label">当前余额</span>
          <span class="currency">CNY</span>
        </div>
        <div class="balance-amount">
          <span class="symbol">¥</span>
          <span class="value">{{ currentUser?.balance?.toFixed(2) || '0.00' }}</span>
        </div>
        <div class="balance-footer">
          <div class="account-info">
            <span class="account-label">账户ID:</span>
            <span class="account-value">{{ currentUser?.id }}</span>
          </div>
          <div class="account-info">
            <span class="account-label">用户名:</span>
            <span class="account-value">{{ currentUser?.username }}</span>
          </div>
        </div>
      </div>

      <!-- 充值选项 -->
      <div class="recharge-options">
        <h2 class="section-title">选择充值金额</h2>
        
        <div class="amount-grid">
          <button 
            v-for="amount in amounts" 
            :key="amount"
            class="amount-btn"
            :class="{ active: selectedAmount === amount }"
            @click="selectAmount(amount)"
          >
            <span class="amount-symbol">¥</span>
            <span class="amount-value">{{ amount }}</span>
          </button>
          
          <!-- 自定义金额 -->
          <div class="custom-amount" :class="{ active: isCustomAmount }">
            <span class="input-symbol">¥</span>
            <input 
              type="number" 
              v-model.number="customAmount" 
              placeholder="自定义金额"
              @focus="selectCustom"
              @input="handleCustomInput"
              min="1"
            />
          </div>
        </div>

        <!-- 支付方式 (模拟) -->
        <h2 class="section-title">支付方式</h2>
        <div class="payment-methods">
          <div class="payment-method active">
            <div class="method-icon wechat">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M17.5 10c2.5 0 4.5-1.8 4.5-4s-2-4-4.5-4c-2.5 0-4.5 1.8-4.5 4 0 .4.1.9.2 1.3-1.6-.3-3.2.2-4.2 1.2-1.3 1.3-1.3 3.4 0 4.7.5.5 1.1.8 1.8 1 .1.4.1.8.1 1.2 0 2.5-2.2 4.5-5 4.5-.6 0-1.2-.1-1.7-.3-.4.3-.9.5-1.4.5-.3 0-.6-.1-.8-.2.1-.4.3-.8.6-1.1-.9-.8-1.4-1.9-1.4-3.1 0-2.5 2.2-4.5 5-4.5 1.6 0 3 .7 3.9 1.8.9-.6 2-1 3.1-1h.1c1.3-.6 2.7-1 4.2-1z" fill="currentColor"/>
              </svg>
            </div>
            <span class="method-name">微信支付</span>
            <div class="method-check">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <circle cx="12" cy="12" r="10" fill="#34C759"/>
                <path d="M8 12l3 3 5-5" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
          </div>
          <div class="payment-method disabled">
            <div class="method-icon alipay">
              <span style="font-weight: 800; font-size: 14px;">支</span>
            </div>
            <span class="method-name">支付宝</span>
          </div>
        </div>

        <div class="action-area">
          <button class="btn-submit" @click="submitRecharge" :disabled="loading || !isValidAmount">
            <span v-if="loading">处理中...</span>
            <span v-else>立即充值 ¥{{ currentAmount }}</span>
          </button>
          <p class="tip-text">点击"立即充值"即代表您已阅读并同意《充值服务协议》</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useUserStore } from '../stores/user'
import { storeToRefs } from 'pinia'
import { userAPI } from '../api'

const userStore = useUserStore()
const { currentUser } = storeToRefs(userStore)

const amounts = [50, 100, 200, 500, 1000]
const selectedAmount = ref(100)
const customAmount = ref('')
const isCustomAmount = ref(false)
const loading = ref(false)

const currentAmount = computed(() => {
  return isCustomAmount.value ? (customAmount.value || 0) : selectedAmount.value
})

const isValidAmount = computed(() => {
  const amount = currentAmount.value
  return typeof amount === 'number' && amount > 0
})

const selectAmount = (amount) => {
  selectedAmount.value = amount
  isCustomAmount.value = false
  customAmount.value = ''
}

const selectCustom = () => {
  isCustomAmount.value = true
  selectedAmount.value = null
}

const handleCustomInput = () => {
  if (!isCustomAmount.value) {
    isCustomAmount.value = true
    selectedAmount.value = null
  }
}

const submitRecharge = async () => {
  if (!isValidAmount.value) return
  
  try {
    loading.value = true
    await userAPI.recharge(currentUser.value.id, currentAmount.value)
    
    // 刷新用户信息
    try {
      const res = await userAPI.get(currentUser.value.id)
      userStore.setUser(res)
    } catch (e) {
      console.error('刷新余额失败', e)
    }
    
    loading.value = false
    alert(`成功充值 ¥${currentAmount.value}`)
    
    // 重置表单
    if (isCustomAmount.value) {
      customAmount.value = ''
      selectedAmount.value = 100
      isCustomAmount.value = false
    }
    
  } catch (error) {
    console.error('充值失败:', error)
    alert('充值失败，请稍后重试')
    loading.value = false
  }
}
</script>

<style scoped>
.recharge-page {
  max-width: 800px;
  margin: 0 auto;
}

.recharge-container {
  display: grid;
  gap: 24px;
}

/* 余额卡片 */
.balance-card {
  background: linear-gradient(135deg, #34C759 0%, #30B74F 100%);
  border-radius: 20px;
  padding: 32px;
  color: white;
  box-shadow: 0 10px 30px rgba(52, 199, 89, 0.3);
}

.balance-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  opacity: 0.9;
}

.balance-amount {
  font-size: 48px;
  font-weight: 700;
  margin-bottom: 24px;
  display: flex;
  align-items: baseline;
}

.balance-amount .symbol {
  font-size: 32px;
  margin-right: 8px;
}

.balance-footer {
  display: flex;
  gap: 24px;
  padding-top: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
}

.account-info {
  display: flex;
  gap: 8px;
  font-size: 14px;
}

.account-label {
  opacity: 0.8;
}

.account-value {
  font-weight: 600;
}

/* 充值选项 */
.recharge-options {
  background: white;
  border-radius: 20px;
  padding: 32px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  color: var(--color-text-primary);
}

.amount-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 32px;
}

.amount-btn {
  padding: 20px;
  border: 2px solid #eee;
  border-radius: 12px;
  background: white;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 4px;
}

.amount-btn:hover {
  border-color: #34C759;
  background: rgba(52, 199, 89, 0.05);
}

.amount-btn.active {
  border-color: #34C759;
  background: #34C759;
  color: white;
  box-shadow: 0 4px 12px rgba(52, 199, 89, 0.2);
}

.amount-symbol {
  font-size: 16px;
}

.amount-value {
  font-size: 24px;
  font-weight: 600;
}

.custom-amount {
  position: relative;
  border: 2px solid #eee;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.2s;
}

.custom-amount.active {
  border-color: #34C759;
}

.custom-amount .input-symbol {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 20px;
  color: #999;
}

.custom-amount input {
  width: 100%;
  height: 100%;
  border: none;
  padding: 0 16px 0 40px;
  font-size: 20px;
  font-weight: 600;
  outline: none;
}

/* 支付方式 */
.payment-methods {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 32px;
}

.payment-method {
  display: flex;
  align-items: center;
  padding: 16px;
  border: 2px solid #eee;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.payment-method.active {
  border-color: #34C759;
  background: rgba(52, 199, 89, 0.05);
}

.payment-method.disabled {
  opacity: 0.6;
  cursor: not-allowed;
  background: #f9f9f9;
}

.method-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
}

.method-icon.wechat {
  background: #09BB07;
  color: white;
}

.method-icon.alipay {
  background: #1677FF;
  color: white;
}

.method-name {
  flex: 1;
  font-weight: 500;
}

.method-check {
  width: 24px;
  height: 24px;
}

/* 提交按钮 */
.btn-submit {
  width: 100%;
  padding: 16px;
  background: linear-gradient(135deg, #34C759 0%, #30B74F 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 4px 12px rgba(52, 199, 89, 0.3);
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(52, 199, 89, 0.4);
}

.btn-submit:disabled {
  background: #ccc;
  cursor: not-allowed;
  box-shadow: none;
}

.tip-text {
  text-align: center;
  margin-top: 16px;
  font-size: 12px;
  color: #999;
}

@media (max-width: 768px) {
  .amount-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .payment-methods {
    grid-template-columns: 1fr;
  }
}
</style>
<template>
  <div class="page">
    <h1 class="page-title">充电资费设置</h1>

    <!-- 基础电价设置 -->
    <div class="card">
      <div class="card-header">
        <h2 class="card-title">基础电价设置</h2>
        <p class="card-desc">设置不同时段的充电电价</p>
      </div>
      
      <div class="settings-grid">
        <div class="setting-item">
          <div class="setting-header">
            <span class="setting-icon peak">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M13 2L3 14h8l-1 8 10-12h-8l1-8z" fill="currentColor" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <div>
              <div class="setting-label">峰时电价</div>
              <div class="setting-time">8:00 - 22:00</div>
            </div>
          </div>
          <div class="setting-control">
            <input 
              v-model.number="tariffConfig.peakPrice" 
              type="number" 
              step="0.01"
              class="input-field" 
            />
            <span class="unit">元/kWh</span>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-header">
            <span class="setting-icon valley">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" fill="currentColor"/>
              </svg>
            </span>
            <div>
              <div class="setting-label">谷时电价</div>
              <div class="setting-time">22:00 - 次日8:00</div>
            </div>
          </div>
          <div class="setting-control">
            <input 
              v-model.number="tariffConfig.valleyPrice" 
              type="number" 
              step="0.01"
              class="input-field" 
            />
            <span class="unit">元/kWh</span>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-header">
            <span class="setting-icon service">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M20 7h-4V4c0-1.1-.9-2-2-2h-4c-1.1 0-2 .9-2 2v3H4c-1.1 0-2 .9-2 2v11c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V9c0-1.1-.9-2-2-2zM10 4h4v3h-4V4zm10 16H4V9h16v11z" fill="currentColor"/>
                <circle cx="12" cy="15" r="2" fill="currentColor"/>
              </svg>
            </span>
            <div>
              <div class="setting-label">服务费</div>
              <div class="setting-time">固定费用</div>
            </div>
          </div>
          <div class="setting-control">
            <input 
              v-model.number="tariffConfig.serviceFee" 
              type="number" 
              step="0.01"
              class="input-field" 
            />
            <span class="unit">元/kWh</span>
          </div>
        </div>

        <div class="setting-item">
          <div class="setting-header">
            <span class="setting-icon parking">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect x="3" y="3" width="18" height="18" rx="2" fill="currentColor" opacity="0.2"/>
                <path d="M9 7h4.5c1.93 0 3.5 1.57 3.5 3.5S15.43 14 13.5 14H11v3H9V7zm2 2v3h2.5c.83 0 1.5-.67 1.5-1.5S14.33 9 13.5 9H11z" fill="currentColor"/>
              </svg>
            </span>
            <div>
              <div class="setting-label">停车费</div>
              <div class="setting-time">超时后收取</div>
            </div>
          </div>
          <div class="setting-control">
            <input 
              v-model.number="tariffConfig.parkingFee" 
              type="number" 
              step="0.1"
              class="input-field" 
            />
            <span class="unit">元/小时</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 充电规则设置 -->
    <div class="card">
      <div class="card-header">
        <h2 class="card-title">充电规则设置</h2>
        <p class="card-desc">设置充电时长、功率等限制规则</p>
      </div>

      <div class="settings-grid">
        <div class="setting-item">
          <div class="setting-label">单次充电时长限制</div>
          <div class="setting-control">
            <input 
              v-model.number="tariffConfig.maxChargingHours" 
              type="number" 
              step="0.5"
              class="input-field" 
            />
            <span class="unit">小时</span>
          </div>
          <div class="setting-hint">超时将自动停止充电</div>
        </div>

        <div class="setting-item">
          <div class="setting-label">免费停车时长</div>
          <div class="setting-control">
            <input 
              v-model.number="tariffConfig.freeParkingMinutes" 
              type="number" 
              step="5"
              class="input-field" 
            />
            <span class="unit">分钟</span>
          </div>
          <div class="setting-hint">充电结束后的免费停车时间</div>
        </div>

        <div class="setting-item">
          <div class="setting-label">最低充电金额</div>
          <div class="setting-control">
            <input 
              v-model.number="tariffConfig.minChargeFee" 
              type="number" 
              step="1"
              class="input-field" 
            />
            <span class="unit">元</span>
          </div>
          <div class="setting-hint">低于此金额将不收费</div>
        </div>

        <div class="setting-item">
          <div class="setting-label">最大充电功率</div>
          <div class="setting-control">
            <input 
              v-model.number="tariffConfig.maxPower" 
              type="number" 
              step="1"
              class="input-field" 
            />
            <span class="unit">kW</span>
          </div>
          <div class="setting-hint">充电桩最大输出功率</div>
        </div>
      </div>
    </div>

    <!-- 优惠策略 -->
    <div class="card">
      <div class="card-header">
        <h2 class="card-title">优惠策略</h2>
        <p class="card-desc">设置会员优惠和促销活动</p>
      </div>

      <div class="discount-list">
        <div 
          v-for="(discount, index) in tariffConfig.discounts" 
          :key="index"
          class="discount-item"
        >
          <div class="discount-info">
            <input 
              v-model="discount.name" 
              type="text" 
              placeholder="优惠名称"
              class="input-field discount-name"
            />
            <div class="discount-controls">
              <select v-model="discount.type" class="select-field">
                <option value="percentage">百分比折扣</option>
                <option value="fixed">固定金额优惠</option>
              </select>
              <input 
                v-model.number="discount.value" 
                type="number" 
                step="0.1"
                class="input-field discount-value"
                :placeholder="discount.type === 'percentage' ? '折扣 (%)' : '优惠 (元)'"
              />
              <label class="checkbox-label">
                <input type="checkbox" v-model="discount.enabled" />
                <span>启用</span>
              </label>
              <button 
                class="btn-icon btn-danger" 
                @click="removeDiscount(index)"
                title="删除"
              >
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 18px; height: 18px;">
                  <path d="M3 6h18M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  <line x1="10" y1="11" x2="10" y2="17" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                  <line x1="14" y1="11" x2="14" y2="17" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                </svg>
              </button>
            </div>
          </div>
        </div>

        <button class="btn-add" @click="addDiscount">
          + 添加优惠策略
        </button>
      </div>
    </div>

    <!-- 费用预览 -->
    <div class="card preview-card">
      <div class="card-header">
        <h2 class="card-title">费用预览</h2>
        <p class="card-desc">根据当前设置计算充电费用示例</p>
      </div>

      <div class="preview-examples">
        <div class="example-item">
          <div class="example-scenario">
            <div class="scenario-title">峰时充电 10kWh</div>
            <div class="scenario-detail">白天高峰期充电</div>
          </div>
          <div class="example-calculation">
            <div class="calc-row">
              <span>电费</span>
              <span>{{ (10 * tariffConfig.peakPrice).toFixed(2) }} 元</span>
            </div>
            <div class="calc-row">
              <span>服务费</span>
              <span>{{ (10 * tariffConfig.serviceFee).toFixed(2) }} 元</span>
            </div>
            <div class="calc-row total">
              <span>合计</span>
              <span>{{ (10 * (tariffConfig.peakPrice + tariffConfig.serviceFee)).toFixed(2) }} 元</span>
            </div>
          </div>
        </div>

        <div class="example-item">
          <div class="example-scenario">
            <div class="scenario-title">谷时充电 10kWh</div>
            <div class="scenario-detail">夜间低谷期充电</div>
          </div>
          <div class="example-calculation">
            <div class="calc-row">
              <span>电费</span>
              <span>{{ (10 * tariffConfig.valleyPrice).toFixed(2) }} 元</span>
            </div>
            <div class="calc-row">
              <span>服务费</span>
              <span>{{ (10 * tariffConfig.serviceFee).toFixed(2) }} 元</span>
            </div>
            <div class="calc-row total">
              <span>合计</span>
              <span>{{ (10 * (tariffConfig.valleyPrice + tariffConfig.serviceFee)).toFixed(2) }} 元</span>
            </div>
          </div>
        </div>

        <div class="example-item highlight">
          <div class="example-scenario">
            <div class="scenario-title">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 28px; height: 28px; display: inline-block; vertical-align: middle; margin-right: 8px;">
                <circle cx="12" cy="12" r="10" fill="white" opacity="0.3"/>
                <path d="M12 6v6l4 2" stroke="white" stroke-width="2" stroke-linecap="round"/>
                <path d="M8 14l2 2 4-4" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
              谷时充电优惠
            </div>
            <div class="scenario-detail">相比峰时可节省 {{ ((10 * (tariffConfig.peakPrice - tariffConfig.valleyPrice))).toFixed(2) }} 元</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="actions">
      <button class="btn-secondary" @click="resetSettings">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 20px; height: 20px;">
          <path d="M1 4v6h6M23 20v-6h-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M20.49 9A9 9 0 0 0 5.64 5.64L1 10m22 4l-4.64 4.36A9 9 0 0 1 3.51 15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        重置为默认
      </button>
      <button class="btn-primary" @click="saveSettings">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width: 20px; height: 20px;">
          <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          <polyline points="17 21 17 13 7 13 7 21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          <polyline points="7 3 7 8 15 8" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        保存设置
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { tariffAPI } from '../api'

// 资费配置数据
const tariffConfig = ref({
  id: null,
  peakPrice: 1.2,
  valleyPrice: 0.6,
  serviceFee: 0.8,
  parkingFee: 5.0,
  maxChargingHours: 4,
  freeParkingMinutes: 15,
  minChargeFee: 1.0,
  maxPower: 120,
  discounts: [
    { name: '会员折扣', type: 'percentage', value: 10, enabled: true },
    { name: '首次充电优惠', type: 'fixed', value: 5, enabled: true }
  ]
})

// 默认配置
const defaultConfig = {
  peakPrice: 1.2,
  valleyPrice: 0.6,
  serviceFee: 0.8,
  parkingFee: 5.0,
  maxChargingHours: 4,
  freeParkingMinutes: 15,
  minChargeFee: 1.0,
  maxPower: 120,
  discounts: [
    { name: '会员折扣', type: 'percentage', value: 10, enabled: true }
  ]
}

// 加载配置
const loadSettings = async () => {
  try {
    const res = await tariffAPI.getCurrent()
    if (res) {
      // 合并后端数据和前端默认数据（保留前端特有的字段）
      tariffConfig.value = {
        ...tariffConfig.value,
        ...res,
        // 确保数值类型正确
        peakPrice: Number(res.peakPrice),
        valleyPrice: Number(res.valleyPrice),
        serviceFee: Number(res.serviceFee),
        parkingFee: Number(res.parkingFee || 5.0)
      }
    }
  } catch (error) {
    console.error('加载配置失败:', error)
    // 如果加载失败，尝试从本地存储加载作为后备
    const saved = localStorage.getItem('tariffConfig')
    if (saved) {
      tariffConfig.value = JSON.parse(saved)
    }
  }
}

// 保存配置
const saveSettings = async () => {
  try {
    // 验证数据
    if (tariffConfig.value.peakPrice <= 0 || tariffConfig.value.valleyPrice <= 0) {
      alert('电价必须大于0')
      return
    }
    
    if (tariffConfig.value.peakPrice < tariffConfig.value.valleyPrice) {
      alert('峰时电价应该高于谷时电价')
      return
    }

    // 保存到后端
    await tariffAPI.update(tariffConfig.value)
    
    // 同时保存到本地作为缓存
    localStorage.setItem('tariffConfig', JSON.stringify(tariffConfig.value))
    
    alert('保存成功！')
  } catch (error) {
    console.error('保存失败:', error)
    alert('保存失败，请重试')
  }
}

// 重置为默认配置
const resetSettings = () => {
  if (confirm('确定要重置为默认配置吗？')) {
    tariffConfig.value = JSON.parse(JSON.stringify(defaultConfig))
    localStorage.removeItem('tariffConfig')
    alert('已重置为默认配置')
  }
}

// 添加优惠策略
const addDiscount = () => {
  tariffConfig.value.discounts.push({
    name: '',
    type: 'percentage',
    value: 0,
    enabled: false
  })
}

// 删除优惠策略
const removeDiscount = (index) => {
  if (confirm('确定要删除这个优惠策略吗？')) {
    tariffConfig.value.discounts.splice(index, 1)
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadSettings()
})
</script>

<style scoped>
.page {
  max-width: 1400px;
  animation: pageSlideIn 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  padding-bottom: var(--spacing-xl);
}

@keyframes pageSlideIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.page-title {
  font-size: 32px;
  font-weight: 800;
  margin-bottom: 28px;
  background: linear-gradient(135deg, #1d1d1f 0%, #007AFF 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -1px;
  position: relative;
  padding-bottom: 16px;
}

.page-title::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 60px;
  height: 4px;
  background: linear-gradient(90deg, #007AFF, #5856D6);
  border-radius: 2px;
}

.card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(40px) saturate(180%);
  -webkit-backdrop-filter: blur(40px) saturate(180%);
  border-radius: 20px;
  padding: 32px;
  border: 1px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.06), 
              0 2px 8px rgba(0, 0, 0, 0.04);
  margin-bottom: 24px;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #007AFF, #5856D6, #AF52DE);
  opacity: 0;
  transition: opacity 0.3s;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.1), 
              0 4px 16px rgba(0, 0, 0, 0.06);
}

.card:hover::before {
  opacity: 1;
}

.card-header {
  margin-bottom: 28px;
  padding-bottom: 20px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
  position: relative;
}

.card-title {
  font-size: 22px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0 0 8px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.card-title::before {
  content: '';
  width: 4px;
  height: 24px;
  background: linear-gradient(135deg, #007AFF, #5856D6);
  border-radius: 2px;
}

.card-desc {
  font-size: 14px;
  color: var(--color-text-secondary);
  margin: 0;
  line-height: 1.6;
}

.settings-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.setting-item {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 20px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 16px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid transparent;
}

.setting-item:hover {
  background: rgba(0, 122, 255, 0.04);
  border-color: rgba(0, 122, 255, 0.1);
  transform: translateY(-2px);
}

.setting-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 4px;
}

.setting-icon {
  width: 52px;
  height: 52px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 14px;
  flex-shrink: 0;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  color: white;
}

.setting-icon svg {
  width: 28px;
  height: 28px;
  display: block;
}

.setting-item:hover .setting-icon {
  transform: scale(1.1) rotate(-5deg);
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.18);
}

.setting-icon.peak {
  background: linear-gradient(135deg, #FF9500 0%, #FF6B00 100%);
}

.setting-icon.valley {
  background: linear-gradient(135deg, #5856D6 0%, #007AFF 100%);
}

.setting-icon.service {
  background: linear-gradient(135deg, #34C759 0%, #30D158 100%);
}

.setting-icon.parking {
  background: linear-gradient(135deg, #AF52DE 0%, #BF5AF2 100%);
}

.setting-label {
  font-size: 16px;
  font-weight: 700;
  color: var(--color-text-primary);
  letter-spacing: -0.3px;
}

.setting-time {
  font-size: 12px;
  color: var(--color-text-secondary);
  font-weight: 500;
}

.setting-control {
  display: flex;
  align-items: center;
  gap: 12px;
}

.input-field {
  flex: 1;
  padding: 14px 18px;
  border: 2px solid rgba(0, 0, 0, 0.08);
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: white;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  color: var(--color-text-primary);
}

.input-field:hover {
  border-color: rgba(0, 122, 255, 0.2);
}

.input-field:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 4px rgba(0, 122, 255, 0.12);
  transform: scale(1.02);
}

.unit {
  font-size: 14px;
  color: var(--color-text-secondary);
  min-width: 80px;
  font-weight: 600;
}

.setting-hint {
  font-size: 12px;
  color: var(--color-text-secondary);
  margin-top: 4px;
  padding-left: 4px;
  line-height: 1.5;
}

/* 优惠策略 */
.discount-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.discount-item {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.03) 0%, rgba(88, 86, 214, 0.03) 100%);
  border: 2px solid rgba(0, 122, 255, 0.1);
  border-radius: 16px;
  padding: 20px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.discount-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background: linear-gradient(135deg, #007AFF, #5856D6);
  opacity: 0;
  transition: opacity 0.3s;
}

.discount-item:hover {
  border-color: rgba(0, 122, 255, 0.3);
  transform: translateX(4px);
  box-shadow: 0 4px 16px rgba(0, 122, 255, 0.1);
}

.discount-item:hover::before {
  opacity: 1;
}

.discount-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.discount-name {
  font-weight: 700;
  font-size: 15px;
}

.discount-controls {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  align-items: center;
}

.select-field {
  padding: 12px 16px;
  border: 2px solid rgba(0, 0, 0, 0.08);
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
  background: white;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  min-width: 160px;
}

.select-field:hover {
  border-color: rgba(0, 122, 255, 0.2);
}

.select-field:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 4px rgba(0, 122, 255, 0.12);
}

.discount-value {
  max-width: 140px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  user-select: none;
  padding: 8px 16px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 10px;
  transition: all 0.2s;
}

.checkbox-label:hover {
  background: rgba(0, 122, 255, 0.08);
}

.checkbox-label input[type="checkbox"] {
  cursor: pointer;
  width: 20px;
  height: 20px;
  accent-color: var(--color-primary);
}

.btn-icon {
  padding: 10px 14px;
  border: none;
  border-radius: 12px;
  font-size: 18px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: none;
}

.btn-icon.btn-danger {
  background: rgba(255, 59, 48, 0.1);
}

.btn-icon.btn-danger:hover {
  background: rgba(255, 59, 48, 0.2);
  transform: scale(1.15) rotate(5deg);
}

.btn-add {
  padding: 16px 24px;
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.08) 0%, rgba(88, 86, 214, 0.08) 100%);
  color: var(--color-primary);
  border: 2px dashed rgba(0, 122, 255, 0.3);
  border-radius: 16px;
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  letter-spacing: 0.3px;
}

.btn-add:hover {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.15) 0%, rgba(88, 86, 214, 0.15) 100%);
  border-style: solid;
  border-color: var(--color-primary);
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 122, 255, 0.2);
}

/* 费用预览 */
.preview-card {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.08) 0%, rgba(88, 86, 214, 0.08) 100%);
  border: 2px solid rgba(0, 122, 255, 0.2);
  position: relative;
}

.preview-card::before {
  background: linear-gradient(90deg, #007AFF, #5856D6, #AF52DE, #34C759);
  opacity: 1;
}

.preview-examples {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.example-item {
  background: rgba(255, 255, 255, 0.95);
  border: 2px solid rgba(0, 0, 0, 0.06);
  border-radius: 16px;
  padding: 20px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.example-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  border-color: rgba(0, 122, 255, 0.2);
}

.example-item.highlight {
  background: linear-gradient(135deg, #34C759 0%, #30D158 100%);
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 24px rgba(52, 199, 89, 0.3);
}

.example-item.highlight:hover {
  transform: translateY(-4px) scale(1.02);
  box-shadow: 0 12px 32px rgba(52, 199, 89, 0.4);
}

.example-scenario {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.06);
}

.example-item.highlight .example-scenario {
  border-bottom: 2px solid rgba(255, 255, 255, 0.25);
  text-align: center;
}

.scenario-title {
  font-size: 16px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 6px;
  letter-spacing: -0.3px;
}

.example-item.highlight .scenario-title {
  color: white;
  font-size: 20px;
  font-weight: 800;
}

.scenario-detail {
  font-size: 13px;
  color: var(--color-text-secondary);
  line-height: 1.5;
}

.example-item.highlight .scenario-detail {
  color: rgba(255, 255, 255, 0.95);
  font-size: 15px;
  font-weight: 600;
}

.example-calculation {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.calc-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  color: var(--color-text-secondary);
  font-weight: 500;
}

.calc-row.total {
  padding-top: 12px;
  margin-top: 4px;
  border-top: 2px solid rgba(0, 0, 0, 0.1);
  font-weight: 800;
  font-size: 18px;
  color: var(--color-primary);
}

/* 操作按钮 */
.actions {
  display: flex;
  gap: 16px;
  justify-content: flex-end;
  margin-top: 32px;
}

.btn-primary {
  padding: 16px 36px;
  background: linear-gradient(135deg, #007AFF 0%, #5856D6 100%);
  color: white;
  border: none;
  border-radius: 14px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 16px rgba(0, 122, 255, 0.3);
  display: flex;
  align-items: center;
  gap: 10px;
  letter-spacing: 0.3px;
}

.btn-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(0, 122, 255, 0.4);
}

.btn-primary:active {
  transform: translateY(-1px);
}

.btn-secondary {
  padding: 16px 36px;
  background: rgba(0, 0, 0, 0.05);
  color: var(--color-text-primary);
  border: 2px solid rgba(0, 0, 0, 0.1);
  border-radius: 14px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  gap: 10px;
  letter-spacing: 0.3px;
}

.btn-secondary:hover {
  background: rgba(0, 0, 0, 0.08);
  border-color: var(--color-text-secondary);
  transform: translateY(-3px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.btn-secondary:active {
  transform: scale(0.97);
}

@media (max-width: 768px) {
  .settings-grid {
    grid-template-columns: 1fr;
  }

  .preview-examples {
    grid-template-columns: 1fr;
  }

  .discount-controls {
    flex-direction: column;
    align-items: stretch;
  }

  .discount-value {
    max-width: 100%;
  }

  .actions {
    flex-direction: column;
  }

  .actions button {
    width: 100%;
  }
}
</style>

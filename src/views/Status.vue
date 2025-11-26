<template>
  <div class="page">
    <h1 class="page-title">设备状态监控</h1>
    <div class="card">
      <p class="placeholder-text">实时监控所有充电桩设备状态</p>
    </div>
  </div>
</template>

<style scoped>
.page {
  max-width: 1400px;
  animation: fadeIn 0.4s ease-out;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: var(--spacing-lg);
  color: var(--color-text-primary);
  letter-spacing: -0.5px;
}

.card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-radius: var(--radius-lg);
  padding: var(--spacing-xl);
  border: 1px solid var(--color-separator);
  box-shadow: var(--shadow-sm);
}

.placeholder-text {
  color: var(--color-text-secondary);
  text-align: center;
  padding: var(--spacing-xl);
}
</style>

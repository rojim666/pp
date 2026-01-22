import { createPinia } from 'pinia'

export const pinia = createPinia()

// 导出所有store
export { useStationStore } from './station'
export { useRecordStore } from './record'
export { useUserStore } from './user'
export { useDashboardStore } from './dashboard'
export { useThemeStore } from './theme'

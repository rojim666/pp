import { defineStore } from 'pinia'
import { ref, watch } from 'vue'

export const useThemeStore = defineStore('theme', () => {
  // 主题模式: 'light' | 'dark' | 'auto'
  const mode = ref<'light' | 'dark' | 'auto'>('auto')
  
  // 实际使用的主题
  const actualTheme = ref<'light' | 'dark'>('light')
  
  // 是否自动跟随系统
  const followSystem = ref(true)

  // 初始化主题
  const initTheme = () => {
    const savedMode = localStorage.getItem('theme-mode') as 'light' | 'dark' | 'auto' | null
    if (savedMode) {
      mode.value = savedMode
    } else {
      mode.value = 'auto'
    }
    
    updateTheme()
    setupSystemThemeListener()
  }

  // 更新主题
  const updateTheme = () => {
    let newTheme: 'light' | 'dark' = 'light'
    
    if (mode.value === 'auto') {
      // 检查系统偏好
      if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
        newTheme = 'dark'
      }
    } else {
      newTheme = mode.value
    }
    
    actualTheme.value = newTheme
    applyTheme(newTheme)
  }

  // 应用主题到 DOM
  const applyTheme = (theme: 'light' | 'dark') => {
    const html = document.documentElement
    html.setAttribute('data-theme', theme)
    
    if (theme === 'dark') {
      html.classList.add('dark-mode')
      html.classList.remove('light-mode')
    } else {
      html.classList.add('light-mode')
      html.classList.remove('dark-mode')
    }
    
    // 确保 CSS 变量正确应用
    document.body.style.backgroundColor = ''
    document.documentElement.style.backgroundColor = ''
  }

  // 设置主题模式
  const setThemeMode = (newMode: 'light' | 'dark' | 'auto') => {
    mode.value = newMode
    localStorage.setItem('theme-mode', newMode)
    updateTheme()
  }

  // 切换主题
  const toggleTheme = () => {
    if (mode.value === 'auto') {
      setThemeMode(actualTheme.value === 'light' ? 'dark' : 'light')
    } else {
      setThemeMode(mode.value === 'light' ? 'dark' : 'light')
    }
  }

  // 设置跟随系统
  const setFollowSystem = (follow: boolean) => {
    followSystem.value = follow
    localStorage.setItem('theme-follow-system', follow.toString())
    if (follow) {
      setThemeMode('auto')
    }
  }

  // 监听系统主题变化
  const setupSystemThemeListener = () => {
    if (!window.matchMedia) return
    
    const darkModeQuery = window.matchMedia('(prefers-color-scheme: dark)')
    
    // 处理媒体查询变化
    const handleChange = (e: MediaQueryListEvent | MediaQueryList) => {
      if (mode.value === 'auto') {
        updateTheme()
      }
    }
    
    // 现代浏览器支持 addEventListener
    if (darkModeQuery.addEventListener) {
      darkModeQuery.addEventListener('change', handleChange)
    } else {
      // 旧版浏览器支持 addListener
      darkModeQuery.addListener(handleChange)
    }
  }

  // 监听主题变化，同步保存
  watch(mode, (newMode) => {
    localStorage.setItem('theme-mode', newMode)
    updateTheme()
  })

  return {
    mode,
    actualTheme,
    followSystem,
    initTheme,
    updateTheme,
    setThemeMode,
    toggleTheme,
    setFollowSystem
  }
})

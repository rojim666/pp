<template>
  <router-view />
  <Toast />
</template>

<script setup>
import { onMounted } from 'vue'
import { useThemeStore } from './stores/theme'
import Toast from './components/Toast.vue'

const themeStore = useThemeStore()

onMounted(() => {
  // 初始化主题
  themeStore.initTheme()
})
</script>

<style scoped>
</style>

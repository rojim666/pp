import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../components/Layout.vue'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { title: '注册', requiresAuth: false }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '充电桩记录' }
      },
      {
        path: '/settings',
        name: 'Settings',
        component: () => import('../views/Settings.vue'),
        meta: { title: '充电资费设置' }
      },
      {
        path: '/device',
        name: 'Device',
        component: () => import('../views/Device.vue'),
        meta: { title: '充电设备状态' }
      },
      {
        path: '/usage',
        name: 'Usage',
        component: () => import('../views/Usage.vue'),
        meta: { title: '充电使用统计' }
      },
      {
        path: '/charging',
        name: 'Charging',
        component: () => import('../views/Charging.vue'),
        meta: { title: '充电桩管理' }
      },
      {
        path: '/records',
        name: 'Records',
        component: () => import('../views/Records.vue'),
        meta: { title: '充电记录查看' }
      },
      {
        path: '/user',
        name: 'User',
        component: () => import('../views/User.vue'),
        meta: { title: '用户信息管理' }
      },
      {
        path: '/statistics',
        name: 'Statistics',
        component: () => import('../views/Statistics.vue'),
        meta: { title: '充电费用统计' }
      },
      {
        path: '/system',
        name: 'System',
        component: () => import('../views/System.vue'),
        meta: { title: '系统健康巡查' }
      },
      {
        path: '/notifications',
        name: 'Notifications',
        component: () => import('../views/Notifications.vue'),
        meta: { title: '通知中心' }
      },
      {
        path: '/profile',
        name: 'Profile',
        component: () => import('../views/Profile.vue'),
        meta: { title: '个人中心' }
      },
      {
        path: '/recharge',
        name: 'Recharge',
        component: () => import('../views/Recharge.vue'),
        meta: { title: '充值中心' }
      },
      {
        path: '/settings-system',
        name: 'SettingsPage',
        component: () => import('../views/SettingsPage.vue'),
        meta: { title: '系统设置' }
      },
      {
        path: '/settings-theme',
        name: 'SettingsTheme',
        component: () => import('../views/SettingsTheme.vue'),
        meta: { title: '主题与外观设置' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫 - 检查登录状态
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth !== false)
  
  if (requiresAuth && !userStore.isLoggedIn) {
    // 需要登录但未登录，跳转到登录页
    next({
      path: '/login',
      query: { redirect: to.fullPath }
    })
  } else if (to.path === '/login' && userStore.isLoggedIn) {
    // 已登录用户访问登录页，跳转到首页
    next('/dashboard')
  } else {
    next()
  }
})

export default router

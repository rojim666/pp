<template>
  <component :is="iconComponent" :class="['icon', size]" />
</template>

<script setup>
import { computed, defineComponent, h } from 'vue'

const props = defineProps({
  name: {
    type: String,
    required: true
  },
  size: {
    type: String,
    default: 'md' // sm, md, lg
  }
})

const icons = {
  // 设置图标
  settings: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M12 15C13.6569 15 15 13.6569 15 12C15 10.3431 13.6569 9 12 9C10.3431 9 9 10.3431 9 12C9 13.6569 10.3431 15 12 15Z',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        }),
        h('path', {
          d: 'M19.4 15C19.1277 15.6171 19.2583 16.3378 19.73 16.82L19.79 16.88C20.1656 17.2551 20.3766 17.7642 20.3766 18.295C20.3766 18.8258 20.1656 19.3349 19.79 19.71C19.4149 20.0856 18.9058 20.2966 18.375 20.2966C17.8442 20.2966 17.3351 20.0856 16.96 19.71L16.9 19.65C16.4178 19.1783 15.6971 19.0477 15.08 19.32C14.4755 19.5791 14.0826 20.1724 14.08 20.83V21C14.08 22.1046 13.1846 23 12.08 23C10.9754 23 10.08 22.1046 10.08 21V20.91C10.0642 20.2327 9.63587 19.6339 9 19.4C8.38291 19.1277 7.66219 19.2583 7.18 19.73L7.12 19.79C6.74486 20.1656 6.23582 20.3766 5.705 20.3766C5.17418 20.3766 4.66514 20.1656 4.29 19.79C3.91445 19.4149 3.70343 18.9058 3.70343 18.375C3.70343 17.8442 3.91445 17.3351 4.29 16.96L4.35 16.9C4.82167 16.4178 4.95235 15.6971 4.68 15.08C4.42093 14.4755 3.82764 14.0826 3.17 14.08H3C1.89543 14.08 1 13.1846 1 12.08C1 10.9754 1.89543 10.08 3 10.08H3.09C3.76733 10.0642 4.36613 9.63587 4.6 9C4.87235 8.38291 4.74167 7.66219 4.27 7.18L4.21 7.12C3.83445 6.74486 3.62343 6.23582 3.62343 5.705C3.62343 5.17418 3.83445 4.66514 4.21 4.29C4.58514 3.91445 5.09418 3.70343 5.625 3.70343C6.15582 3.70343 6.66486 3.91445 7.04 4.29L7.1 4.35C7.58219 4.82167 8.30291 4.95235 8.92 4.68H9C9.60447 4.42093 9.99738 3.82764 10 3.17V3C10 1.89543 10.8954 1 12 1C13.1046 1 14 1.89543 14 3V3.09C14.0026 3.74764 14.3955 4.34093 15 4.6C15.6171 4.87235 16.3378 4.74167 16.82 4.27L16.88 4.21C17.2551 3.83445 17.7642 3.62343 18.295 3.62343C18.8258 3.62343 19.3349 3.83445 19.71 4.21C20.0856 4.58514 20.2966 5.09418 20.2966 5.625C20.2966 6.15582 20.0856 6.66486 19.71 7.04L19.65 7.1C19.1783 7.58219 19.0477 8.30291 19.32 8.92V9C19.5791 9.60447 20.1724 9.99738 20.83 10H21C22.1046 10 23 10.8954 23 12C23 13.1046 22.1046 14 21 14H20.91C20.2524 14.0026 19.6591 14.3955 19.4 15Z',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        })
      ])
    }
  }),
  
  // 图表图标
  chart: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M18 20V10M12 20V4M6 20V14',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        })
      ])
    }
  }),
  
  // 文档图标
  document: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M14 2H6C5.46957 2 4.96086 2.21071 4.58579 2.58579C4.21071 2.96086 4 3.46957 4 4V20C4 20.5304 4.21071 21.0391 4.58579 21.4142C4.96086 21.7893 5.46957 22 6 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V8L14 2Z',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        }),
        h('path', {
          d: 'M14 2V8H20',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        })
      ])
    }
  }),
  
  // 充电插头图标
  plug: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M8 3V7M16 3V7M7 7H17C18.1046 7 19 7.89543 19 9V11C19 13.2091 17.2091 15 15 15H13V19C13 20.6569 11.6569 22 10 22H10C8.34315 22 7 20.6569 7 19V15H9C6.79086 15 5 13.2091 5 11V9C5 7.89543 5.89543 7 7 7Z',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        })
      ])
    }
  }),
  
  // 趋势图标
  trending: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M23 6L13.5 15.5L8.5 10.5L1 18',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        }),
        h('path', {
          d: 'M17 6H23V12',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        })
      ])
    }
  }),
  
  // 闪电图标
  zap: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M13 2L3 14H12L11 22L21 10H12L13 2Z',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round',
          fill: 'currentColor',
          opacity: '0.2'
        })
      ])
    }
  }),
  
  // 列表图标
  list: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M8 6H21M8 12H21M8 18H21M3 6H3.01M3 12H3.01M3 18H3.01',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        })
      ])
    }
  }),
  
  // 雷达图标
  radar: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('circle', {
          cx: '12',
          cy: '12',
          r: '10',
          stroke: 'currentColor',
          'stroke-width': '2'
        }),
        h('circle', {
          cx: '12',
          cy: '12',
          r: '6',
          stroke: 'currentColor',
          'stroke-width': '2'
        }),
        h('circle', {
          cx: '12',
          cy: '12',
          r: '2',
          fill: 'currentColor'
        }),
        h('path', {
          d: 'M12 2V12L18 18',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round'
        })
      ])
    }
  }),
  
  // 用户图标
  user: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M20 21V19C20 17.9391 19.5786 16.9217 18.8284 16.1716C18.0783 15.4214 17.0609 15 16 15H8C6.93913 15 5.92172 15.4214 5.17157 16.1716C4.42143 16.9217 4 17.9391 4 19V21',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        }),
        h('circle', {
          cx: '12',
          cy: '7',
          r: '4',
          stroke: 'currentColor',
          'stroke-width': '2'
        })
      ])
    }
  }),
  
  // 金钱图标
  money: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M12 1V23M17 5H9.5C8.57174 5 7.6815 5.36875 7.02513 6.02513C6.36875 6.6815 6 7.57174 6 8.5C6 9.42826 6.36875 10.3185 7.02513 10.9749C7.6815 11.6313 8.57174 12 9.5 12H14.5C15.4283 12 16.3185 12.3687 16.9749 13.0251C17.6313 13.6815 18 14.5717 18 15.5C18 16.4283 17.6313 17.3185 16.9749 17.9749C16.3185 18.6313 15.4283 19 14.5 19H6',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        })
      ])
    }
  }),
  
  // 搜索图标
  search: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('circle', {
          cx: '11',
          cy: '11',
          r: '8',
          stroke: 'currentColor',
          'stroke-width': '2'
        }),
        h('path', {
          d: 'M21 21L16.65 16.65',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round'
        })
      ])
    }
  }),
  
  // 通知图标
  bell: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M18 8C18 6.4087 17.3679 4.88258 16.2426 3.75736C15.1174 2.63214 13.5913 2 12 2C10.4087 2 8.88258 2.63214 7.75736 3.75736C6.63214 4.88258 6 6.4087 6 8C6 15 3 17 3 17H21C21 17 18 15 18 8Z',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        }),
        h('path', {
          d: 'M13.73 21C13.5542 21.3031 13.3019 21.5547 12.9982 21.7295C12.6946 21.9044 12.3504 21.9965 12 21.9965C11.6496 21.9965 11.3054 21.9044 11.0018 21.7295C10.6982 21.5547 10.4458 21.3031 10.27 21',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        })
      ])
    }
  }),
  
  // 主页图标
  home: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M3 9L12 2L21 9V20C21 20.5304 20.7893 21.0391 20.4142 21.4142C20.0391 21.7893 19.5304 22 19 22H5C4.46957 22 3.96086 21.7893 3.58579 21.4142C3.21071 21.0391 3 20.5304 3 20V9Z',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        }),
        h('path', {
          d: 'M9 22V12H15V22',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        })
      ])
    }
  }),
  
  // 菜单图标
  menu: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M3 12H21M3 6H21M3 18H21',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        })
      ])
    }
  }),
  
  // 下载图标
  download: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M21 15V19C21 19.5304 20.7893 21.0391 20.4142 21.4142C20.0391 21.7893 19.5304 22 19 22H5C4.46957 22 3.96086 21.7893 3.58579 21.4142C3.21071 21.0391 3 19.5304 3 19V15',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        }),
        h('path', {
          d: 'M7 10L12 15L17 10',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        }),
        h('path', {
          d: 'M12 15V3',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        })
      ])
    }
  }),
  
  // 保存图标
  save: defineComponent({
    render() {
      return h('svg', {
        viewBox: '0 0 24 24',
        fill: 'none',
        xmlns: 'http://www.w3.org/2000/svg'
      }, [
        h('path', {
          d: 'M19 21H5C4.46957 21 3.96086 20.7893 3.58579 20.4142C3.21071 20.0391 3 19.5304 3 19V5C3 4.46957 3.21071 3.96086 3.58579 3.58579C3.96086 3.21071 4.46957 3 5 3H16L21 8V19C21 19.5304 20.7893 20.0391 20.4142 20.4142C20.0391 20.7893 19.5304 21 19 21Z',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        }),
        h('path', {
          d: 'M17 21V13H7V21',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        }),
        h('path', {
          d: 'M7 3V8H15',
          stroke: 'currentColor',
          'stroke-width': '2',
          'stroke-linecap': 'round',
          'stroke-linejoin': 'round'
        })
      ])
    }
  })
}

const iconComponent = computed(() => icons[props.name])
</script>

<style scoped>
.icon {
  display: inline-block;
  width: 1em;
  height: 1em;
  vertical-align: middle;
}

.icon.sm {
  font-size: 16px;
}

.icon.md {
  font-size: 20px;
}

.icon.lg {
  font-size: 24px;
}
</style>

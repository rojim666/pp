import { reactive } from 'vue'

const state = reactive({
  toasts: []
})

let idCounter = 0

export const useToast = () => {
  const remove = (id) => {
    const index = state.toasts.findIndex(t => t.id === id)
    if (index > -1) {
      state.toasts.splice(index, 1)
    }
  }

  const add = (message, type = 'info', duration = 3000) => {
    const id = idCounter++
    const toast = { id, message, type }
    state.toasts.push(toast)
    
    if (duration > 0) {
      setTimeout(() => {
        remove(id)
      }, duration)
    }
  }

  const success = (message, duration) => add(message, 'success', duration)
  const error = (message, duration) => add(message, 'error', duration)
  const info = (message, duration) => add(message, 'info', duration)
  const warning = (message, duration) => add(message, 'warning', duration)

  return {
    toasts: state.toasts,
    add,
    remove,
    success,
    error,
    info,
    warning
  }
}

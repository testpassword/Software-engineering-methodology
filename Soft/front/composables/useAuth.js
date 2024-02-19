import { useStorage } from '@vueuse/core'

const secret = useStorage('secret', null)
const userId = useStorage('userId', null)

export const useAuth = () => ({
  userId, // REMEMBER THAT IS STRING!!!
  getRaw: () => secret.value,
  get: () => {
    if (!secret.value) return null
    const decoded = atob(secret.value).split(' ').lastItem.split(':')
    return { email: decoded[0], password: decoded[1] }
  },
  set: ({ email, password }) => secret.value = `Basic ${btoa(`${email}:${password}`)}`,
  logout: () => secret.value = null
})

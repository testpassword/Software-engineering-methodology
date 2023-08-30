import { useStorage } from '@vueuse/core'

const secret = useStorage('secret', null)

export const useAuth = () => ({
  getRaw: () => secret.value,
  get: () => !secret.value ? null : JSON.parse(atob(secret.value.split(' ').lastItem)),
  set: ({ id, phone, password }) => secret.value = `Basic ${btoa(JSON.stringify({ id, phone, password }))}`,
  logout: () => secret.value = null
})

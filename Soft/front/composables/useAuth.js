import { useStorage } from '@vueuse/core'

const secret = useStorage('secret', null)

export const useAuth = () => ({
  getRaw: () => secret.value,
  get: () => !secret.value ? null : JSON.parse(atob(secret.value)),
  set: ({ phone, password }) => { secret.value = btoa(JSON.stringify({ phone, password })) }
})

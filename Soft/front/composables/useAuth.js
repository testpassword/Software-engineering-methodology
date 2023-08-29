import { useStorage } from '@vueuse/core'

const secret = useStorage('secret', null)
// todo: do not use phone as user id

export const useAuth = () => ({
  getRaw: () => secret.value,
  get: () => !secret.value ? null : JSON.parse(atob(secret.value.split(' ').lastItem)),
  set: ({ id, phone, password }) => secret.value = `Basic ${btoa(JSON.stringify({ id, phone, password }))}`
})

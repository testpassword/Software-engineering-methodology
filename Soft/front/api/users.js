import { useApi } from '/composables/useApi'
import basicInterface from './basic'
import arrows from './arrows'

const ENDPOINT = '/users/'

export default function users() {
  return {
    ENDPOINT,

    ...basicInterface(ENDPOINT),

    login: async ({ phone, password }) =>
      await useApi(`${ENDPOINT}login/`, { body: { phone, password }, method: 'POST' }),

    register: async ({ phone, email, password }) =>
      await useApi(`${ENDPOINT}`, { body: { phone, email, password }, method: 'POST' }),

    for: id => ({ arrows: arrows(`${ENDPOINT}${id ?? useAuth().get().phone}/`) })
  }
}

import { useApi } from '/composables/useApi'
import basicInterface from './basic'

export default function users() {
  const ENDPOINT = '/users/'

  return {
    ENDPOINT,

    ...basicInterface(ENDPOINT, ['update']),

    register: async ({ phone, email, password }) =>
      await useApi(ENDPOINT, { body: { phone, email, password }, method: 'POST' }),

    for: id => {
      const userEndpoint = `${ENDPOINT}${id ?? useAuth().get().phone}/`
      return {
        ENDPOINT: userEndpoint,
        ...basicInterface(userEndpoint)
      }
    }
  }
}

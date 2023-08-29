import { useApi } from '/composables/useApi'
import basicInterface from './basic'
import { userArrows } from './arrows'
import idEndpointGetter from './idEndpointGetter'

export default function users() {
  const ENDPOINT = '/users/'

  return idEndpointGetter({
    ENDPOINT,

    ...basicInterface(ENDPOINT, ['update', 'del']),

    register: async ({ phone, email, password }) =>
      await useApi(ENDPOINT, { body: { phone, email, password }, method: 'POST' }),

    for: id => {
      const userEndpoint = `${ENDPOINT}${id ?? useAuth().get().phone}/`
      return {
        ENDPOINT: userEndpoint,
        ...basicInterface(userEndpoint),
        arrows: userArrows(userEndpoint)
      }
    }
  })
}

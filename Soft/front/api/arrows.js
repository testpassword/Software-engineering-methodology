import basicInterface from './basic'
import {useApi} from '../composables/useApi'

export default function arrows(parentEndpoint) {

  const ENDPOINT = `${parentEndpoint}arrows/`

  return {
    ...basicInterface(ENDPOINT),

    push: async () => await useApi(ENDPOINT, { method: 'PUT' }),

    buy: async amount => await useApi(ENDPOINT, { body: { amount }, method: 'POST' }),

    price: async () => await useApi(`${ENDPOINT}price/`)
  }
}

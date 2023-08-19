import { useApi } from '/composables/useApi'

export default function session() {
  const ENDPOINT = '/session/'

  return {
    ENDPOINT,

    login: async ({ phone, password }) =>
      await useApi(ENDPOINT, { body: { phone, password }, method: 'POST' }),
  }
}

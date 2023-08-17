import { useApi } from '/composables/useApi'

export default function session() {
  const ENDPOINT = '/session/'

  return {
    ENDPOINT,

    login: async ({ phone, password }) =>
      await useApi(`${ENDPOINT}login/`, { body: { phone, password }, method: 'POST' }),
  }
}

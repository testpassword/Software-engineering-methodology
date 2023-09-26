import { useApi } from '/composables/useApi'

export default function session() {
  const ENDPOINT = '/session/'

  return {
    ENDPOINT,

    login: async ({ email, password }) =>
      await useApi(ENDPOINT, { body: { email, password }, method: 'POST' }),
  }
}

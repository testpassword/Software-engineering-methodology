import { useApi } from '/composables/useApi'

export default function basicInterface(ENDPOINT) {
  return {
    get: async (id = null) => await useApi(`${ENDPOINT}/${id ?? ''}/`)
  }
}

import { useApi } from '/composables/useApi'

export default function basicInterface(ENDPOINT, exclude = []) {
  const ided = (id = null) => `${ENDPOINT}/${id ?? ''}/`
  const get = async (id = null) => await useApi(ided(id))
  const del = async id => await useApi(ided(id), { method: 'DELETE' })
  const update = async diff => await useApi(ENDPOINT, { body: diff, method: 'PATCH' })
  const DEFAULT_FUNCS = { ENDPOINT, get, del, update }
  return exclude.reduce(
    (acc, cur) => {
      delete acc[cur]
      return acc
    },
    { ...DEFAULT_FUNCS }
  )
}

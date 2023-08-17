import { useApi } from '/composables/useApi'

export default function basicInterface(ENDPOINT, exclude = []) {
  const get = async (id = null) => await useApi(`${ENDPOINT}/${id ?? ''}/`)
  const del = async id => useApi(`${ENDPOINT}${id}/`)
  const update = async diff => await useApi(ENDPOINT, { body: diff.unvoid(), method: 'PATCH' })
  const DEFAULT_FUNCS = { ENDPOINT, get, del, update }
  return exclude.reduce(
    (acc, cur) => {
      delete acc[cur]
      return acc
    },
    { ...DEFAULT_FUNCS }
  )
}

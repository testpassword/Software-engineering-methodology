export const useApi = async (request, opts) => {
  if (typeof request === 'string') {
    request = request.replace(/\/+/g, '/')
    if (!request.endsWith('/')) request = request + '/'
  }
  const resp = await useFetch(request, { baseURL: useRuntimeConfig().public.urlApi, ...opts })
  const err = resp.error.value
  if (err) {
    const e = JSON.stringify(err)
    alert(e)
    throw Error(e)
  }
  return resp.data.value
}

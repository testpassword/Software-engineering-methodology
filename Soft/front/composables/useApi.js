import { useAuth } from './useAuth'
import { useWebNotification } from '@vueuse/core/index'

export const useApi = async (request, opts = {}) => {
  if (typeof request === 'string') {
    request = request.replace(/\/+/g, '/')
    if (!request.endsWith('/')) request = request + '/'
  }
  const creds = useAuth().getRaw()
  if (creds) opts.headers = {...opts?.headers ?? {}, 'Authorization': creds}
  const resp = await useFetch(
    request,
    {
      baseURL: useRuntimeConfig().public.urlApi,
      server: false,
      ...opts
    }
  )
  const err = resp.error.value
  if (err) {
    const e = JSON.stringify(err)
    await useWebNotification({ title: 'Error', body: e }).show()
    throw Error(e)
  }
  return resp.data.value
}

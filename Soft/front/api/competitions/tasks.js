import basicInterface from '../basic'
import { useApi } from '/composables/useApi'
import idEndpointGetter from '../idEndpointGetter'

export default function tasks(parentEndpoint = '') {
  const ENDPOINT = `/${parentEndpoint}tasks/`

  return idEndpointGetter({
    ...basicInterface(ENDPOINT, ['del', 'update']),

    create: async({ text, executorId }) => await useApi(ENDPOINT, { body: { text, executorId }, method: 'POST' }),

    for: id => {
      const taskEndpoint = `${ENDPOINT}${id}/`
      return {
        ENDPOINT: taskEndpoint,
        ...basicInterface(taskEndpoint, ['del'])
      }
    }
  })
}

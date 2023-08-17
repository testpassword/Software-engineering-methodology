import basicInterface from '../basic'
import { useApi } from '/composables/useApi'

export default function tasks(parentEndpoint = '') {
  const ENDPOINT = `/${parentEndpoint}tasks/`

  return {
    ...basicInterface(ENDPOINT, ['del', 'update']),

    create: async({ text, executor }) => await useApi(ENDPOINT, { body: { text, executor }, method: 'POST' }),

    for: id => {
      const taskEndpoint = `${ENDPOINT}${id}/`
      return {
        ENDPOINT: taskEndpoint,
        ...basicInterface(taskEndpoint, ['del'])
      }
    }
  }
}

import basicInterface from '../basic'
import { useApi } from '/composables/useApi'

export default function brideVote(parentEndpoint = '') {
  const ENDPOINT = `${parentEndpoint}bride_vote`

  const create = async({ candidates }) => await useApi(ENDPOINT, { body: { candidates }, method: 'POST' })

  return {
    ENDPOINT,
    ...basicInterface(ENDPOINT, ['del']),
    create
  }
}

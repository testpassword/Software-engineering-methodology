import basicInterface from '../basic'
import { useApi } from '/composables/useApi'
import tasks from './tasks'
import marriage from './marriage'
import members from './members'
import brideVote from './brideVote'

export default function competitions(parentEndpoint = '') {
  const ENDPOINT = `/${parentEndpoint}competitions/`

  return {
    ENDPOINT,

    ...basicInterface(ENDPOINT, ['del', 'update']),

    create: async ({ name, city }) => await useApi(ENDPOINT, { body: { name, city }, method: 'POST' }),

    for: id => {
      const competitionEndpoint = `${ENDPOINT}${id}/`
      return {
        ENDPOINT: competitionEndpoint,
        ...basicInterface(competitionEndpoint, ['del']),
        tasks: tasks(competitionEndpoint),
        members: members(competitionEndpoint),
        brideVote: brideVote(competitionEndpoint),
        marriage: marriage(competitionEndpoint)
      }
    }
  }
}
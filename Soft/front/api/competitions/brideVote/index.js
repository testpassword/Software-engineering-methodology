import basicInterface from '/api/basic'
import candidates from './candidates'
import vote from './vote'
import idEndpointGetter from '../../idEndpointGetter'

export default function brideVote(parentEndpoint = '') {
  const ENDPOINT = `${parentEndpoint}bride_vote/`

  return idEndpointGetter({
    ENDPOINT,
    ...basicInterface(ENDPOINT, ['del', 'update']),
    candidates: candidates(ENDPOINT),
    for: id => {
      const brideVoteIncEndpoint = `${ENDPOINT}${id}/`
      return {
        ENDPOINT: brideVoteIncEndpoint,
        vote: vote(brideVoteIncEndpoint)
      }
    }
  })
}

import basicInterface from '/api/basic'
import candidates from './candidates'
import vote from './vote'

export default function brideVote(parentEndpoint = '') {
  const ENDPOINT = `${parentEndpoint}bride_vote/`

  return {
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
  }
}

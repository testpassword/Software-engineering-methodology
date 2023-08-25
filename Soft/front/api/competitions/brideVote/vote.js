export default function vote(parentEndpoint = '') {
  const ENDPOINT = `/${parentEndpoint}vote/`

  return {
    inc: async () => await useApi(ENDPOINT, { method: 'POST' }),
  }
}

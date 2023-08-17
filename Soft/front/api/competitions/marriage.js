import basicInterface from '../basic'

export default function marriage(parentEndpoint = '') {
  const ENDPOINT = `${parentEndpoint}marriage`

  return {
    ENDPOINT,
    ...basicInterface(ENDPOINT, ['del'])
  }
}

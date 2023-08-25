import basicInterface from '../../basic'

export default function candidates(parentEndpoint = '') {
  const ENDPOINT = `/${parentEndpoint}candidates/`

  return { ...basicInterface(ENDPOINT, ['del', 'update']) }
}

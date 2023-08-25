import basicInterface from '../basic'

export default function members(parentEndpoint = '') {
  const ENDPOINT = `/${parentEndpoint}members/`

  return {
    ...basicInterface(ENDPOINT, ['del', 'update'])
  }
}

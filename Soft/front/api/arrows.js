import basicInterface from './basic'

export function arrows(parentEndpoint = '') {
  const ENDPOINT = `${parentEndpoint}arrows/`
  const priceEndpoint = `${ENDPOINT}price/`
  return {
    ENDPOINT,
    price: {
      ENDPOINT: priceEndpoint,
      ...basicInterface(priceEndpoint, ['del', 'update'])
    }
  }
}

export function userArrows(parentEndpoint = '') {
  const ENDPOINT = `${parentEndpoint}arrows/`
  return {
    ENDPOINT,
    ...basicInterface(ENDPOINT, ['del', 'update']),
    buy: async amount => await useApi(ENDPOINT, { body: { amount }, method: 'POST' })
  }
}

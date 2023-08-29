export default function idEndpointGetter(parentApi) {
  return new Proxy(
    parentApi,
    { get: (target, field)  => isNaN(field) ? target[field] : target.for(field) }
  )
}

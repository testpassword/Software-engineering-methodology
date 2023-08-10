export default defineNuxtRouteMiddleware(to => {
  const urn = useRoles().userRole.value
  if (
    useRuntimeConfig().public.environment !== 'DEV' &&
    urn !== to.path.split('/').lastItem
  ) return navigateTo(`/rooms/${urn}`)
})

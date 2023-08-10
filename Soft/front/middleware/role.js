export default defineNuxtRouteMiddleware(to => {
  const urn = useRoles().userRole.value
  if (useRuntimeConfig().public.environment !== 'DEV')
    if (!urn && useAuth().get()) return navigateTo('/account-settings')
    else return navigateTo(`/rooms/${urn}`)
})

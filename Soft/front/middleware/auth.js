export default defineNuxtRouteMiddleware(to => {
  if (
    useRuntimeConfig().public.environment !== 'DEV' &&
    !useAuth().get() ||
    !useRoles().userRole
  ) return navigateTo(`/login`)
})

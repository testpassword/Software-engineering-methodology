export default defineNuxtRouteMiddleware(to => {
  if (
    useRuntimeConfig().public.environment !== 'DEV' &&
    !useAuth().get()
  ) return navigateTo(`/login`)
})

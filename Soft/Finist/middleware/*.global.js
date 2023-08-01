const { userRole, ROLES } = useRoles()

export default defineNuxtRouteMiddleware(to => {
  if (
    useRuntimeConfig().public?.environment !== 'DEV' &&
    !ROLES.find( it => it.name === userRole.value )?.allowedPages.includes(to?.path)
  ) return abortNavigation()
})

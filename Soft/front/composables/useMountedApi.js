export const useMountedApi = f =>
  onMounted(
    async () =>
      await nextTick(async () => await f()
      )
  )

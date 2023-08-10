export default defineNuxtConfig({
  modules: ['@nuxtjs/tailwindcss'],
  runtimeConfig: {
    public: {
      urlApi: process.env.NUXT_URL_API,
      environment: process.env.NUXT_ENVIRONMENT
    }
  }
})

<script setup>
const { isBlurred, init } = use3DBackground()
init()
</script>

<template>
  <div>
    <canvas
      id="background"
      class="w-screen h-screen absolute -z-10 transition-all duration-1000"
      :class="{ 'blur-md': isBlurred }"
    />
    <NuxtPage
      class="p-6"
      @click="isBlurred = true"
    />
  </div>
</template>

<style lang="sass">
@import url('https://fonts.googleapis.com/css2?family=Montserrat+Alternates&display=swap')

*
  font-family: 'Montserrat Alternates', sans-serif

a
  @apply text-red-600
  &:hover
    @apply text-red-400 underline decoration-red-400

h1, h2, h3, h4, span, input, p, label, option, .default-icon
  @apply text-grey-800

h1
  @apply text-4xl

h2
  @apply text-3xl

h3
  @apply text-xl

h1, h2, h3
  @apply font-bold

input::placeholder
  @apply text-grey-500

button.btn-modal-close
  @apply btn btn-sm btn-circle btn-ghost absolute right-2 top-2 hover:bg-red-600 hover:text-green-100

.spacer
  @apply flex flex-grow
</style>

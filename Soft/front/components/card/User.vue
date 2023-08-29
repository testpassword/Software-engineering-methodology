<script setup>
const props = defineProps({
  user:       { type: Object,  required: true },
  horizontal: { type: Boolean, required: false, default: false }
})

const { user } = toRefs(props)
</script>

<template>
  <div class="card glass flex flex-row gap-4 py-3 px-6">
    <div class="flex items-center">
      <img
        class="avatar rounded-full h-24"
        src="/avatar-placeholder.webp"
        alt="avatar"
      />
    </div>
    <div
      class="flex info gap-0.5"
      :class="[ horizontal ? 'flex-row flex-wrap items-center' : 'flex-col' ]"
    >
      <h3 class="pb-2 pr-2">{{ user.name }}</h3>
      <div>
        <IconAge/>
        <span>{{ user.age }}</span>
      </div>
      <div>
        <IconCity/>
        <span>{{ user.city }}</span>
      </div>
      <div>
        <IconEducation/>
        <span>{{ user.education }}</span>
      </div>
      <div>
        <IconMask/>
        <span>{{ user.role }}</span>
      </div>
    </div>
    <slot name="default"/>
  </div>
</template>

<style lang="sass" scoped>
.info div
  @apply flex gap-3 capitalize
  & .ico
    @apply text-red-600/75
</style>

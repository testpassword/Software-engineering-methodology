<script setup>
const props = defineProps({
  item:       { type: Object,  required: true },
  horizontal: { type: Boolean, required: false, default: false }
})

const { item } = toRefs(props)

function age(birthday) {
  const today = new Date()
  const birthDate = new Date(birthday)
  let age = today.getFullYear() - birthDate.getFullYear()
  if (today.getMonth() < birthDate.getMonth()) age--
  if (today.getDay() < birthDate.getDay()) age--
  return age
}
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
      <h3 class="pb-2 pr-2">{{ item.name }}</h3>
      <div>
        <IconAge/>
        <span>{{ age(item.dateOfBirth) }}</span>
      </div>
      <div>
        <IconCity/>
        <span>{{ item.city }}</span>
      </div>
      <div>
        <IconEducation/>
        <span>{{ item.education }}</span>
      </div>
      <div>
        <IconMask/>
        <span>{{ item.role }}</span>
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

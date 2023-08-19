<script setup>
const props = defineProps({
  competition: { type: Object, required: true }
})

const competition = toRef(props, 'competition')

const memberDial = ref()
const showedMember = ref({})

// todo: replace progress to daisyUI 'Steps' component

const showMember = member => {
  showedMember.value = member
  memberDial.value.dialog.showModal()
}
</script>

<template>
  <div class="card glass flex flex-row gap-8 py-3 px-6 capitalize">
    <div class="info">
      <h3>{{ competition.name }}</h3>
      <div class="flex flex-grow w-full">
        <IconCity/>
        <span>{{ competition.city }}</span>
        <div class="spacer"/>
        <Chip>
          {{ competition.progress * 100 }}%
        </Chip>
      </div>
    </div>
    <div class="flex flex-row">
      <div
        class="tooltip -ml-2 tooltip-secondary"
        v-for="m in competition.members"
        :data-tip="m.name"
        @click="showMember(m)"
      >
        <img
          class="avatar rounded-full h-12"
          src="/avatar-placeholder.webp"
          alt="avatar"
        />
      </div>
    </div>
    <slot name="default"/>
    <AcceptDialog
      ref="memberDial"
      hide-accept
    >
      <template #content>
        <CardUser :user="showedMember"/>
      </template>
    </AcceptDialog>
  </div>
</template>

<style lang="sass" scoped>
.info div
  @apply flex gap-3 capitalize
  & .ico
    @apply text-red-600/75
</style>

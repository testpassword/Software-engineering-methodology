<script setup>
import api from '/api'

const { STATUSES } = useCompetitionsStatues()
const props = defineProps({
  competition: { type: Object, required: true }
})

const { competition } = toRefs(props)
const members = ref([])
const progress = computed(() => STATUSES.findIndex(it => it.name === competition.value.status))

await useMountedApi(async () => members.value = await api.competitions.for(competition.value.id).members.get())

const memberDial = ref()
const showedMember = ref({})

const showMember = member => {
  showedMember.value = member
  memberDial.value.dialog.showModal()
}
</script>

<template>
  <div class="flex flex-col card glass">
    <div class="flex flex-row gap-8 py-3 px-6 capitalize">
      <div class="info">
        <h3>{{ competition.name }}</h3>
        <div class="flex flex-grow w-full">
          <IconCity/>
          <span>{{ competition.city }}</span>
        </div>
      </div>
      <div class="spacer"/>
      <div class="flex flex-row">
        <div
          class="tooltip -ml-2 tooltip-secondary"
          v-for="m in members"
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
    <ul class="steps">
      <li
        class="step text-[8px] text-black-100"
        v-for="(it, i) in STATUSES"
        :class="{ 'step-primary text-primary': progress >= i }"
      >
        {{ it.label }}
      </li>
    </ul>
  </div>
</template>

<style lang="sass" scoped>
.info div
  @apply flex gap-3 capitalize
  & .ico
    @apply text-red-600/75
</style>

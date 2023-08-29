<script setup>
import api from '/api'

const { STATUSES } = useCompetitionsStatues()
const props = defineProps({
  competition: { type: Object, required: true }
})

const { competition } = toRefs(props)
const members = ref([])
const tasks = ref([])
const progress = computed(() => STATUSES.findIndex(it => it.name === competition.value.status))

await useMountedApi(async () => {
  const comApi = api.competitions[competition.value.id]
  members.value = await comApi.members.get()
  try { tasks.value = await comApi.tasks.get() } catch { /* ignore if not exist */ }
})

const memberDial = ref()
const showedMember = ref({})

const showMember = member => {
  showedMember.value = member
  memberDial.value.dialog.showModal()
}

const getTaskByUserId = userId => tasks.value.find( it => it.executorId === userId )

const bvDial = ref()
const tomorrow = new Date(); tomorrow.setDate(new Date().getDate() + 1)
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

          <!-- todo: id normal in getTaskByUserId() -->

          <img
            :class="{ 'border-primary border-2': getTaskByUserId(1)?.completed }"
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
        ultrawide
      >
        <template #content>
          <CardUser :user="showedMember">
            <CardTask
              class="max-w-[350px]"
              :com-id="competition.id"
              :task="getTaskByUserId(1)"
            />
          </CardUser>
        </template>
      </AcceptDialog>
    </div>
    <ul class="steps">
      <li
        class="step text-[8px] text-black-100"
        v-for="(it, i) in STATUSES"
        @click="bvDial.dialog.showModal"
        :class="{
          'step-primary text-primary': progress >= i,
          'animate-pulse': competition.status === it.name
        }"
      >
        {{ it.label }}
      </li>
    </ul>
    <AcceptDialog
      ultrawide
      ref="bvDial"
      hide-accept
    >
      <template #title>
        {{
          {
            IN_PROGRESS: 'Ваше задание',
            VOTING: 'Кандидатки'
          }[competition.status]
        }}
      </template>
      <template #content>
        <CardTask
          :com-id="competition.id"
          :task="getTaskByUserId(1)"
          is-executor
        />
        <CardBrideVote
          v-if="competition.status === 'VOTING'"
          :com-id="competition.id"
        />
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

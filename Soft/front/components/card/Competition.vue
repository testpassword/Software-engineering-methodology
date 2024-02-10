<script setup>
import api from '/api'
import Commentary from '../icon/Commentary.vue'

const { STATUSES } = useCompetitionsStatues()
const props = defineProps({
  item: { type: Object, required: true },
  hideActions: { type: Boolean, required: false, default: false },
})

const { item } = toRefs(props)
const members = ref([])
const tasks = ref([])
const progress = computed(() => STATUSES.findIndex(it => it.name === item.value.status))

await useMountedApi(async () => {
  const comApi = api.competitions[item.value.id]
  members.value = await comApi.members.get()
  try { tasks.value = await comApi.tasks.get() } catch { /* ignore if not exist */ }
})

const memberDial = ref()
const showedMember = ref({})

const getTaskByUserId = userId => tasks.value.find( it => it.executorId === userId )

const bvDial = ref()
const tomorrow = new Date(); tomorrow.setDate(new Date().getDate() + 1)

const commentsDial = ref()

const follow = () => alert('Вы подписались!')
</script>

<template>
  <div class="flex flex-col card glass competitionCard">
    <div class="flex flex-row gap-8 py-3 px-6 capitalize">
      <div class="info">
        <h3>{{ item.name }}</h3>
        <div class="flex flex-grow w-full">
          <IconCity/>
          <span>{{ item.city }}</span>
        </div>
      </div>
      <div class="spacer"/>
      <div class="flex flex-row">
        <div
          class="tooltip -ml-2 tooltip-secondary moreAboutUserBtn"
          v-for="m in members"
          :data-tip="m.name"
          @click="() => {
            showedMember = m
            memberDial.dialog.showModal()
          }"
        >
          <img
            :class="{ 'border-primary border-2': getTaskByUserId(m.id)?.completed }"
            class="avatar rounded-full h-12"
            src="/avatar-placeholder.webp"
            alt="avatar"
          />
        </div>
      </div>
      <slot name="default"/>
      <LazyDialogAccept
        ref="memberDial"
        hide-accept
        ultrawide
      >
        <template #content>
          <CardUser :item="showedMember">
            <CardTask
              class="max-w-[350px]"
              :com-id="item.id"
              :task="getTaskByUserId(showedMember.id)"
            />
          </CardUser>
        </template>
      </LazyDialogAccept>
    </div>
    <ul class="steps">
      <li
        class="step text-[8px] text-black-100"
        v-for="(it, i) in STATUSES"
        @click="bvDial.dialog.showModal"
        :class="{
          'step-primary text-primary': progress >= i,
          'animate-pulse': item.status === it.name
        }"
      >
        {{ it.label }}
      </li>
    </ul>
    <div class="m-3">
      <slot
        name="actions"
        v-if="!hideActions"
      >
        <div class="flex flex-row gap-3">
          <button
            class="btn text-purple-500 btn-outline w-fit hover:bg-purple-500 btn-sm followCompetitionRooms"
            @click="follow"
          >
            <IconFollow/>
            Подписаться
          </button>
          <button
            class="btn btn-outline text-purple-600 hover:bg-purple-600 btn-sm commentCompetitionRooms"
            @click="commentsDial.dialog.showModal"
          >
            <IconCommentary/>
            Комментарии
          </button>
          <button
            id="participateRooms"
            class="btn btn-outline btn-sm text-red-600 hover:bg-red-600"
            data-tip="Принять участие на роли помощника или супостата"
          >
            <IconTakePart/>
            Вписаться
          </button>
        </div>
      </slot>
    </div>
    <LazyDialogAccept
      ultrawide
      ref="bvDial"
      hide-accept
    >
      <template #title>
        {{
          {
            IN_PROGRESS: 'Ваше задание',
            VOTING: 'Кандидатки'
          }[item.status]
        }}
      </template>
      <template #content>
        <CardTask
          v-if="item?.status === 'IN_PROGRESS' && ['bride, groom'].includes(useRoles().userRole.value)"
          @completed="bvDial?.dialog?.close"
          :com-id="item.id"
          :task="getTaskByUserId(useAuth().userId)"
          is-executor
        />
        <div v-else>
          Ваша роль не предусматривает задания
        </div>
        <CardBrideVote
          v-if="item.status === 'VOTING'"
          :com-id="item.id"
        />
      </template>
    </LazyDialogAccept>
    <LazyDialogAccept
      ultrawide
      ref="commentsDial"
      hide-accept
    >
      <template #title>
        Комментарии
      </template>
      <template #content>
        <LazyDialogComments/>
      </template>
    </LazyDialogAccept>
  </div>
</template>

<style lang="sass" scoped>
.info div
  @apply flex gap-3 capitalize
  & .ico
    @apply text-red-600/75
</style>

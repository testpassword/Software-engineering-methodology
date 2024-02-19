<script setup>
import api from '/api'
import { useAsyncValidator } from '@vueuse/integrations/useAsyncValidator'

const { STATUSES } = useCompetitionsStatues()
const props = defineProps({
  item: { type: Object, required: true },
  hideActions: { type: Boolean, required: false, default: false },
})

let comApi = null

const { item } = toRefs(props)
const members = ref([])
const tasks = ref([])
const progress = computed(() => STATUSES.findIndex(it => it.name === item.value.status))

await useMountedApi(async () => {
  comApi = api.competitions[item.value.id]
  members.value = await comApi.members.get()
  try { tasks.value = await comApi.tasks.get() } catch { /* ignore if not exist */ }
})

const memberDial = ref()
const showedMember = ref({})

const getTaskByUserId = userId => tasks.value.find( it => it.executorId === userId )

const bvDial = ref()
const commentsDial = ref()
const changeStatusDial = ref()
const tomorrow = new Date(); tomorrow.setDate(new Date().getDate() + 1)

const form = ref({ status: '' })
const { pass } = useAsyncValidator(form, { status: { type: 'string', required: true } })

const changeStatus = async () => {
  const engSt = STATUSES.find(it => it.label === form.value.status).name
  await comApi.update({ status: engSt })
  item.value.status = engSt
}
</script>

<template>
  <div
    class="flex flex-col card glass competitionCard"
    :class="[item.status, `comp-${item.id}`]"
  >
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
          {{ getTaskByUserId(showedMember.id) }}
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
            class="btn btn-outline text-purple-600 hover:bg-purple-600 btn-sm commentCompetitionRooms"
            @click="commentsDial.dialog.showModal"
          >
            <IconCommentary/>
            Комментарии
          </button>
          <button
            class="btn btn-outline btn-sm text-red-600 hover:bg-red-600 changeCompStatus"
            data-tip="Изменить статус"
            @click="changeStatusDial.dialog.showModal"
          >
            <IconTakePart/>
            Изменить статус
          </button>
        </div>
      </slot>
    </div>
    <LazyDialogAccept
      ref="changeStatusDial"
      @accept="changeStatus"
    >
      <template #title>
        Изменить статус
      </template>
      <template #content>
        <select
          class="select select-bordered w-full changeStatusSelect"
          v-model="form.status"
        >
          <option
            disabled
            selected
            value
          >
            Статус
          </option>
          <option
            v-for="s in STATUSES"
            :value="s.label"
          >
            {{ s.label }}
          </option>
        </select>
      </template>
    </LazyDialogAccept>

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
          v-if="item?.status === 'IN_PROGRESS' && ['bride', 'groom'].includes(useRoles().userRole.value)"
          @completed="bvDial?.dialog?.close"
          :com-id="item.id"
          :task="getTaskByUserId(parseInt(useAuth().userId.value))"
          is-executor
        />
        <CardBrideVote
          v-if="item.status === 'VOTING'"
          :com-id="item.id"
        />
        <CardMarriage v-if="item.status === 'MARRIAGE'"/>
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

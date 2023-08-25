<script setup>
import { useAsyncValidator } from '@vueuse/integrations/useAsyncValidator'
import api from '/api'
definePageMeta({ middleware: ['role', 'auth'] })

const cities = useRUCities()
const users = ref([])
useMountedApi(async () => users.value = await api.users.get())

const createCompetitionDial = ref()
const form = ref({
  name: '',
  city: '',
})

const { pass, errorFields } = useAsyncValidator(
  form,
  {
    name: { type: 'string', required: true },
    city: { type: 'enum',   required: true, enum: cities },
  }
)

// todo: не показывать в следующих выпадающих элементах тех, кто уже заассайнен
const rolesTasks = ref(
  ['bride', 'bride', 'bride', 'groom', 'enemy', 'assistant']
    .map(it => ({ task: '', user: null, role: it }))
)
const validated = computed(() => rolesTasks.value.filter( it => it.task && it.user ).length)
const completed = computed(() => validated.value === rolesTasks.value.length)

const assignTask = ut => {
  const { user, task } = ut
  const executor = rolesTasks.value.find( it => it.role === user.role && !it.task )
  executor.task = task
  executor.user = user
}

const createCompetition = async () => {
  const comApi = api.competitions.for((await api.competitions.create(form.value)).id)
  await comApi.update({
    tasksIds: await Promise.all(
      rolesTasks
        .value
        .map(async it => (await comApi.tasks.create({ text: it.task, executorId: it.user.phone })).id)
    )
  })
  alert('Соревнование создано! Все участники получат приглашения в ближайшее время.')
}

const tomorrow = new Date(); tomorrow.setDate(new Date().getDate() + 1)
</script>

<template>

  <CardBrideVote
    :com-id="1"
    :bride-vote="{
      candidates: [
       { brideId: 1, points: 1   },
       { brideId: 2, points: 132 },
       { brideId: 3, points: 32  },
      ],
      endTime: tomorrow
    }"/>

  <div>
    <Header
      ico-name="Gamepad"
      title="Комната Свахи"
    />
    <div class="flex flex-row mt-10 gap-8 flex-wrap">
      <ListCompetition/>
      <h3 class="text-yellow-600">список пользователей</h3>
      <h3 class="text-yellow-600">заявки</h3>
      <h3 class="text-yellow-600">обновления</h3>
      <ActionsPanel>
        <template #extra>
          <button
            class="btn text-purple-500 btn-outline"
            @click="createCompetitionDial.dialog.showModal"
          >
            <IconCreateCompetition/>
            Создать испытание
          </button>
        </template>
      </ActionsPanel>
    </div>
    <AcceptDialog
      ref="createCompetitionDial"
      ultrawide
      :hide-accept="!completed || !pass"
      @accept="createCompetition"
    >
      <template #title>
        Создать испытание
      </template>
      <template #content>
        <div class="flex gap-3">
          <input
            type="text"
            placeholder="Название"
            class="input input-bordered w-full"
            :class="{ 'input-error': errorFields?.name?.length }"
            v-model="form.name"
          />
          <select
            class="select select-bordered w-full max-w-xs"
            :class="{ 'select-error': errorFields?.city?.length }"
            v-model="form.city"
          >
            <option
              disabled
              selected
              value
            >
              Город
            </option>
            <option v-for="c in cities">
              {{ c }}
            </option>
          </select>
        </div>
        <FormCandidateTask
          :users="users"
          v-for="{ role } in rolesTasks"
          :candidate-role="role"
          :city="form.city"
          @completed="assignTask"
        />
      </template>
    </AcceptDialog>
  </div>
</template>

<script setup>
import { useAsyncValidator } from '@vueuse/integrations/useAsyncValidator'
import api from '/api'
definePageMeta({ middleware: ['role', 'auth'] })

const cities = useRUCities()
const users = ref([])
const compsTable = ref()
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
const rolesTasks = ref(['bride', 'groom', 'enemy', 'assistant'].map(it => ({ task: '', users: [], role: it }))
)
const validated = computed(() =>
  rolesTasks
    .value
    .filter( it => it.users.length > 0 && (['assistant', 'enemy'].includes(it.role) ? true : it.task) )
    .length
)
const completed = computed(() => validated.value === rolesTasks.value.length)

const createCompetition = async () => {
  const comApi = api.competitions[(await api.competitions.create(form.value)).id]
  const rolesTasksWithUsers =
    rolesTasks
      .value
      .map(it =>
        it
          .users
          .map(u => ({ text: it.task, executorId: u.id }))
      )
      .flat()
  await comApi.update({
    tasksIds: await Promise.all(
      rolesTasksWithUsers
        .map(async it => (await comApi.tasks.create(it)).id)
    )
  })
  alert('Соревнование создано! Все участники получат приглашения в ближайшее время.')
  await compsTable.value.refresh()
}

onMounted(() => window.assignDebug = async (tasks = []) => {
  // DO NOT CALL THIS FUNC UNTIL YOU SET CITY OF FUTURE COMPETITION
  const getTask = r => rolesTasks.value.filter(it => it.role === r)[0]
  const filteredUsers = r => users.value.filter( it => it.city === form.value.city && it.role === r )
  const brideTask = getTask('bride')
  brideTask.task = tasks[0]
  brideTask.users = filteredUsers('bride').slice(0, 1)
  const groomTask = getTask('groom')
  groomTask.task = tasks[1]
  groomTask.users = filteredUsers('groom').slice(0, 3)
  getTask('assistant').users = filteredUsers('assistant').slice(0, 1)
  getTask('enemy').users = filteredUsers('enemy').slice(0, 1)
})
</script>

<template>
  <div>
    <Header
      ico-name="Gamepad"
      title="Комната Свахи"
    />
    <div class="flex flex-row mt-10 gap-8">
      <AsyncDataTable
        ref="compsTable"
        :api-fn="api.competitions"
        item-component-name="Competition"
      >
        <template #title>
          Список состязаний
        </template>
      </AsyncDataTable>
      <AsyncDataTable
        :api-fn="api.users"
        item-component-name="User"
      >
        <template #title>
          Список пользователей
        </template>
      </AsyncDataTable>
      <ActionsPanel>
        <template #extra>
          <button
            id="createCompetitionRooms"
            class="btn text-purple-500 btn-outline btn-sm"
            @click="createCompetitionDial.dialog.showModal"
          >
            <IconCreateCompetition/>
            Создать испытание
          </button>
        </template>
      </ActionsPanel>
    </div>
    <LazyDialogAccept
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
            id="competitionCreationName"
            type="text"
            placeholder="Название"
            class="input input-bordered w-full"
            :class="{ 'input-error': errorFields?.name?.length }"
            v-model="form.name"
          />
          <select
            id="competitionCreationCity"
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
        <LazyFormCandidateTask
          :users="users"
          v-for="{ role } in rolesTasks"
          :empty-task="['assistant', 'enemy'].includes(role)"
          :candidate-role="role"
          :city="form.city"
          @completed="({ users, task }) => {
            const executorPrototype = rolesTasks.find( it => it.role === role)
            executorPrototype.task = task
            executorPrototype.users = users
          }"
        />
      </template>
    </LazyDialogAccept>
  </div>
</template>

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
}
</script>

<template>
  <div>
    <Header
      ico-name="Gamepad"
      title="Комната Свахи"
    />
    <div class="flex flex-row mt-10 gap-8">
      <ListCompetition/>
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
    </AcceptDialog>
  </div>
</template>

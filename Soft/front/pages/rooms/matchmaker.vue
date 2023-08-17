<script setup>
import { useAsyncValidator } from '@vueuse/integrations/useAsyncValidator'
definePageMeta({ middleware: ['role', 'auth'] })

const cities = useRUCities()
const { ROLES } = useRoles()

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

const rolesTasks = ref(
  ROLES
    .map( it => it.name )
    .filter( it => it && !['сваха', 'гость'].includes(it) )
    .map( it => ({ role: it, task: '', user: null }) )
)
const validated = computed(() => rolesTasks.value.filter( it => it.task && it.user ).length)
const completed = computed(() => validated.value === rolesTasks.value.length)

const assignTask = ut => {
  const { user, task } = ut
  const executor = rolesTasks.value.find( it => it.role === user.role )
  executor.task = task
  executor.user = user
}
</script>

<template>
  <div>
    <Header
      ico-name="Gamepad"
      title="Комната Свахи"
    />
    <div class="flex flex-row mt-10 gap-8 flex-wrap">
      <h1 class="text-yellow-600">список испытания</h1>
      <h1 class="text-yellow-600">список пользователей</h1>
      <h1 class="text-yellow-600">заявки</h1>
      <h1 class="text-yellow-600">обновления</h1>
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
          v-for="{ role } in rolesTasks"
          :candidate-role="role"
          :city="form.city"
          @completed="assignTask"
        />
      </template>
    </AcceptDialog>
  </div>
</template>

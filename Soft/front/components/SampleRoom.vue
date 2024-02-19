<script setup>
import api from '/api'
import { useAuth } from '/composables/useAuth'

definePageMeta({ middleware: ['role', 'auth'] })

let userApi
const user = ref(null)
const currentCompetition = ref(null)

const refreshUser = async () => user.value = await userApi.get()

useMountedApi(async () => {
  userApi = api.users.for(useAuth().userId.value)
  await refreshUser()
  // todo: получать состязание пользователя, а не первое состязание с api
  currentCompetition.value = (await api.competitions.get())[0]
})

const dismiss = async () => {
  const res = confirm(`
  Отказаться от участия в состязании:
  вы автоматически будет признаны проигравшим и забанены. Продолжить?
  `)
  if (res) {
    currentCompetition.value = null
    // todo: отправить на api
  }
}
</script>

<template>
  <div>
    <Header
      ico-name="SimpleArrow"
      title="Комната Невесты"
    />
    <div class="flex flex-row mt-10 gap-8 flex-wrap h-fit">
      <div class="flex flex-row gap-8 h-fit">
        <div class="stats bg-primary text-primary-content w-full">
          <div class="stat bg-secondary">
            <h3>
              Ваше состязание
            </h3>
            <div v-if="currentCompetition" class="flex flex-col gap-4">
              <CardCompetition :item="currentCompetition">
                <template #actions>
                  <button
                    id="dismissRooms"
                    class="btn btn-warning btn-outline w-fit btn-sm"
                    @click="dismiss"
                  >
                    <IconDismiss/>
                    Отказаться
                  </button>
                </template>
              </CardCompetition>
            </div>
            <template v-else>
              <span class="text-center text-[150px]">
              ☹️
            </span>
              <div class="stat-desc text-grey-800">
                У вас нет текущих испытаний. Запустите стрелу, чтобы начать.
              </div>
            </template>
          </div>
        </div>
      </div>
      <AsyncDataTable
        :api-fn="api.competitions"
        item-component-name="Competition"
      >
        <template #title>
          Список состязаний
        </template>
      </AsyncDataTable>
      <ActionsPanel/>
    </div>
  </div>
</template>

<style lang="sass" scoped>
.calc
  @apply flex flex-row gap-4
  & span
    @apply pt-3 text-xl
  span.sum
    @apply pt-1 font-bold text-3xl text-green-600
.stat-actions p
  @apply text-error text-sm
</style>

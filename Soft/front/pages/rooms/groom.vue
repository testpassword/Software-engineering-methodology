<script setup>
import api from '/api'
import { useAuth } from '../../composables/useAuth'

definePageMeta({ middleware: ['role', 'auth'] })

const buyArrowsModal = ref()
const price = ref(-1)
const wantToBuy = ref(1)

let userApi
const user = ref(null)
const currentCompetition = ref(null)

const refreshUser = async () => user.value = await userApi.get()

useMountedApi(async () => {
  userApi = api.users.for(useAuth().userId.value)
  await refreshUser()
  // todo: получать состязание пользователя, а не первое состязание с api
  currentCompetition.value = (await api.competitions.get())[0]
  // todo: сделать эндпоинт price на беке
  price.value = (await api.arrows.price.get()).price
})

const buy = async () => {
  const val = wantToBuy.value
  await userApi.arrows.buy(val)
  alert(`Успешно приобретено ${val} стрел. Поздравляем!`)
  await refreshUser()
}

const pushArrow = async () => {
  await userApi.update({ isPairing: true })
  await refreshUser()
}

const dismiss = async () => {
  const res = confirm(`
  Отказаться от участия в состязании:
  вы автоматически будет признаны проигравшим и забанены. Продолжить?
  `)
  if (res) {
    // todo: отправить на api
  }
}
</script>

<template>
  <div>
    <Header
      ico-name="SimpleArrow"
      title="Комната Жениха"
    />
    <div class="flex flex-row mt-10 gap-8 flex-wrap h-fit">
      <div class="stats bg-primary text-primary-content">
        <div class="stat bg-secondary">
          <div class="stat-figure">
            <IconArrow class="w-32"/>
          </div>
          <h3>
            Количество стрел
          </h3>
          <div class="stat-value text-grey-700">
            {{ user?.arrowsAmount ?? 'Error' }}
          </div>
          <div class="stat-actions flex gap-4">
            <p v-if="!price">
              Не удалось получить <br/> цену стрелы, <br/> попробуйте позже
            </p>
            <button
              class="btn btn-success btn-outline"
              @click="buyArrowsModal.dialog.showModal"
              v-else
            >
              <IconMoney/>
              Купить
            </button>
            <p v-if="user?.arrowsAmount === 0">
              Купить стрелу, <br/> чтобы запустить
            </p>
            <button
              :disabled="user?.isPairing || user?.arrowsAmount <= 0"
              class="btn bg-primary"
              @click="pushArrow"
            >
              <IconPush/>
              Запуск
            </button>
          </div>
          <div class="stat-desc text-grey-800">
            После запуска стрелы, наши Свахи начнут искать вам пару.<br/>
            Убедитесь, что правильно заполнили страницу о себе:<br/>
            эти данные будут использоваться для подбора пары.<br/>
            Вы получите уведомление, когда пары будут найдены.
          </div>
        </div>
      </div>
      <div class="flex flex-row gap-8 h-fit">
        <div class="stats bg-primary text-primary-content w-full">
          <div class="stat bg-secondary">k
            <h3>
              Ваше состязание
            </h3>
            <div v-if="currentCompetition" class="flex flex-col gap-4">
              <CardCompetition :item="currentCompetition">
                <template #actions>
                  <button
                    class="btn btn-warning btn-outline w-fit"
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
    <LazyDialogAccept
      ref="buyArrowsModal"
      ultrawide
      @accept="buy"
    >
      <template #title>
        Купить стрелы
      </template>
      <template #content>
        <div class="calc">
          <span>
            Цена стрелы: {{ price }} *
          </span>
          <input
            type="number"
            placeholder="Приобрести"
            class="input input-bordered"
            min="1"
            v-model="wantToBuy"
          />
          <span>
            =
          </span>
          <span class="sum">
            {{ price * wantToBuy }} ₽
          </span>
        </div>
      </template>
    </LazyDialogAccept>
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

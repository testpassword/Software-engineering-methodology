<script setup>
import api from '/api'

definePageMeta({ middleware: ['role', 'auth'] })

const currentAmount = ref()
const buyArrowsModal = ref()
const price = ref(-1)
const wantToBuy = ref(1)

let userApi
const user = ref(null)
const currentCompetition = ref(null)

const actualizeArrowsAmount = async () => currentAmount.value = (await userApi[useAuth().userId.value].arrows.get())

watch(user, nv => currentAmount.value = nv?.arrowsAmount)

useMountedApi(async () => {
  userApi = api.users.for()
  user.value = await userApi.get()
  price.value = (await api.arrows.price.get()).price
  await actualizeArrowsAmount()
})

const buy = async () => {
  const val = wantToBuy.value
  await userApi.arrows.buy(val)
  alert(`Успешно приобретено ${val} стрел. Поздравляем!`)
  await actualizeArrowsAmount()
}

const pushArrow = async () => {
  await userApi.update({ isPairing: true })
  await actualizeArrowsAmount()
}
</script>

<template>
  <div>
    <Header
      ico-name="SimpleArrow"
      title="Комната Жениха"
    />
    <div class="flex flex-row mt-10 gap-8 flex-wrap h-[90vh]">
      <div class="stats bg-primary text-primary-content">
        <div class="stat bg-secondary">
          <div class="stat-figure">
            <IconArrow class="w-32"/>
          </div>
          <h3>
            Количество стрел
          </h3>
          <div class="stat-value text-grey-700">
            {{ currentAmount ?? 'Error' }}
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
            <p v-if="currentAmount === 0">
              Купить стрелу, <br/> чтобы запустить
            </p>
            <button
              :disabled="user?.isPairing || currentAmount <= 0"
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
      <div class="flex flex-row gap-8 flex-grow">
        <div class="stats bg-primary text-primary-content w-full">
          <div class="stat bg-secondary">
            <h3>
              Ваше испытание
            </h3>
            <div v-if="currentCompetition">
              todo: ЗДЕСЬ БУДЕТ ИСПЫТАНИЕ
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
      <ActionsPanel/>
    </div>
    <AcceptDialog
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
    </AcceptDialog>
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

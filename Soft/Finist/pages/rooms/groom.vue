<script setup>
const numOfArrows = ref(1)
const buyArrowsModal = ref()
const arrowPrice = ref(1000)
const arrowsNum = ref(1)

const currentCompetition = ref(null)
</script>

<template>
  <div>
    <Header>
      <div class="flex flex-col justify-center default-icon">
        <LazyIconSimpleArrow/>
      </div>
      <h2 class="font-bold w-3/5">
        Комната Жениха
      </h2>
    </Header>
    <div class="flex flex-row mt-10 gap-8 flex-wrap">
      <div class="stats bg-primary text-primary-content">
        <div class="stat bg-secondary">
          <div class="stat-figure">
            <LazyIconArrow class="w-32"/>
          </div>
          <h3>
            Количество стрел
          </h3>
          <div class="stat-value text-grey-700">
            {{ numOfArrows }}
          </div>
          <div class="stat-actions flex gap-4">
            <button
              class="btn btn-success btn-outline"
              @click="buyArrowsModal.showModal()"
            >
              <LazyIconMoney/>
              Купить
            </button>
            <button
              class="btn bg-primary"
              :disabled="numOfArrows === 0"
            >
              <LazyIconPush/>
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
      <div class="flex flex-row gap-8">
        <div class="stats bg-primary text-primary-content">
          <div class="stat bg-secondary">
            <h3>
              Настройки
            </h3>
            <div class="flex flex-col w-full gap-4">
              <button
                class="btn btn-info btn-outline"
                @click="navigateTo('/account-settings')"
              >
                <LazyIconUser/>
                Настройки аккаунта
              </button>
              <button
                class="btn btn-error btn-outline"
              >
                <LazyIconDelete/>
                Удалить аккаунт
              </button>
              <button
                class="btn text-black-100 btn-outline"
                @click="navigateTo('/login')"
              >
                <LazyIconExit/>
                Выйти
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <dialog
      ref="buyArrowsModal"
      class="modal bg-transparent min-w-[750px]"
    >
      <form
        method="dialog"
        class="modal-box min-w-full"
      >
        <button class="btn-modal-close">
          ✕
        </button>
        <h2 class="py-4">
          Купить стрелы
        </h2>
        <div class="calc">
          <span>
            Цена стрелы: {{ arrowPrice }} *
          </span>
          <input
            type="number"
            placeholder="Приобрести"
            class="input input-bordered"
            min="1"
            v-model="arrowsNum"
          />
          <span>
            =
          </span>
          <span class="sum">
            {{ arrowPrice * arrowsNum }} ₽
          </span>
        </div>
        <div class="w-full justify-center flex pt-8">
          <button class="btn btn-success">
            <LazyIconApply/>
            Подтвердить
          </button>
        </div>
      </form>
    </dialog>
  </div>
</template>

<style lang="sass" scoped>
.calc
  @apply flex flex-row gap-4
  & span
    @apply pt-3 text-xl
  span.sum
    @apply pt-1 font-bold text-3xl text-green-600
</style>

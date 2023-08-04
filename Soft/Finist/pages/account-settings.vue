<script setup>
import { useAsyncValidator } from '@vueuse/integrations/useAsyncValidator'
const cities = useRUCities()

const ROLES = useRoles().ROLES.filter( it => !it.isServiceRole )
const form = ref({
  role: '',
  name: '',
  city: '',
  age: null,
  education: '',
  aboutSelf: '',
  aboutPartner: ''
})
const education = ['Церковно-приходское', 'Среднее общее', 'Среднее профессиональное', 'Высшее']

const selectRole = role => {
  // todo: саундтреки для других ролей
  new Audio(`/roles/${role}.mp3`).play()
  form.value.role = role
}

watch(
  form,
  () => {
    nextTick(() => {
      const kl = o => Object.keys(o.value).length
      progress.value = 100 - Math.percentage(kl(form), kl(errorFields))
    })
  },
  { deep: true }
)

const progress = ref(0)

const { pass, errorFields } = useAsyncValidator(
  form,
  {
    role:         { type: 'enum',    required: true, enum: ROLES.map( it => it.name ) },
    name:         { type: 'string',  required: true },
    city:         { type: 'enum',    required: true, enum: cities },
    age:          { type: 'integer', required: true, min: 18 },
    education:    { type: 'enum',    required: true, enum: education },
    aboutSelf:    { type: 'string',  required: true, min: 8 },
    aboutPartner: { type: 'string',  required: true, min: 8 },
  }
)
</script>

<template>
  <div class="flex flex-col gap-8">
    <Header
      ico-name="User"
      title="Настройки аккаунта"
    >
      <progress
        class="progress progress-primary w-full mt-4"
        :value="progress"
        max="100"
      />
      <button
        class="btn btn-primary min-h-0 h-10"
        :disabled="!pass"
        @click="navigateTo('/run')"
      >
        Далее
      </button>
    </Header>
    <div class="flex flex-col gap-4">
      <h3>Роль</h3>
      <div class="flex flex-row overflow-scroll gap-3 w-full">
          <div
            class="card glass my-8 mx-2 flex transition-all min-w-[17rem]"
            :class="{
              'bg-primary': form.role === r.name,
              'hover:scale-110 hover:bg-gradient-to-r from-transparent via-secondary to-primary-focus': form.role !== r.name
            }"
            v-for="r in ROLES"
            @click="selectRole(r.name)"
          >
            <div class="w-full flex justify-center">
              <img
                class="avatar rounded-full m-2 mt-4 h-32 min-w-[8rem]"
                :src="`/roles/${r.name}.webp`"
                alt="role avatar"
              />
            </div>
            <div class="card-body p-2">
              <h2
                class="capitalize card-title justify-center"
                :class="{ 'text-grey-100': form.role === r.name }"
              >
                {{ r.name }}
              </h2>
              <p :class="{ 'text-grey-100': form.role === r.name }">
                {{ r.description }}
              </p>
            </div>
        </div>
      </div>
      <h3>Персональные данные</h3>
      <div class="flex flex-row gap-4 flex-wrap">
        <img
          class="avatar rounded-full m-2 h-48 "
          src="/avatar-placeholder.webp"
          alt="avatar"
        />
        <div class="flex flex-col gap-4">
          <input
            type="text"
            placeholder="Имя"
            class="input input-bordered"
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
          <input
            type="number"
            min="18"
            placeholder="Возраст (18+)"
            class="input input-bordered"
            :class="{ 'input-error': errorFields?.age?.length }"
            v-model="form.age"
          />
          <select
            class="select select-bordered w-full "
            :class="{ 'select-error': errorFields?.education?.length }"
            v-model="form.education"
          >
            <option
              disabled
              selected
              value
            >Образование
            </option>
            <option v-for="e in education">
              {{ e }}
            </option>
          </select>
        </div>
        <textarea
          class="textarea textarea-bordered"
          placeholder="О себе"
          :class="{ 'textarea-error': errorFields?.aboutSelf?.length }"
          v-model="form.aboutSelf"
        />
        <textarea
          class="textarea textarea-bordered"
          placeholder="О партнёре"
          :class="{ 'textarea-error': errorFields?.aboutPartner?.length }"
          v-model="form.aboutPartner"
        />
      </div>
    </div>
  </div>
</template>

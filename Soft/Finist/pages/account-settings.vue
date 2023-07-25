<script setup>
const { ROLES } = useRoles()

const selectedRole = ref(null)

const selectRole = role => {
  // todo: саундтреки для других ролей
  new Audio(`/roles/${role}.mp3`).play()
  selectedRole.value = role
}

watch(selectedRole, nv => {
  if (nv) progress.value = 100
  // todo: нормальное заполнение статистики
})

const progress = ref(0)

const pass = ref(true)
</script>

<template>
  <div class="flex flex-col gap-8">
    <div class="flex flex-row gap-4 sticky top-0 backdrop-blur-3xl z-10 rounded-xl p-4 drop-shadow-2xl">
      <div class="flex flex-col justify-center">
        <LazyIconUser />
      </div>
      <h2 class="font-bold w-3/5">
        Настройки аккаунта
      </h2>
      <progress
        class="progress progress-primary w-full mt-3"
        :value="progress"
        max="100"
      />
      <button
        class="btn btn-primary"
        :disabled="!pass"
      >
        <LazyIconHeart />
        Далее
      </button>
    </div>
    <div class="flex flex-col gap-4">
      <h3>Роль</h3>
      <div class="flex flex-row overflow-scroll gap-3 w-full">
          <div
            class="card glass my-8 mx-2 flex transition-all"
            :class="{
              'bg-primary': selectedRole === r.name,
              'hover:scale-110 hover:bg-gradient-to-r from-transparent via-secondary to-primary-focus': selectedRole !== r.name
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
                :class="{ 'text-grey-100': selectedRole === r.name }"
              >
                {{ r.name }}
              </h2>
              <p :class="{ 'text-grey-100': selectedRole === r.name }">
                {{ r.description }}
              </p>
            </div>
        </div>
      </div>
      <h3>Персональные данные</h3>
      <div class="flex flex-row gap-4 flex-wrap">
        <img
          class="avatar rounded-full m-2 h-48 "
          :src="`/placeholder.webp`"
          alt="avatar"
        />
        <div class="flex flex-col gap-4">
          <input
            type="text"
            placeholder="Имя"
            class="input input-bordered"
          />
          <input
            type="text"
            placeholder="Город"
            class="input input-bordered"
          />
          <input
            type="number"
            placeholder="Возраст (18+)"
            class="input input-bordered"
          />
          <select class="select select-bordered w-full max-w-xs">
            <option>Церковно-приходское</option>
            <option>Среднее общее</option>
            <option>Среднее профессиональное</option>
            <option>Высшее</option>
          </select>
        </div>
        <textarea class="textarea textarea-bordered" placeholder="О себе"/>
        <textarea class="textarea textarea-bordered" placeholder="О партнёре"/>
      </div>
    </div>
  </div>
</template>

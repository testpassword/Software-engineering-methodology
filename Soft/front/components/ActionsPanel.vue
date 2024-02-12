<script setup>
import api from '/api'

const deleteAccount = async () => {
  const res = confirm('Удалить аккаунт: вы уверены?')
  if (res) {
    // todo: не хватает id пользователя
    //await api.users.for().del()
    navigateTo('/login')
  }
}

const logout = () => {
  useAuth().logout()
  navigateTo('/login')
}
</script>

<template>
  <div class="flex flex-row gap-8 h-fit">
    <div class="stats bg-primary text-primary-content">
      <div class="stat bg-secondary">
        <h3>
          Панель возможностей
        </h3>
        <div class="flex flex-col w-full gap-4">
          <div class="divider">Стандартные</div>
          <button
            id="settingsRooms"
            class="btn btn-info btn-outline btn-sm"
            @click="navigateTo('/account-settings')"
          >
            <IconUser/>
            Настройки аккаунта
          </button>
          <button
            id="deleteAccountRooms"
            class="btn btn-error btn-outline btn-sm"
            @click="deleteAccount"
          >
            <IconDelete/>
            Удалить аккаунт
          </button>
          <button
            id="logout"
            class="btn text-black-100 btn-outline btn-sm"
            @click="logout"
          >
            <IconExit/>
            Выйти
          </button>
          <div
            class="divider"
            v-if="$slots.extra"
          >
            Ролевые
          </div>
          <slot name="extra"/>
        </div>
      </div>
    </div>
  </div>
</template>

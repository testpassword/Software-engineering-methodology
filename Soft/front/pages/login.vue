<script setup>
import { useAsyncValidator } from '@vueuse/integrations/useAsyncValidator'
import api from '/api'

const form = ref({ email: '', password: '' })
const { pass, errorFields } = useAsyncValidator(form, {
  email:     { type: 'email', required: true },
  password: { type: 'string', required: true }
})

const login = async () => {
  const { role, id } = await api.session.login(form.value)
  useAuth().userId.value = id
  useAuth().set(form.value)
  await Notification.requestPermission()
  if (role) {
    useRoles().userRole.value = role
    navigateTo(`/rooms/${role}`)
  } else navigateTo('/account-settings')
}
</script>

<template>
  <GateCard>
    <template #content>
      <h2>Вход</h2>
      <input
        id="emailLogin"
        type="email"
        placeholder="Почта"
        class="input input-bordered"
        :class="{ 'input-error': errorFields?.email?.length }"
        v-model="form.email"
      />
      <input
        id="passwordLogin"
        type="password"
        placeholder="Пароль"
        class="input input-bordered"
        :class="{ 'input-error': errorFields?.password?.length }"
        v-model="form.password"
      />
      <button
        id="login"
        class="btn btn-primary"
        :disabled="!pass"
        @click="login"
      >
        <IconHeart/>
        Найти любовь
      </button>
    </template>
    <template #link>
      <span class="text-center w-full">
      Ещё нет аккаунта?
      <NuxtLink id="toRegister" to="/register">
        Зарегистрироваться!
      </NuxtLink>
    </span>
    </template>
  </GateCard>
</template>

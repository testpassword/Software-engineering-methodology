<script setup>
import { useAsyncValidator } from '@vueuse/integrations/useAsyncValidator'
import api from '/api'

const form = ref({ phone: '', password: '' })

const { pass, errorFields } = useAsyncValidator(form, {
  phone:    { type: 'string', required: true, pattern: /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/im },
  password: { type: 'string', required: true }
})

const login = async () => {
  const { role } = await api.session.login(form.value)
  useAuth().set(form.value)
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
        type="tel"
        placeholder="Номер телефона (+7...)"
        class="input input-bordered"
        :class="{ 'input-error': errorFields?.phone?.length }"
        v-model="form.phone"
      />
      <input
        type="password"
        placeholder="Пароль"
        class="input input-bordered"
        :class="{ 'input-error': errorFields?.password?.length }"
        v-model="form.password"
      />
      <button
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
      <NuxtLink to="/register">
        Зарегистрироваться!
      </NuxtLink>
    </span>
    </template>
  </GateCard>
</template>

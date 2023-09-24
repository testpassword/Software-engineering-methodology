<script setup>
import { useAsyncValidator } from '@vueuse/integrations/useAsyncValidator'
import api from '/api'

const form = ref({ phone: '', email: '', password: '' })

const { pass, errorFields } = useAsyncValidator(
  form,
  {
    phone: {
      type: 'string',
      required: true,
      pattern: /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/im
    },
    email: {
      type: 'email',
      required: true,
    },
    password: {
      type: 'string',
      min: 6,
      required: true
    }
  }
)

const register = async () => {
  await api.users.register(form.value)
  navigateTo('/login')
}
</script>


<template>
  <GateCard>
    <template #content>
      <h2>Регистрация</h2>
      <input
        type="tel"
        placeholder="Номер телефона (+7...)"
        class="input input-bordered"
        v-model="form.phone"
        :class="{ 'input-error': errorFields?.phone?.length }"
      />
      <input
        type="email"
        placeholder="Почта"
        class="input input-bordered"
        v-model="form.email"
        :class="{ 'input-error': errorFields?.phone?.length }"
      />
      <input
        type="password"
        placeholder="Пароль"
        class="input input-bordered"
        v-model="form.password"
        :class="{ 'input-error': errorFields?.phone?.length }"
      />
      <label class="label cursor-pointer">
        <input
          type="checkbox"
          class="checkbox checkbox-primary mr-4"
        />
        <span class="label-text text-grey-800">
          Я согласен с политикой обработки персональных данных
        </span>
      </label>
      <button
        class="btn btn-primary z-10"
        :disabled="!pass"
        @click="register"
      >
        <IconDoor />
        Открыть своё сердце
      </button>
    </template>
    <template #link>
      <span class="text-center w-full">
      Уже есть аккаунт?
      <NuxtLink to="/login">
        Войти!
      </NuxtLink>
    </span>
    </template>
  </GateCard>
</template>

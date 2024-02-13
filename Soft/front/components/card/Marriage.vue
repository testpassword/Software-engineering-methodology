<script setup>
// todo: норм данные запрашивать
import api from '/api'
import { useAsyncValidator } from '@vueuse/integrations/useAsyncValidator'

const groom = ref({})
const bride = ref({})
const competition = ref({})

useMountedApi(async () => {
  groom.value = await api.users[1].get()
  bride.value = await api.users[2].get()
  competition.value = await api.competitions[1].get()
  competition.value.report = ''
})

const form = ref({ report: '' })

const { pass } = useAsyncValidator(form, { report: { type: 'string', required: true, min: 10 } })

const send = async () => {
  // todo: send to api
  alert('Счастье да мир молодожёнам!')
}

onMounted(() => { window.debugReport = report => { form.value.report = report } })
</script>

<template>
  <div class="flex flex-col card glass p-2 gap-4">
    <div class="flex flex-row gap-2">
      <h3>
        💍 Свадьба 🎉💖
      </h3>
      <span class="pt-1 text-sm">
        {{ competition.city }}
      </span>
      <div class="spacer"/>
      <button
        class="btn btn-secondary btn-xs tooltip tooltip-secondary"
        data-tip="комментарии"
      >
        <IconCommentary/>
      </button>
    </div>
    <div class="flex flex-row gap-3">
      <h3 class="pb-2 pr-2 flex items-center">
        {{ groom?.name }}
      </h3>
      <div>
        <img
          class="avatar rounded-full h-12"
          src="/avatar-placeholder.webp"
          alt="avatar"
        />
        <span class="pb-2 pr-2 flex items-center">
          ⚦
        </span>
      </div>
      <h2 class="text-accent flex items-center">
        ⚤
      </h2>
      <div>
        <img
          class="avatar rounded-full h-12"
          src="/avatar-placeholder.webp"
          alt="avatar"
        />
        <span class="pb-2 pr-2 flex items-center">
          ♀
        </span>
      </div>
      <h3 class="pb-2 pr-2 flex items-center">
        {{ bride?.name }}
      </h3>
      <div class="grow"/>
      <button
        :disabled="!pass"
        class="btn btn-outline btn-primary h-50 marriageReportBtn"
        @click="send"
      >
        <IconPush/>
        Отправить
      </button>
    </div>
    <Editor
      v-model:value="form.report"
      :disabled="!useRoles().isMatchmaker"
    />
  </div>
</template>

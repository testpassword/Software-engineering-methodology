<script setup>
import api from '/api'
import { useAsyncValidator } from '@vueuse/integrations/useAsyncValidator'

const props = defineProps({
  comId:      { type: Number,  required: true },
  task:       { type: Object,  required: true },
  isExecutor: { type: Boolean, required: true, default: false }
})

const emits = defineEmits(['completed'])

const { comId, task } = toRefs(props)
const taskApi = computed(() => api.competitions[comId.value].tasks[task.value?.id])

const form = ref({ report: '' })
const { pass } = useAsyncValidator(form, {
  report: { type: 'string', required: true, min: 10 }
})

onMounted(() => window.report = async report => { form.value = report })
</script>

<template>
  <div>
    <h3>Задание</h3>
    <p class="text-purple-600">
      {{ task?.text }}
    </p>
    <h3>Отчёт</h3>
    <div
      v-if="isExecutor && !task?.report"
      class="flex flex-col gap-4"
    >
      <Editor v-model:value="form.report"/>
      <button
        class="btn btn-primary reportBtn"
        v-if="pass"
        @click="async () => {
          await taskApi.update({ report: form.report })
          emits('completed')
        }"
      >
        <IconTaskSave/>
        Отправить
      </button>
    </div>
    <template v-else>
      <p v-if="task?.report">
        {{ task.report }}
      </p>
      <span
        v-else
        class="italic text-error"
      >
        Отчёт ещё не загружен
      </span>
    </template>
  </div>
</template>

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
</script>

<template>
  <div>
    <h3>Задание</h3>
    <p class="text-purple-600">
      {{ task?.text }}
    </p>
    <div class="flex flex-row gap-4 justify-center p-4">
      <input
        disabled
        type="checkbox"
        :checked="task?.completed"
        class="checkbox checkbox-primary"
      />
      Выполнено
    </div>
    <h3>Отчёт</h3>
    <div
      v-if="isExecutor && !task?.report"
      class="flex flex-col gap-4"
    >
      <Editor v-model:value="form.report"/>
      <button
        class="btn btn-primary"
        v-if="pass"
        @click="async () => {
          await taskApi.update(form.value)
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

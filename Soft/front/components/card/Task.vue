<script setup>
import api from '/api'

const props = defineProps({
  comId:      { type: Number, required: true },
  task:       { type: Object, required: true },
  isExecutor: { type: Boolean, required: true, default: false }
})

const { comId, task } = toRefs(props)
let taskApi = api.competitions[comId.value].tasks[task.value?.id]

const report = ref('')
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
    <Editor
      v-if="isExecutor && !task?.report"
      :value="report"
    />
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

<script setup>
// todo: использовать как фильтры для api
const props = defineProps({
  candidateRole: { type: String, required: true },
  city:          { type: String, required: true }
})

const user = ref(null)
const task = ref('')

// todo: удалить после создания api
const demoUsers = computed(() => [
  { name: 'kek', age: 14, role: 'жених', city: 'Zvony', education: 'высшее' },
  { name: 'lol', age: 18, role: 'невеста', city: 'Zvony', education: 'среднее' },
  { name: 'dsf', age: 22, role: 'помощникъ', city: 'Zvony', education: 'среднее' },
  { name: 'dsf', age: 22, role: 'супостатъ', city: 'Zvony', education: 'среднее' }
].filter( it => it.city === props.city && it.role === props.candidateRole ))

const emits = defineEmits(['completed'])
const completed = computed(() => user.value && task.value)
watch(completed, nv => { if (nv) emits('completed', { task: task.value, user: user.value }) })
</script>

<template>
  <div
    class="collapse collapse-arrow border rounded-xl"
    :class="[ completed ? 'border-secondary' : 'border-error' ]"
  >
    <input type="checkbox" />
    <div class="collapse-title">
      <div
        class="flex gap-4 capitalize"
        v-if="completed"
      >
        <h3>{{ user.name }}</h3>
        <span class="text-xl">|</span>
        <span class="truncate">{{ task.slice(0, 50) }}...</span>
      </div>
      <span
        class="text-error"
        v-else
      >
        Задание не создано
      </span>
    </div>
    <div class="collapse-content flex flex-col gap-8 overflow-scroll">
      <Editor v-model:value="task"/>
      <UserCard
        v-for="u in demoUsers"
        :user="u"
        @click="user = u"
        horizontal
        :class="{ 'bg-primary hover:bg-primary': user?.name === u.name }"
      />
    </div>
  </div>
</template>

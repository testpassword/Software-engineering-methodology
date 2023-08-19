<script setup>
// todo: использовать как фильтры для api
const props = defineProps({
  candidateRole: { type: String, required: true },
  city:          { type: String, required: true },
  users:         { type: Array, required: true }
})

const users = toRef(props, 'users')
const filteredUsers = computed( () => users.value.filter( it => it.city === props.city && it.role === props.candidateRole ))

const assignedUser = ref(null)
const task = ref('')
const emits = defineEmits(['completed'])
const completed = computed(() => assignedUser.value && task.value)
watch(completed, nv => { if (nv) emits('completed', { task: task.value, user: assignedUser.value }) })
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
        <h3>{{ assignedUser.name }}</h3>
        <span class="text-xl">|</span>
        <span class="truncate">{{ task.slice(0, 50) }}...</span>
      </div>
      <span
        class="text-error"
        v-else
      >
        Задание для роли
        <span class="font-bold text-lg">{{ useRoles().ROLES.find( it => it.name === candidateRole ).label }}</span>
        не создано
      </span>
    </div>
    <div class="collapse-content flex flex-col gap-8 overflow-scroll">
      <Editor v-model:value="task"/>
      <CardUser
        v-for="u in filteredUsers"
        :user="u"
        @click="assignedUser = u"
        horizontal
        :class="{ 'bg-primary hover:bg-primary': assignedUser?.name === u.name }"
      />
    </div>
  </div>
</template>

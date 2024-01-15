<script setup>
// todo: использовать как фильтры для api
// todo: избавится от emptyTask на UI и api
// todo: allowedUsersCount добавить
const props = defineProps({
  emptyTask:     { type: Boolean, required: false, default: false },
  candidateRole: { type: String,  required: true },
  city:          { type: String,  required: true },
  users:         { type: Array,   required: true }
})

const users = toRef(props, 'users')
const filteredUsers = computed( () => users.value.filter( it => it.city === props.city && it.role === props.candidateRole ))

const assignedUsers = ref([])
const task = ref('')
const emits = defineEmits(['completed'])
const completed = computed(() => assignedUsers.value.length > 0 && (props.emptyTask ? true : task.value))
watch(completed, nv => { if (nv) emits('completed', { task: task.value, users: assignedUsers.value }) })

const isCandidateAssign = candidate =>
  assignedUsers
    ?.value
    .map( it => it?.name )
    ?.includes(candidate?.name)

const swapAssigment = candidate =>
  assignedUsers
    .value
    ?.[isCandidateAssign(candidate) ? 'remove' : 'push']
    ?.(candidate)
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
        <h3>{{ assignedUsers?.map( it => it?.name ).join(' | ') }}</h3>
        <span class="text-xl">|</span>
        <span class="truncate">{{ task.slice(0, 50) }}...</span>
      </div>
      <span
        class="text-error"
        v-else
      >
        Исполнительно на роль
        <span class="font-bold text-lg">{{ useRoles().ROLES.find( it => it.name === candidateRole ).label }}</span>
        не назначен
      </span>
    </div>
    <div class="collapse-content flex flex-col gap-8 overflow-scroll">
      <Editor
        v-model:value="task"
        v-if="!emptyTask"
      />
      <CardUser
        v-for="u in filteredUsers"
        :item="u"
        @click="swapAssigment(u)"
        horizontal
        :class="{ 'bg-primary hover:bg-primary': isCandidateAssign(u) }"
      />
    </div>
  </div>
</template>

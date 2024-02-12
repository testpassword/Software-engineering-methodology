<script setup>
// todo: получать с api
import api from '/api'

const comments = ref([
  {
    userId: 11,
    text: 'круто',
    time: new Date()
  },
  {
    userId: 2,
    text: 'нет',
    time: new Date()
  },
  {
    userId: 3,
    text: 'не ешьте под этот видос',
    time: new Date()
  },
  {
    userId: 11,
    text: 'круто!!!',
    time: new Date()
  },
])

const newComment = ref()
const send = async () => {
  // todo: отправить в api
  comments.value.push({
    userId: useAuth().userId.value,
    text: newComment.value.value,
    time: new Date()
  })
}
</script>

<template>
  <div class="flex flex-col gap-6">
    <div v-for="{ userId, text, time } in comments">
      <div
        class="chat"
        :class="userId == useAuth().userId.value ? 'chat-end' : 'chat-start'"
      >
        <div class="chat-header">
          {{ userId }}
          <time class="text-xs opacity-50">{{ time.toLocaleString() }}</time>
        </div>
        <div
          class="chat-bubble"
          :class="userId == useAuth().userId.value ? 'chat-bubble-primary' : 'chat-bubble-info'"
        >
          {{ text }}
        </div>
      </div>
    </div>
    <div class="flex flex-row gap-3">
      <input
        class="input input-bordered input-primary w-full newCommentInput"
        ref="newComment"
      />
      <button
        class="btn btn-outline btn-primary h-50 newCommentBtn"
        @click="send"
      >
        <IconPush/>
      </button>
    </div>
  </div>
</template>

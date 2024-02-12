<script setup>
const props = defineProps({
  item:        { type: Object,  required: true },
  horizontal:  { type: Boolean, required: false, default: false },
  hideActions: { type: Boolean, required: false, default: false },
})

const { item } = toRefs(props)
const moreDial = ref()

const age = birthday => {
  const today = new Date()
  const birthDate = new Date(birthday)
  let age = today.getFullYear() - birthDate.getFullYear()
  if (today.getMonth() < birthDate.getMonth()) age--
  if (today.getDay() < birthDate.getDay()) age--
  return age
}

const blockUser = async () => {
  const res = confirm('Заблокировать пользователя? Он больше не сможет участвовать в состязания и оставлять комментарии.')
  if (res) {
    item.value.isBanned = true
    // todo: нет хватает вызова api
  }
}
</script>

<template>
  <div class="card glass flex flex-row gap-4 py-3 px-6">
    <div class="flex items-center">
      <img
        class="avatar rounded-full h-24"
        src="/avatar-placeholder.webp"
        alt="avatar"
      />
      <IconLockHeart
        class="text-error -ml-4 -mt-16 z-10 isBanned"
        v-if="item?.isBanned"
      />
    </div>
    <div
      class="flex info gap-0.5"
      :class="[ horizontal ? 'flex-row flex-wrap items-center' : 'flex-col' ]"
    >
      <div class="flex flex-row">
        <h3 class="pb-2 pr-2 flex items-center">
          {{ item?.name }}
        </h3>
        <slot
          name="actions"
          v-if="!hideActions"
        >
          <button
            class="btn btn-secondary btn-xs tooltip tooltip-secondary z-50 banUser"
            data-tip="заблокировать"
            @click="blockUser"
          >
            <IconBlock/>
          </button>
          <button
            v-if="useRoles().isMatchmaker"
            class="btn btn-secondary btn-xs tooltip tooltip-secondary userInfoBtn"
            data-tip="подробнее"
            @click="moreDial.dialog.showModal"
          >
            <IconMore/>
          </button>
        </slot>
      </div>
      <div>
        <IconAge/>
        <span>{{ age(item?.dateOfBirth) }}</span>
      </div>
      <div>
        <IconCity/>
        <span>{{ item?.city }}</span>
      </div>
      <div>
        <IconEducation/>
        <span>{{ item?.education }}</span>
      </div>
      <div>
        <IconMask/>
        <span>{{ item?.role }}</span>
      </div>
    </div>
    <slot name="default"/>
  </div>
  <LazyDialogAccept
    ref="moreDial"
    hide-accept
  >
    <template #title>
      Подробнее
    </template>
    <template #content>
      <PrettyJson :item="item"/>
    </template>
  </LazyDialogAccept>
</template>

<style lang="sass" scoped>
.info div
  @apply flex gap-3 capitalize
  & .ico
    @apply text-red-600/75
</style>

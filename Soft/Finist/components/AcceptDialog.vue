<script setup>
defineProps({
  hideAccept: { type: Boolean, required: false, default: false },
  hideClose:  { type: Boolean, required: false, default: false },
  ultrawide:  { type: Boolean, required: false, default: false }
})


const dialog = ref()

defineExpose({ dialog })
</script>

<template>
  <dialog
    ref="dialog"
    class="modal bg-transparent min-w-[750px]"
  >
    <form
      method="dialog"
      :class="{ 'min-w-full': ultrawide }"
      class="modal-box"
    >
      <button
        class="btn-modal-close"
        v-if="!hideClose"
      >
        ✕
      </button>
      <h2 class="py-4">
        <slot name="title"/>
      </h2>
      <slot name="content"/>
      <div
        class="w-full justify-center flex pt-8"
        v-if="!hideAccept"
      >
        <button class="btn btn-success">
          <IconApply/>
          Подтвердить
        </button>
      </div>
    </form>
  </dialog>
</template>

<style lang="sass" scoped>
button.btn-modal-close
  @apply btn btn-sm btn-circle btn-ghost absolute right-2 top-2 hover:bg-red-600 hover:text-green-100
</style>

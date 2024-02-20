<script setup>
const checked = ref(false)

watch(checked, nv => {
  if (nv) {
    // todo: send to api
    alert('Ждите подтверждения от Свахи')
  }
})
</script>

<template>
  <div class="form-control">
    <label class="cursor-pointer label">
      <h3 class="pr-4">Согласие</h3>
      <p>Вы согласно пожениться?</p>
      <input
        type="checkbox"
        :checked="checked"
        @click="checked = true"
        class="checkbox checkbox-primary marriageAgreementCheck"
      />
    </label>
  </div>
</template>

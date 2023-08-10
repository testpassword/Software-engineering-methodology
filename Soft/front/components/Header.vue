<script setup>
const props = defineProps({
  icoName: { type: String, required: true },
  title:   { type: String, required: true }
})

const comp = computed(() => defineAsyncComponent(() => import(`./icon/${props.icoName}.vue`)))
</script>

<template>
  <div class="flex flex-row gap-4 sticky top-0 backdrop-blur-3xl z-10 rounded-xl p-4 drop-shadow-2xl">
    <div class="flex flex-col justify-center default-icon">
      <component :is="comp"/>
    </div>
    <h2 class="font-bold w-3/5">
      {{ title }}
    </h2>
    <slot name="default"/>
  </div>
</template>

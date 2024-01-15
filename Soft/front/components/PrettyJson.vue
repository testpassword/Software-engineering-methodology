<script setup>
const props = defineProps({
  item: { type: Object, required: true }
})

const { item } = toRefs(props)
</script>

<template>
  <div v-for="[k, v] in Object.entries(item)">
    <span class="font-bold text-accent">
      {{ k?.capitalize()?.match(/[A-Z][a-z]+|[0-9]+/g)?.join(' ') }}:
    </span>
    <span>
      {{ v }}
    </span>
  </div>
</template>

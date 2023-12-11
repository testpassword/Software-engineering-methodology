<script setup>
const items = ref([])
const props = defineProps({
  apiFn: { type: Object, required: true },
  itemComponentName: { type: String, required: true }
})

const { apiFn, itemComponentName } = toRefs(props)

useMountedApi(async () => {
  items.value = await apiFn.value.get()
})

const itemComponent = computed(() => (
  {
    'Competition': defineAsyncComponent(() => import('/components/card/Competition.vue')),
    'User': defineAsyncComponent(() => import('/components/card/User.vue')),
  }[itemComponentName.value])
)
</script>

<template>
  <div class="stats bg-primary text-primary-content h-fit">
    <div class="stat bg-secondary gap-4">
      <h3>
        <slot name="title"/>
      </h3>
      <component
        :is="itemComponent"
        v-for="c in items"
        :item="c"
      />
    </div>
  </div>
</template>

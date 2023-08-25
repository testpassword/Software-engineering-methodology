<script setup>
import api from '/api'

const coms = ref([])

useMountedApi(async () => {
  coms.value = await api.competitions.get()
})
</script>

<template>
  <div class="stats bg-primary text-primary-content">
    <div class="stat bg-secondary gap-4">
      <h3>
        Список соревнований
      </h3>
      <CardCompetition
        v-for="c in coms"
        :competition="c"
      />
    </div>
  </div>
</template>

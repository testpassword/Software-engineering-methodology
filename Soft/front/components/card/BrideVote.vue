<script setup>
import api from '/api'

const props = defineProps({
  comId:       { type: Number, required: true },
  brideVote:   { type: Object, required: false },
})
const { comId } = toRefs(props)
const bv = ref(props.brideVote)
const bvApi = api.competitions[comId.value].brideVote

const remainSec = ref({})

const brides = ref([])
useMountedApi(async () => {
  bv.value = bv.value ?? await bvApi.get()
  setInterval(() => remainSec.value = Math.ceil((bv.value.endTime - new Date()) / 1000), 1000)
  brides.value = (
    await bvApi
      .candidates
      .get()
  ).map(it => ({
    ...it,
    points: bv
      .value
      .candidates
      .find(c => c.brideId === it.id)
      .points
  }))
    .sort((a, b) => b.points - a.points)
})

const voteDial = ref()
const selectedBrideId = ref()

const selectBride = brideId => {
  if (!bv.value.userVote) {
    voteDial.value.dialog.showModal()
    selectedBrideId.value = brideId
  } else alert('Вы уже голосовали')
}

const vote = async () => {
  await bvApi[selectedBrideId.value].vote.inc()
  brides.value.find( it => it.id === selectedBrideId.value ).points++
  bv.value.userVote = selectedBrideId.value
  alert(`Ващ голос засчитан!`)
}
</script>

<template>
    <div class="flex flex-row gap-4 py-1">
      <h4>До конца голосования:</h4>
      <div
        class="flex timer"
        v-if="remainSec > 0"
      >
        <div><span>{{ remainSec }}</span>sec</div>
      </div>
      <span v-else>
        завершено
      </span>
    </div>
    <div class="flex flex-row gap-8">
      <div
        v-for="{ name, points, id } in brides"
        @click="selectBride(id)"
        class="card glass flex flex-row transition-all p-3"
        :class="{
          'bg-primary': bv.userVote === id,
        }"
      >
        <img
          class="avatar rounded-full h-20 w-20"
          src="/avatar-placeholder.webp"
          alt="role avatar"
        />
        <div class="flex flex-col px-5">
          <h3 class="text-center">
            {{ name }}
          </h3>
          <Chip>
            {{ points }}
          </Chip>
        </div>
      </div>
    </div>
  <AcceptDialog
    ref="voteDial"
    @accept="vote"
  >
    <template #title>
      Вы уверены?
    </template>
    <template #content>
      Изменить свой голос будет нельзя
    </template>
  </AcceptDialog>
</template>

<style lang="sass" scoped>
.timer
  span
    @apply countdown font-mono pr-1 text-primary
</style>

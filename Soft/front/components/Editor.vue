<script setup>
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

const props = defineProps({
  value:       { type: String,  required: true },
  disabled:    { type: Boolean, required: false, default: false },
  showWarning: { type: Boolean, default: false },
})
const emits = defineEmits(['update:value'])
const { value } = toRefs(props)
const { innerValue } = useInnerValue(value, emits)

const hideTooltip = ref(false)
onMounted(() => hideTooltip.value = Boolean(localStorage.getItem('hideTooltip')))
watch(hideTooltip, nv => { if (nv) localStorage.setItem('hideTooltip', nv) })
</script>

<template>
  <div
    class="alert alert-warning"
    v-if="!hideTooltip && showWarning"
  >
    <IconAlert/>
    <span>После сохранения, редактирование будет запрещено</span>
    <label class="label cursor-pointer">
      <span class="text-xs">Больше не показывать</span>
      <input
        type="checkbox"
        class="checkbox checkbox-primary"
        v-model="hideTooltip"
      />
    </label>
  </div>
  <div class="flex gap-4 flex-col">
    <MdEditor
      :disabled="disabled"
      :preview="false"
      language="en-US"
      v-model="innerValue"
      :toolbarsExclude="['github', 'htmlPreview', 'catalog', 'fullscreen']"
      class="rounded-xl"
    />
  </div>
</template>

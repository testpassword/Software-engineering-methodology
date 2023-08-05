export default function useInnerValue (value, emit, options) {
  const { converter = v => v, prevent = () => false, onUpdate = () => {}, updateEventName = 'update:value' } = (options ?? {})

  const innerValue = ref(converter(value.value))
  emit(updateEventName, converter(innerValue.value))
  watch(value, (v) => { innerValue.value = converter(v) })
  watch(innerValue, (v) => {
    if (v === value.value) return
    onUpdate?.(v)
    if (prevent(v)) return
    emit(updateEventName, converter(v))
  }, { immediate: true })

  return {
    innerValue,
  }
}

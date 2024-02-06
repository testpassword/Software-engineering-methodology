const ROLES = [
  {
    name: 'matchmaker',
    label: 'сваха',
    description: `От вас требуется обдуманно и ответственно находить пары участников, придумывать для них испытания.`,
  },
  {
    name: 'groom',
    label: 'жених',
    description: 'Вы одинокий совершеннолетний мужчины, желающий создать семью.',
  },
  {
    name: 'bride',
    label: 'невеста',
    description: 'Вы одинокая совершеннолетние девушка, желающая создать семью.',
  },
  {
    name: 'guest',
    label: 'гость',
    description: 'Вы просто хотите весело провести время, наблюдая как другие ищут любовь.',
  },
  {
    name: 'assistant',
    label: 'помощникъ',
    description: 'Вы хотите помогать в выполнении испытаний невесте.',
  },
  {
    name: 'enemy',
    label: 'супостатъ',
    description: 'Вы хотите мешать жениху выполнять испытания.',
  },
]

// will be set after login.vue
const userRole = ref(ROLES[0].name)

const isMatchmaker = computed(() => userRole.value === ROLES[0].name)

export const useRoles = () => ({
  userRole,
  ROLES,
  isMatchmaker
})

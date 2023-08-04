const ROLES = [
  {
    name: undefined,
    allowedPages: ['/login', '/register'],
    isServiceRole: true
  },
  {
    name: 'сваха',
    description: `От вас требуется обдуманно и ответственно находить пары участников, придумывать для них испытания.`,
  },
  {
    name: 'жених',
    description: 'Вы одинокий совершеннолетний мужчины, желающий создать семью.',
    allowedPages: ['/login', '/register', '/account-settings', '/grooms_room'],
  },
  {
    name: 'невеста',
    description: 'Вы одинокая совершеннолетние девушка, желающая создать семью.',
  },
  {
    name: 'гость',
    description: 'Вы просто хотите весело провести время, наблюдая как другие ищут любовь.',
  },
  {
    name: 'помощникъ',
    description: 'Вы хотите помогать в выполнении испытаний невесте.',
  },
  {
    name: 'супостатъ',
    description: 'Вы хотите мешать жениху выполнять испытания.',
  },
]

const userRole = ref(ROLES[1])

export const useRoles = () => ({
  userRole,
  ROLES
})

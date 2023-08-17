import users from './users'
import competitions from './competitions'
import session from './session'

export default {
  ENDPOINT: '/',
  session: session(),
  users: users(),
  competitions: competitions()
}

import users from './users'
import competitions from './competitions'
import session from './session'
import { arrows } from './arrows'

export default {
  ENDPOINT: '/',
  session: session(),
  users: users(),
  competitions: competitions(),
  arrows: arrows()
}

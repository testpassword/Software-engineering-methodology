import 'core-js/actual/array/group'
import 'core-js/proposals/array-last'

export default defineNuxtPlugin(() => {

  Math.percentage = function (total, part) {
    return Math.round((part / total) * 100)
  }
})

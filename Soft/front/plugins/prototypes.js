import 'core-js/actual/array/group'
import 'core-js/proposals/array-last'

export default defineNuxtPlugin(() => {

  Math.percentage = function (total, part) {
    return Math.round((part / total) * 100)
  }

  Array.prototype.removeBy = function (k, v) {
    const i = this.findIndex( it => it[k] === v )
    if (i > -1) this.splice(i, 1)
    return this
  }
})

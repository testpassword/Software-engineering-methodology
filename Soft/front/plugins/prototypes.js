import 'core-js/actual/array/group'
import 'core-js/proposals/array-last'

export default defineNuxtPlugin(() => {

  Math.percentage = function (total, part) {
    return Math.round((part / total) * 100)
  }

  Array.prototype.remove = function (v) {
    const i = this.findIndex( it => JSON.stringify(it) === JSON.stringify(v) )
    if (i > -1) this.splice(i, 1)
    return this
  }

  String.prototype.capitalize = function () {
    return this.charAt(0).toUpperCase() + this.slice(1)
  }
})

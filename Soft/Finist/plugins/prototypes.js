export default defineNuxtPlugin(() => {

  Math.percentage = function (total, part) {
    return Math.round((part / total) * 100)
  }

})

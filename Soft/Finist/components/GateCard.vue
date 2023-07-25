<template>
  <div class="flex flex-col h-screen">
    <div class="h-1/3 flex justify-center items-center">
      <h1 class="font-extrabold tracking-wide uppercase text-transparent bg-clip-text bg-gradient-to-br from-red-400 to-white text-6xl">
        <slot name="title">
          Финист
        </slot>
      </h1>
    </div>
    <div class="h-1/3">
      <div class="flex flex-col gap-4">
        <slot name="content"/>
      </div>
    </div>
    <div class="h-1/3 flex items-end">
      <slot name="link"/>
    </div>
  </div>
</template>

<script setup>
</script>

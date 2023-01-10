<template>
  <web-view :src="url"></web-view>
</template>

<script>
export default {
  data() {
    return {
      url:''
    }
  },
  onLoad: function (options) {
    this.url = options.url
  }
}
</script>

<style>
</style>
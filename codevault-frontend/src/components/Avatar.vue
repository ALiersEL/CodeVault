<script setup lang="ts">
import { NAvatar } from "naive-ui";
import sha224 from "crypto-js/hmac-sha512";
import Identicon from "identicon.js"
const hash = sha224(localStorage.getItem("username")!, "secret");
const data = new Identicon(hash.toString(), 420).toString();
</script>

<template>
  <div>
    <n-avatar
      round
      size="medium"
      :src="`data:image/png;base64,${data}`"
      fallback-src="https://07akioni.oss-cn-beijing.aliyuncs.com/07akioni.jpeg"
    />
  </div>
</template>

<style scoped>
</style>

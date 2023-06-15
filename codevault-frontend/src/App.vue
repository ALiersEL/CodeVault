<script setup lang="ts">
  import { computed } from "vue";
  import { useOsTheme, darkTheme } from "naive-ui";
  const osThemeRef = useOsTheme();
  // computed
  const theme = computed(() => {
    return osThemeRef.value === "dark" ? darkTheme : {};
  });
</script>

<template>
  <n-config-provider :theme="theme">
    <router-view :key="$route.fullPath"></router-view>
  </n-config-provider>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  font-size: 15px;
}
</style>

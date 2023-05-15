<script setup lang="ts">
import NavigationPanel from './components/NavigationPanel.vue';
</script>

<template>
  <div>
    <NavigationPanel />
  </div>
  <router-view></router-view>
</template>

<style>
* {
  margin: 0;
  padding: 0;
}
</style>

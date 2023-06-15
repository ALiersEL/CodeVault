<script setup lang="ts">
import { NProgress, NEl } from "naive-ui";

const easyPercentage = 30;
const mediumPercentage = 10;
const hardPercentage = 60;
</script>

<template>
    <div>
      <n-el>
        <n-progress
          type="multiple-circle"
          :stroke-width="6"
          :circle-gap="0.5"
          :percentage="[
            easyPercentage,
            mediumPercentage,
            hardPercentage
          ]"
          :color="[
            'var(--success-color)',
            'var(--warning-color)',
            'var(--error-color)'
          ]"
          :rail-style="[
            { stroke: 'var(--success-color)', opacity: 0.3 },
            { stroke: 'var(--warning-color)', opacity: 0.3 },
            { stroke: 'var(--error-color)', opacity: 0.3 }
          ]"
        >
          <div style="text-align: center">
            答题难度占比
          </div>
        </n-progress>
      </n-el>
    </div>
</template>

<style scoped>
</style>
  
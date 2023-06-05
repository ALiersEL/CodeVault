<script setup lang="ts">
import { NInput, NIcon } from "naive-ui";
import { FlashOutline } from '@vicons/ionicons5'
</script>

<template>
    <n-input placeholder="搜索">
        <template #prefix>
            <n-icon :component="FlashOutline" />
        </template>
    </n-input>
</template>

<style scoped>
</style>

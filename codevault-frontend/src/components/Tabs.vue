<script setup lang="ts">
import { NTabs, NTabPane } from "naive-ui";
</script>

<template>
    <n-tabs :bar-width="28" type="line" class="custom-tabs">
        <n-tab-pane name="code" tab="Code" display-directive="show:lazy">
        代码
        </n-tab-pane>
        <n-tab-pane name="note" tab="Note" display-directive="show:lazy">
        笔记
        </n-tab-pane>
        <n-tab-pane name="other" tab="Other" display-directive="show:lazy">
        其他
        </n-tab-pane>
    </n-tabs>
</template>
  
<style scoped>
.custom-tabs .n-tabs-bar {
background-color: transparent !important;
}

.custom-tabs .n-tabs-bar::after {
position: absolute;
content: '';
left: 0;
right: 0;
top: 0;
bottom: -2px;
border-radius: 2px;
background-color: var(--bar-color);
}
</style>
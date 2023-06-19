<script setup lang="ts">
import { NTabs, NTabPane } from "naive-ui";
import CodeEditor from "./CodeEditor.vue";
import NoteEditor from "./NoteEditor.vue";
import CodeTable from "./CodeTable.vue";
import NoteTable from "./NoteTable.vue";
import DiffEditor from "./DiffEditor.vue";
</script>

<template>
    <n-tabs :bar-width="28" type="line" class="custom-tabs">
        <n-tab-pane name="CodeEditor" tab="新增代码" display-directive="show:lazy">
            <CodeEditor />
        </n-tab-pane>
        <n-tab-pane name="NoteEditor" tab="新增笔记" display-directive="show:lazy">
            <NoteEditor />
        </n-tab-pane>
        <n-tab-pane name="CodeDisplay" tab="查看代码" display-directive="show:lazy">
            <CodeTable />
        </n-tab-pane>
        <n-tab-pane name="NoteDisplay" tab="查看笔记" display-directive="show:lazy">
            <NoteTable />
        </n-tab-pane>
        <n-tab-pane name="ToolBox" tab="工具箱" display-directive="show:lazy" >
            <DiffEditor />
        </n-tab-pane>
    </n-tabs>
</template>
  
<style scoped>
.n-tabs {
    padding-left: 10px;
    padding-right: 10px;
}

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
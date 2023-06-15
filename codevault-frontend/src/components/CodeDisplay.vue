<script setup lang="ts">
import { onMounted } from "vue";
import { getMapping } from "../api/request";
// 导入highlight.js
import hljs from 'highlight.js';


onMounted(() => {
    const params = new URLSearchParams(window.location.search)
    const problemID = params.get('problemID');
    const codeID = params.get('codeID');
    getMapping(`problem/${problemID}/codes/${codeID}`,{}).then((res) => {
        console.log(res);
    });
});

// 代码高亮
hljs.highlightAll();
</script>

<template>
    <div>
        
    </div>
</template>

<style scoped>
.code-display {
    width: 100%;
    height: 100%;
    background-color: #f5f5f5;
    border-radius: 5px;
    padding: 10px;
}
</style>
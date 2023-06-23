<script setup lang="ts">
import { onMounted, onUpdated, ref } from "vue";
import { getMapping } from "../api/request";
import Prism from "prismjs";

const languageOptions = ['python', 'cpp', 'csharp', 'go', 'java', 'javascript', 'typescript', 'rust', 'sql', 'swift', 'kotlin', 'lua', 'perl', 'ruby', 'scala', 'php', 'r', 'objectiveC', 'erlang', 'elixir', 'dart', 'apex', 'markdown'];
const code = ref("");
const language = ref("cpp");

onMounted(() => {
    Prism.highlightAll();
    // 代码高亮
    const params = new URLSearchParams(window.location.search)
    const codeID = params.get('codeID');
    getMapping(`/problems/codes/${codeID}`,{}).then((res) => {
        console.log(res);
        code.value = res.data.data.codeText;
        language.value = languageOptions[res.data.data.codeLanguage];
    });
});

onUpdated(() => {
    Prism.highlightAll(); //修改内容后重新渲染
});
</script>

<template>
    <div class="code-display">
        <pre><code :class="`language-${language}`" v-html="Prism.highlight(code, Prism.languages[language], language)"></code></pre>
    </div>
</template>

<style scoped>
.code-display {
    background-color: rgb(39, 40, 34);
    height: 100vh;
    /* 居中 */
    overflow: auto;
}
</style>
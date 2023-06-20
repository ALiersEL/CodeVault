<script setup lang="ts">
import { onMounted, ref } from "vue";
import { getMapping } from "../api/request";
// 导入highlight.js
import hljs from 'highlight.js';
import javascript from 'highlight.js/lib/languages/javascript';
import python from 'highlight.js/lib/languages/python';
import cpp from 'highlight.js/lib/languages/cpp';
import csharp from 'highlight.js/lib/languages/csharp';
import go from 'highlight.js/lib/languages/go';
import java from 'highlight.js/lib/languages/java';
import typescript from 'highlight.js/lib/languages/typescript';
import rust from 'highlight.js/lib/languages/rust';
import sql from 'highlight.js/lib/languages/sql';
import swift from 'highlight.js/lib/languages/swift';
import kotlin from 'highlight.js/lib/languages/kotlin';
import lua from 'highlight.js/lib/languages/lua';
import perl from 'highlight.js/lib/languages/perl';
import ruby from 'highlight.js/lib/languages/ruby';
import scala from 'highlight.js/lib/languages/scala';
import php from 'highlight.js/lib/languages/php';
import r from 'highlight.js/lib/languages/r';
import objectivec from 'highlight.js/lib/languages/objectivec';
import erlang from 'highlight.js/lib/languages/erlang';
import elixir from 'highlight.js/lib/languages/elixir';

const languageOptions = ['python', 'cpp', 'csharp', 'go', 'java', 'javascript', 'typescript', 'rust', 'sql', 'swift', 'kotlin', 'lua', 'perl', 'ruby', 'scala', 'php', 'r', 'objectiveC', 'erlang', 'elixir', 'dart', 'apex', 'markdown'];
const code = ref(null);
const languageClass = ref('python');

onMounted(() => {
    // 代码高亮
    hljs.highlightAll();
    const params = new URLSearchParams(window.location.search)
    const codeID = params.get('codeID');
    getMapping(`/problems/codes/${codeID}`,{}).then((res) => {
        console.log(res);
        code.value = res.data.data.codeText;
        languageClass.value = languageOptions[res.data.language];
    });
});

</script>

<template>
    <div>
        <code>
            <pre class="code-display">
                <code :class="languageClass">
                    {{ code }}
                </code>
            </pre>
        </code>
    </div>
</template>

<style scoped>
.code-display {
    background-color: #f5f5f5;
    /* 居中 */
    display: flex;
    justify-content: center;
}
</style>
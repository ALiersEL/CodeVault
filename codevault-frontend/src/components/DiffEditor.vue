<script setup lang="ts">
import { ref, onMounted, toRaw } from "vue";
import * as monaco from "monaco-editor";
// import all languages
import "monaco-editor/esm/vs/basic-languages/monaco.contribution";
import { NSelect, NSpace } from "naive-ui";

const editor = ref<monaco.editor.IStandaloneDiffEditor | null>(null);
const selectedLanguage = ref("python");
const selectedTheme = ref("vs-dark");
const code1 = ref("");
const code2 = ref("");

onMounted(() => {
  initializeEditor();
});

const initializeEditor = () => {
  editor.value = monaco.editor.createDiffEditor(
    // 获取 DOM 节点
    document.getElementById("diffEditorContainer")!,
    {
      theme: selectedTheme.value,
      // 两边都能编辑
      readOnly: false,
      originalEditable: true,
      selectOnLineNumbers: true, //显示行号
      automaticLayout: true, //自动布局
      autoIndent: "advanced", //自动缩进
      renderLineHighlight: "line", //高亮当前行
      foldingStrategy: "indentation", // 代码可分小段折叠
    }
  );

  toRaw(editor.value!).setModel({
    original: monaco.editor.createModel(code1.value, selectedLanguage.value),
    modified: monaco.editor.createModel(code2.value, selectedLanguage.value),
  });
};

const changeLanguage = () => {
    // original和modified都改
    monaco.editor.setModelLanguage(
        toRaw(editor.value!).getModel()!.original,
        selectedLanguage.value
    );
    monaco.editor.setModelLanguage(
        toRaw(editor.value!).getModel()!.modified,
        selectedLanguage.value
    );
};

const changeTheme = () => {
  monaco.editor.setTheme(selectedTheme.value);
};

const languageOptions = [
  { label: "Python", value: "python" },
  { label: "C++", value: "cpp" },
  { label: "C#", value: "csharp" },
  { label: "Go", value: "go" },
  { label: "Java", value: "java" },
  { label: "JavaScript", value: "javascript" },
  { label: "TypeScript", value: "typescript" },
  { label: "Rust", value: "rust" },
  { label: "SQL", value: "sql" },
  { label: "Swift", value: "swift" },
  { label: "Kotlin", value: "kotlin" },
  { label: "Lua", value: "lua" },
  { label: "Perl", value: "perl" },
  { label: "Ruby", value: "ruby" },
  { label: "Scala", value: "scala" },
  { label: "PHP", value: "php" },
  { label: "R", value: "r" },
  { label: "Objective-C", value: "objective-c" },
  { label: "Erlang", value: "erlang" },
  { label: "Elixir", value: "elixir" },
  { label: "Dart", value: "dart" },
  { label: "Apex", value: "apex" },
  { label: "Markdown", value: "markdown" },
];

const themeOptions = [
  { label: "Visual Studio", value: "vs" },
  { label: "Visual Studio Dark", value: "vs-dark" },
  { label: "High Contrast Dark", value: "hc-black" },
];
</script>

<template>
    <div class="container">
        <n-space>
            <n-select
            v-model:value="selectedLanguage"
            filterable
            :options="languageOptions"
            @update:value="changeLanguage"
            />

            <n-select
                v-model:value="selectedTheme"
                :options="themeOptions"
                @update:value="changeTheme"
            />
        </n-space>

        <div id="diffEditorContainer" class="diff-editor"></div>
    </div>
</template>

<style scoped>
.container {
  width: 100%;
  height: 100%;
  flex-direction: column;
}
.diff-editor {
  width: 100%;
  height: 81vh; /* 根据需要设置编辑器的高度 */
  margin-top: 10px;
}
</style>
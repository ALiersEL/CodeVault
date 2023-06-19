<script setup lang="ts">
import { ref, onMounted, toRaw } from "vue";
import * as monaco from "monaco-editor";
// import all languages
import "monaco-editor/esm/vs/basic-languages/monaco.contribution";
import { NSelect, NSpace, NButton } from "naive-ui";
import { postMapping } from "../api/request.ts";

const editor = ref<monaco.editor.IStandaloneCodeEditor | null>(null);
const selectedLanguage = ref("python");
const selectedTheme = ref("vs-dark");
const code = ref("");

onMounted(() => {
  initializeEditor();
});

const initializeEditor = () => {
  editor.value = monaco.editor.create(
    // 获取 DOM 节点
    document.getElementById("editorContainer")!,
    {
      value: code.value,
      language: selectedLanguage.value,
      theme: selectedTheme.value,
      readOnly: false,
      selectOnLineNumbers: true, //显示行号
      automaticLayout: true, //自动布局
      autoIndent: "advanced", //自动缩进
      renderLineHighlight: "line", //高亮当前行
      foldingStrategy: "indentation", // 代码可分小段折叠
    }
  );
};

const changeLanguage = () => {
  monaco.editor.setModelLanguage(
    toRaw(editor.value!).getModel()!,
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

// 提交代码，从 editor 中获取代码
const handleSubmit = () => {
  const codeValue = toRaw(editor.value!).getValue();
  const languageValue = selectedLanguage.value;
  // 从url中获取problemID
  const params = new URLSearchParams(window.location.search)
  const problemID = params.get('problemID')
  console.log(codeValue);
  console.log(languageValue);
  console.log(problemID);
  // 提交代码
  postMapping(`problem/${problemID}/codes`, {
    code: codeValue,
    language: languageValue,
  }).then((res) => {
    console.log(res);
  });

};
</script>

<template>
  <div class="code-container">
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

    <div id="editorContainer" class="code-editor"></div>
    
    <n-button 
      attr-type="submit" 
      @click="handleSubmit"
    >提交
  </n-button>
  </div>
</template>

<style scoped>
.code-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.code-editor {
  width: 100%;
  /* 根据浏览器高度自适应 */
  height: calc(100vh - 195px);
  margin-top: 10px;
}
</style>

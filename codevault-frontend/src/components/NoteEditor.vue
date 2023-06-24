<script setup lang="ts">
import { QuillEditor } from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";
import { NButton } from "naive-ui";
import { ref } from "vue";
import { postMapping } from "../api/request";

const note = ref("");

const params = new URLSearchParams(window.location.search)
const problemID = params.get('problemID')
const handleSubmit = () => {
  postMapping(`problems/${problemID}/notes`,{
    noteText: note.value,
  }).then((res) => {
    if (res.data.code === 200) {
      console.log("提交成功");
      alert("提交成功")
    } else {
      console.log("提交失败");
    }
  });
}
</script>

<template>
    <div class="container">
        <QuillEditor
            v-model:content="note"
            toolbar="full"
            theme="snow"
            contentType="html"
            style="height: calc(100vh - 220px);"
        />
        <n-button
            attr-type="submit" 
            @click="handleSubmit"
        >提交
        </n-button>
    </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>
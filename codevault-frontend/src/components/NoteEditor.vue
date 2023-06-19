<script setup lang="ts">
import { QuillEditor } from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";
import { NButton } from "naive-ui";
import { ref } from "vue";

const note = ref("");


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
        <n-button>提交</n-button>
    </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>
<script setup lang="ts">
import { QuillEditor } from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";
import { NButton } from "naive-ui";
import { ref } from "vue";

const note = ref("");


</script>

<template>
    <div>
        <QuillEditor
            v-model:content="note"
            toolbar="full"
            theme="snow"
            contentType="html"
            style="height: 79vh"
        />
        <n-button>提交</n-button>
    </div>
</template>

<style scoped>

</style>
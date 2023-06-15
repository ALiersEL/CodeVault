<script setup lang="ts">
import { ref } from "vue";
import {
  NButton,
  NModal,
  NCard,
}
from "naive-ui";
import FolderFrom from "./FolderForm.vue";

// showModal
const showModal = ref(false);

// handleClose
const handleClose = () => {
  showModal.value = false;
};
</script>

<template>
    <div>
        <n-button 
            type="info" 
            dashed
            @click="showModal = true" 
        > 新建文件夹 
        </n-button>
        <n-modal v-model:show="showModal">
            <n-card
                title="新建文件夹"
                :bordered="false"
                size="huge"
                role="dialog"
                aria-modal="true"
                closable 
                @close="handleClose"
            >
            <FolderFrom @cancel="handleClose"/>
            </n-card>
        </n-modal>
    </div>
</template>

<style scoped>
.n-card {
  width: 550px;
}
</style>
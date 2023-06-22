<script setup lang="ts">
import { defineEmits, computed, ref } from "vue";
import {
  NModal,
  NCard,
  NButton,
  NSpace,
}
from "naive-ui";
import FileTree from "./FileTree.vue";

const props = defineProps({
  showFileTreeModal: {
    type: Boolean,
    required: true,
  },
});
const emit = defineEmits(["update:showFileTreeModal"]);

// showModal
const showFileTreeModal = computed({
  get: () => props.showFileTreeModal,
  set: (value: boolean) => emit("update:showFileTreeModal", value),
});

// handleClose
const handleClose = () => {
  showFileTreeModal.value = false;
};

// handleCancel
const handleCancel = () => {
  showFileTreeModal.value = false;
};

const currentFolderID = ref<number>(-1);

// handleSubmit
const handleSubmit = (folderID: number) => {
  console.log(folderID);
  
  showFileTreeModal.value = false;
};
</script>

<template>
    <div>
        <n-modal v-model:show="showFileTreeModal">
            <n-card
                title="移动到"
                :bordered="false"
                size="huge"
                role="dialog"
                aria-modal="true"
                closable 
                @close="handleClose"
            >
            <FileTree v-model:currentFolderID="currentFolderID" />
            <div style="display: flex; justify-content: flex-end;">
              <n-space>
                <n-button round attr-type="button" @click="handleCancel">
                  取消
                </n-button>
                <n-button round attr-type="submit" @click="handleSubmit(currentFolderID)">
                  移动
                </n-button>
              </n-space>
            </div>
            </n-card>
        </n-modal>
    </div>
</template>

<style scoped>
.n-card {
  width: 550px;
}
</style>
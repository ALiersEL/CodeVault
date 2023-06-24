<script setup lang="ts">
import { computed, ref } from "vue";
import {
  NModal,
  NCard,
  NButton,
  NSpace,
}
from "naive-ui";
import FileTree from "./FileTree.vue";
import { postMapping } from "../api/request";

const props = defineProps({
  showFileTreeModal: {
    type: Boolean,
    required: true,
  },
  id: {
    type: Number,
    required: true,
  },
  type: {
    type: String,
    required: true,
  },
  name : {
    type: String,
    required: true,
  },
  checkedRowKeys: {
    type: Array,
    required: true,
  },
});
const emit = defineEmits(["update:showFileTreeModal"]);

// showModal
const showFileTreeModal = computed({
  get: () => props.showFileTreeModal,
  set: (value: boolean) => emit("update:showFileTreeModal", value),
});

const id = computed({
  get: () => props.id,
  set: () => {},
});

const type = computed({
  get: () => props.type,
  set: () => {},
});

const name = computed({
  get: () => props.name,
  set: () => {},
});

const checkedRowKeys = computed({
  get: () => props.checkedRowKeys,
  set: () => {},
});

const title = computed(() => {
 // 如果checkedRowKeys.value为空, 移动id.value到folderID
  if(checkedRowKeys.value.length === 0) {
    if(type.value === "文件夹") {
      return `移动文件夹 ${name.value} 到`;
    } else {
      return `移动题目 ${name.value} 到`;
    }
  } 
  return `移动选中的 ${checkedRowKeys.value.length} 个文件/文件夹 到`;
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
  // 如果checkedRowKeys.value为空, 移动id.value到folderID
  if(checkedRowKeys.value.length === 0) {
    if(type.value === "文件夹") {
      postMapping(`/folders/${id.value}/move`, { targetFolderID: folderID }).then((res) => {
        console.log(res);
      });
    } else {
      postMapping(`/problems/${id.value}/move`, { targetFolderID: folderID }).then((res) => {
        console.log(res);
      });
    }
    showFileTreeModal.value = false;
    return;
  } 
  const rowKeys = checkedRowKeys.value as string[];
  rowKeys.forEach((key) => {
    // 如果第三个字符是数字，说明是文件, 否则是文件夹
    if(key[2] >= '0' && key[2] <= '9') {
      // id为第三位到最后一位
      const id = parseInt(key.slice(2));
      postMapping(`/problems/${id}/move`, { targetFolderID: folderID }).then((res) => {
        console.log(res);
      });
    } else {
      const id = parseInt(key.slice(3));
      postMapping(`/folders/${id}/move`, { targetFolderID: folderID }).then((res) => {
        console.log(res);
      });
    }
  });
  showFileTreeModal.value = false;
};
</script>

<template>
    <div>
        <n-modal v-model:show="showFileTreeModal">
            <n-card
                :title="title"
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
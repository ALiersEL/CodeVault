<script setup lang="ts">
import { ref } from "vue";
import ProblemFrom from "./ProblemForm.vue";
import {
  NButton,
  NModal,
  NCard,
}
from "naive-ui";

// showModal
const showModal = ref(false);

// handleClose
const handleClose = () => {
  showModal.value = false;
};

</script>

<template>
  <div>
    <n-button @click="showModal = true"> 新增题目 </n-button>
    <n-modal v-model:show="showModal">
      <n-card
        title="新增题目"
        :bordered="false"
        size="huge"
        role="dialog"
        aria-modal="true"
        closable 
        @close="handleClose"
      >
       <ProblemFrom />
      </n-card>
    </n-modal>
  </div>
</template>

<style scoped>
.n-card {
  width: 1000px;
}
</style>

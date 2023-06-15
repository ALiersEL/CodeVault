<script setup lang="ts">
import ProblemDescription from "./ProblemDescription.vue";
import ProblemEdit from "./ProblemEdit.vue";
import { ref } from "vue";
import { Edit } from "@vicons/tabler";
import { NSpace, NButton, NIcon } from "naive-ui";


const isRead = ref(true);

const toggleEdit = () => {
  isRead.value = !isRead.value;
};
</script>

<template>
    <div class="container">
        <!-- 每行一个元素 -->
        <div v-if="isRead">
            <n-space>
                <ProblemDescription />
                <n-button quaternary type="primary"
                    @click="toggleEdit"><n-icon :component="Edit"></n-icon
                ></n-button>
            </n-space>
        </div>
        <div v-else>
            <ProblemEdit @changeMode="toggleEdit"/>
        </div>
    </div>
</template>

<style scoped>
.container {
  width: 100%;
  margin-left: 10px;
  margin-right: 10px;
  margin-top: 10px;
}
</style>
<script setup lang="ts">
import { NSpace, NSelect, NInput, NIcon } from "naive-ui";
import MdSearch from "@vicons/ionicons4/MdSearch";
import Flash16Regular from "@vicons/fluent/Flash16Regular"
import FlashCheckmark16Regular from "@vicons/fluent/FlashCheckmark16Regular";
import { FlashOutline } from '@vicons/ionicons5'
import { ref, onBeforeMount } from "vue";
import { getMapping } from "../api/request";
import emitter from "../utils/bus";

// ref 
const showType = ref(false)
const showDifficulty = ref(false)
const showStatus = ref(false)

// 定义type
type Option = {
  label: string
  value: string
}

// 定义options
const typeOptions = [
  {
    label: "算法题",
    value: "0",
  },
  {
    label: "文字题",
    value: "1",
  },
];
const difficultyOptions = [
  {
    label: "简单",
    value: "0",
  },
  {
    label: "中等",
    value: "1",
  },
  {
    label: "困难",
    value: "2",
  },
];
const statusOptions = [
  {
    label: "未完成",
    value: "0",
  },
  {
    label: "已完成",
    value: "1",
  },
];
const tagOptions = ref<Option[]>([]);

const selectedType = ref<number | null>(null);
const selectedDifficulty = ref<number | null>(null);
const selectedStatus = ref<number | null>(null);
// 定义selectedTags
const selectedTags = ref<string[] | null>(null);
// 定义tagInput
const searchKey = ref<string | null>(null);

const handleSearch = () => {
  console.log( selectedType.value, selectedDifficulty.value, selectedStatus.value, selectedTags.value, searchKey.value);
  emitter.emit("filter", {
    selectedType: selectedType.value,
    selectedDifficulty: selectedDifficulty.value,
    selectedStatus: selectedStatus.value,
    selectedTags: selectedTags.value,
    searchKey: searchKey.value,
  });
};

const getTags = () => {
  getMapping("/users/tags", {}) // 获取标签
    .then((res) => {
      console.log(res);
      // tagOptions的label对应标签名，value对应标签id
      res.data.data.forEach((tag: { name: string; id: number }) => {
        tagOptions.value.push({ label: tag.name, value: tag.id.toString() });
      });
      console.log(tagOptions);
  })
  .catch((err) => {
    console.log(err);
  });
}

onBeforeMount(() => {
  getTags();
});
</script>

<template>
    <div>
        <n-space>
            <n-select 
              v-model:value="selectedType"
              v-model:show="showType"
              :on-update:value="value => { selectedType = value; handleSearch(); }"
              placeholder="类型" 
              :options="typeOptions"
              clearable
            >
                <template #arrow>
                    <transition name="slide-left">
                        <flash-checkmark16-regular v-if="showType" />
                        <flash16-regular v-else />
                    </transition>
                </template>
            </n-select>

            <n-select 
              v-model:value="selectedDifficulty"
              v-model:show="showDifficulty"
              :on-update:value="value => { selectedDifficulty = value; handleSearch(); }"
              placeholder="难度" 
              :options="difficultyOptions"
              clearable
            >
                <template #arrow>
                    <transition name="slide-left">
                        <flash-checkmark16-regular v-if="showDifficulty" />
                        <flash16-regular v-else />
                    </transition>
                </template>
            </n-select>

            <n-select 
              v-model:value="selectedStatus"
              v-model:show="showStatus"
              :on-update:value="value => { selectedStatus = value; handleSearch(); }"
              placeholder="状态" 
              :options="statusOptions"
              clearable
            >
                <template #arrow>
                    <transition name="slide-left">
                        <flash-checkmark16-regular v-if="showStatus" />
                        <flash16-regular v-else />
                    </transition>
                </template>
            </n-select>

            <n-select
                v-model:value="selectedTags"
                :on-update:value="values => { selectedTags= values; handleSearch(); }"
                multiple
                filterable
                placeholder="选择标签"
                :options="tagOptions"
                clearable
            />

            <n-input 
              v-model:value="searchKey"
              placeholder="搜索"
              @keydown="handleSearch"
            >
                <template #prefix>
                    <n-icon :component="FlashOutline" />
                </template>
            </n-input>
        </n-space>
    </div>
</template>

<style scoped>
.n-select {
    width: 150px;
}
:deep(.slide-left-enter-active),
:deep(.slide-left-leave-active) {
  transition: transform 0.3s ease, opacity 0.3s ease;
}

:deep(.slide-left-enter-from),
:deep(.slide-left-leave-to) {
  position: absolute;
  opacity: 0;
}

:deep(.slide-left-enter-from) {
  transform: translateX(-10px);
}

:deep(.slide-left-leave-to) {
  transform: translateX(10px);
}
</style>

<script setup lang="ts">
import { NSpace, NSelect } from "naive-ui";
import MdSearch from "@vicons/ionicons4/MdSearch";
import Flash16Regular from "@vicons/fluent/Flash16Regular"
import FlashCheckmark16Regular from "@vicons/fluent/FlashCheckmark16Regular";
import { ref } from 'vue'

// ref 
const showList = ref(false)
const showType = ref(false)
const showDifficulty = ref(false)
const showStatus = ref(false)


// 定义type
type Option = {
  label: string
  value: string
}

// 定义options
const listOptions: Option[] = [];
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
    value: "简单",
  },
  {
    label: "中等",
    value: "中等",
  },
  {
    label: "困难",
    value: "困难",
  },
];
const statusOptions = [
  {
    label: "未完成",
    value: "未完成",
  },
  {
    label: "已完成",
    value: "已完成",
  },
];
const tagOptions: Option[] = [];

// 定义selectedTags
const selectedTags: string[] = [];
</script>

<template>
    <div>
        <n-space>
            <n-select
                v-model:show="showList"
                filterable
                placeholder="选择题单"
                :options="listOptions"
                clearable
            >
                <template v-if="showList" #arrow>
                    <md-search />
                </template>
            </n-select>

            <n-select 
              v-model:show="showType" 
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
              v-model:show="showDifficulty" 
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
              v-model:show="showStatus" 
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
                multiple
                filterable
                placeholder="选择标签"
                :options="tagOptions"
                clearable
            />
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

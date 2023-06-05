<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  NSpace,
  NDescriptions,
  NDescriptionsItem,
  NRate,
  NTag,
} from "naive-ui";


const cols = ref(1);

const problem = ref({
  problemID: null,
  problemTitle: null,
  problemContent: null,
  problemType: null,
  difficulty: null,
  mastery: 0,
  status: false,
  tags: [] as string[],
  companyName: null,
  departmentName: null,
  postName: null,
});

onMounted(() => {
  // 从后端获取数problem的所有信息，在url中指定problemId
  // getMapping("`/problem/${problemId}`",{}).then((res) => {
  //     problem.value = res.data;
  // });
});


</script>

<template>
  <div>
    <n-descriptions label-placement="top" title="题目描述" :column="cols">
        <n-descriptions-item label="题目标题">
          {{ problem.problemTitle }}
        </n-descriptions-item>
        <n-descriptions-item label="题目类型">
          {{ problem.problemType }}
        </n-descriptions-item>
        <n-descriptions-item label="难度">
          {{ problem.difficulty }}
        </n-descriptions-item>
        <n-descriptions-item label="题目内容">
          <div v-html="problem.problemContent"></div>
        </n-descriptions-item>
        <n-descriptions-item label="掌握程度">
          <n-rate readonly :value="problem.mastery"></n-rate>
        </n-descriptions-item>
        <n-descriptions-item label="完成情况">
        <n-space>
            <n-tag
            :bordered="false"
            :type="problem.status ? 'success' : 'warning'"
            >
            {{ problem.status ? "已完成" : "未完成" }}
            </n-tag>
        </n-space>
        </n-descriptions-item>
        <n-descriptions-item label="标签">
          <n-space>
            <n-descriptions-item v-for="tag in problem.tags" :key="tag">
              <n-tag :bordered="false" type="success"> {{ tag }} </n-tag>
            </n-descriptions-item>
          </n-space>
        </n-descriptions-item>
        <n-descriptions-item label="相关企业">
          <n-tag :bordered="false" type="success"> {{ problem.companyName }} </n-tag>
          <n-tag :bordered="false" type="success"> {{ problem.departmentName }} </n-tag>
          <n-tag :bordered="false" type="success"> {{ problem.postName }} </n-tag>
        </n-descriptions-item>
    </n-descriptions>
  </div>
</template>

<style scoped>
.container {
  margin: 20px;
  width: 40%;
}
</style>

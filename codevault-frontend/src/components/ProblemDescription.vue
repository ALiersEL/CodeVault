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

type option = {
  label: string;
  value: number;
};

type source = {
  company: {
    companyName: string | null,
    companyID: string | null,
  },
  department: {
    departmentName: string | null,
    departmentID: string | null,
  },
  post: {
    postName: string | null,
    postID: string | null,
  },
}

type problemType = {
  problemID: number | null;
  problemTitle: string | null;
  problemContent: string | null;
  problemType: number | null;
  difficulty: number | null;
  mastery: number,
  status: boolean,
  tags: option[],
  sources: source[];
};

const problem = ref<problemType>({
  problemID: null,
  problemTitle: null,
  problemContent: null,
  problemType: null,
  difficulty: null,
  mastery: 0,
  status: false,
  tags: [] as option[],
  sources: [{
    company: {
      companyName: null,
      companyID: null,
    },
    department: {
      departmentName: null,
      departmentID: null,
    },
    post: {
      postName: null,
      postID: null,
    },
  }] as source[]
});

const props = defineProps({
  problem: {
    type: Object,
    required: true
  }
});

onMounted(() => {
  // 从父组件获取problem
  problem.value = (props.problem as problemType);
});

</script>

<template>
  <div>
    <n-descriptions label-placement="top" title="题目描述" :column="cols">
        <n-descriptions-item label="题目标题">
          {{ problem.problemTitle }}
        </n-descriptions-item>
        <n-descriptions-item label="题目类型">
          <n-tag
            :bordered="false"
            type="info"
            >
            {{ problem.problemType ? "文字题" : "算法题" }}
          </n-tag>
        </n-descriptions-item>
        <n-descriptions-item label="难度">
          <n-tag
            :bordered="false"
            :type="problem.difficulty === 0 ? 'success' : problem.difficulty === 1 ? 'warning' : 'error'"
            >
            {{ problem.difficulty === 0 ? "简单" : problem.difficulty === 1 ? "中等" : "困难" }}
          </n-tag>
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
          <div v-for="tag in problem.tags">
              <!-- 如果有标签，则显示标签 -->
              <n-tag v-if="tag.label" :bordered="false" type="success"> {{ tag.label }} </n-tag>
              <!-- 否则显示无标签 -->
              {{ !tag.label ? "无标签" : ""  }}
          </div>
        </n-descriptions-item>
        <n-descriptions-item label="相关企业">
          <n-space>
            <div v-for="source in problem.sources">
              <!-- 如果有企业，则显示企业 -->
              <n-tag v-if="source.company.companyName" :bordered="false" type="success"> {{ source.company.companyName }} </n-tag>
              <!-- 如果有部门，则显示部门 -->
              <n-tag v-if="source.department.departmentName" :bordered="false" type="success"> {{ source.department.departmentName }} </n-tag>
              <!-- 如果有岗位，则显示岗位 -->
              <n-tag v-if="source.post.postName" :bordered="false" type="success"> {{ source.post.postName }} </n-tag>
            </div>
            <!-- 没有来源，显示暂无来源 -->
            {{ !problem.sources[0].company.companyName ? "暂无来源" : ""  }}  
          </n-space>
        </n-descriptions-item>
    </n-descriptions>
  </div>
</template>

<style scoped>
</style>

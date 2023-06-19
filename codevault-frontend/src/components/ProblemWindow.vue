<script setup lang="ts">
import ProblemDescription from "./ProblemDescription.vue";
import ProblemEdit from "./ProblemEdit.vue";
import { ref, onMounted } from "vue";
import { Edit } from "@vicons/tabler";
import { NSpace, NButton, NIcon } from "naive-ui";
import router from "../router";
import { getMapping } from "../api/request";

const isRead = ref(true);

const toggleEdit = () => {
  isRead.value = !isRead.value;
  // 重新获取数据
  getProblem();
};

type option = {
  label: string;
  value: number;
};

type category = {
  categoryName: string;
  categoryID: number;
}

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

const getProblem = () => {
  const problemID = router.currentRoute.value.query.problemID;
  getMapping(`problems/${problemID}`,{}).then((res) => {
    console.log(res.data.data);
    // 将res.data.data中的categories的每个元素中的categoryName和categoryID赋值给problem.tags
    problem.value.tags = res.data.data.categories.map((item: category) => {
      return {
        label: item.categoryName,
        value: item.categoryID,
      };
    });
    // 将res.data.data中的problem的部分属性赋值给problem.value
    const { problemID, problemTitle, problemContent, problemType, difficulty, mastery, status } = res.data.data.problem;
    problem.value.problemID = problemID;
    problem.value.problemTitle = problemTitle;
    problem.value.problemContent = problemContent;
    problem.value.problemType = problemType;
    problem.value.difficulty = difficulty;
    problem.value.mastery = mastery;
    problem.value.status = status;
    // 将res.data.data中的sources的每个元素中的companyName、companyID、departmentName、departmentID、postName、postID赋值给problem.sources
    problem.value.sources = res.data.data.sources.map((item) => {
      return {
        company: {
          companyName: item.companyName,
          companyID: item.companyID,
        },
        department: {
          departmentName: item.departmentName,
          departmentID: item.departmentID,
        },
        post: {
          postName: item.postName,
          postID: item.postID,
        },
      };
    });
});
};

onMounted(() => {
  getProblem();  
});
</script>

<template>
    <div class="container">
        <!-- 每行一个元素 -->
        <div v-if="isRead">
            <n-space>
                <ProblemDescription :problem="problem"/>
                <n-button 
                    quaternary 
                    type="primary"
                    @click="toggleEdit"
                    style="margin-left: 10px;">
                    <n-icon :component="Edit"></n-icon>
                </n-button>
            </n-space>
        </div>
        <div v-else>
            <ProblemEdit :problem="problem" @changeMode="toggleEdit"/>
        </div>
    </div>
</template>

<style scoped>
.container {
  width: 100%;
  margin-left: 10px;
  margin-right: 10px;
  margin-top: 10px;
  height: calc(100vh - 75px);
  overflow: auto;
  scrollbar-width: none; /* Firefox */
}

::-webkit-scrollbar {
  display: none; /* Chrome Safari */
}
</style>
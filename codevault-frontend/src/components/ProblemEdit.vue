<script setup lang="ts">
import { ref, watch, computed, onMounted, nextTick, h, VNodeRef } from "vue";
import {
  NForm,
  NFormItem,
  NInput,
  NRadio,
  NRadioGroup,
  NButton,
  NRadioButton,
  NSpace,
  NRate,
  NAutoComplete,
  NDynamicTags,
  NIcon,
  AutoCompleteInst,
  NSelect,
  NTag,
} from "naive-ui";
import { Add } from "@vicons/ionicons5";
// 导入富文本编辑器quill
import { QuillEditor } from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";
import { putMapping, getMapping } from "../api/request";
import router from "../router";

const formRef = ref<VNodeRef | null>(null);

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
  problemTitle: string | null;
  problemContent: string | null;
  problemType: number | null;
  difficulty: number | null;
  mastery: number,
  status: boolean,
  tags: option[],
  sources: source[];
};

// 从父组件获取problem
const props = defineProps({
  problem: {
    type: Object,
    required: true,
  },
});

// 将props.problem复制一份给problem, 改变problem的值不会改变父组件的值
const problem = ref<problemType>({...props.problem as problemType});

// 只要problem.tags中的tagName, 根据problem.tags动态改变
const tagNames = computed(() => {
  return problem.value.tags.map((tag) => tag.label);
});

// rules
const rules = {
  problemTitle: {
    required: true,
    message: "请输入题目标题",
    trigger: ["blur", "input"],
  },
  problemContent: {
    required: true,
    message: "请输入题目内容",
    trigger: ["blur", "input"],
  },
};

const difficulties = [
  {
    label: "简单",
    value: 0,
  },
  {
    label: "中等",
    value: 1,
  },
  {
    label: "困难",
    value: 2,
  },
];

type option = {
  label: string;
  value: string;
};

// 所有tagOptions中元素的type都为”success“
// 所有tagOptions中元素的type都为”success“
const tagOptionsRef = ref<option[]>([]);
const companyOptionsRef = ref<option[]>([]);
// 二维数组，第一维为来源的序号，第二维为来源的选项
const departmentOptionsRef = ref<option[][]>([]);
const postOptionsRef = ref<option[][]>([]);

// onMounted, 从后端获取当前用户的tagOptions, companyOptions
onMounted(() => {
  getMapping("/users/tags", {}).then((res) => {
    tagOptionsRef.value = res.data.data.map((item: any) => {
      return {
        label: item.name,
        value: item.id
      };
    });
  });
  getMapping("/users/companies", {}).then((res) => {
    console.log(res.data.data);
    companyOptionsRef.value = res.data.data.map((item: any) => {
      return {
        label: item.name,
        value: item.id
      };
    });
  });
  newCompanies.value = problem.value.sources.map((source) => source.company.companyName) as string[];
  newDepartments.value = problem.value.sources.map((source) => source.department.departmentName) as string[];
  newPosts.value = problem.value.sources.map((source) => source.post.postName) as string[];
});

const autoCompleteInstRef = ref<AutoCompleteInst | null>(null);

watch(autoCompleteInstRef, (value) => {
  if (value) nextTick(() => value.focus());
});

const newTag = ref("");
const autoFillTagOptions = computed(() => {
  if (newTag.value === null) {
    return [];
  }
  const prefix = newTag.value;
  // 当前输入的选项在tagOptions存在时，只返回tagOptions中含有输入的内容的选项
  // 当前输入的选项在tagOptions不存在时，返回tagOptions中含有输入的内容的选项以及当前输入的选项, 当前输入的选项为prefix + " (自定义)"
  if (
    tagOptionsRef.value.findIndex((option) => option.label === prefix) !== -1
  ) {
    return tagOptionsRef.value.filter((option) =>
      option.label.includes(prefix)
    );
  } else {
    return [
      ...tagOptionsRef.value.filter((option) => option.label.includes(prefix)),
      {
        label: prefix + " (新增)",
        value: "-1",
      },
    ];
  }
});

const selectTag = (tag: string) => {
  console.log(tag);
  // 如果problem.tags中已经有了这个tag，就不添加, 否则添加
  const existingTagIndex: number = problem.value.tags.findIndex((item) => {
    if (tag !== "-1") {
      return item.value === tag;
    } else {
      return item.label === newTag.value;
    }
  });

  if (existingTagIndex === -1) {
    // 如果tag为-1，label为newTag.value, 否则从tagOptions中找到label
    const nTag: option = tag === "-1" ? {
      label: newTag.value,
      value: tag,
    } : tagOptionsRef.value.find((item) => item.value === tag) as option;
    problem.value.tags.push(nTag);
  }
  console.log(problem.value.tags);
};

// renderTag
const renderTag = (tag: string, index: number) => {
  return h(
    NTag,
    {
      type: "success",
      closable: true,
      onClose: () => {
        problem.value.tags.splice(index, 1);
      },
    },
    {
      default: () => tag,
    }
  );
};


const newCompanies = ref<string[]>([]);
const newDepartments = ref<string[]>([]);
const newPosts = ref<string[]>([]);

const createOption = (label: string) => {
  return {
    label: label,
    value: "-1" + label,
  };
};

const removeSource = (index: number) => {
  problem.value.sources.splice(index, 1);
  newCompanies.value.splice(index, 1);
  newDepartments.value.splice(index, 1);
  newPosts.value.splice(index, 1);
  departmentOptionsRef.value.splice(index, 1);
  postOptionsRef.value.splice(index, 1);
}

const addSource = () => {
  problem.value.sources.push({
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
  });
  console.log(problem.value.sources);
}

const changeCompany = (index: number, value: string) => {
  // 如果value的前两个字符不为-1，就根据value找到对应的companyName和companyID，否则companyName为newCompany, companyID为-1
  if (value.toString().slice(0, 2) !== "-1") {
    const matchingOption = companyOptionsRef.value.find((option) => option.value === value);
    problem.value.sources[index].company.companyName = matchingOption!.label;
    problem.value.sources[index].company.companyID = value;
    // 从后端获取departmentOptions
    getMapping("/users/departments", {
      companyID: value,
    }).then((res) => {
      departmentOptionsRef.value = res.data.data.map((item: any) => {
        return {
          label: item.departmentName,
          value: item.departmentID
        };
      });
    });
  } else {
    problem.value.sources[index].company.companyName = value.toString().slice(2);
    problem.value.sources[index].company.companyID = "-1";
  }
}

const changeDepartment = (index: number, value: string) => {
 // 如果value不为-1，就根据value找到对应的departmentName和departmentID，否则departmentName为newDepartment, departmentID为-1
 if (value.toString().slice(0, 2) !== "-1") {
    const matchingOption = departmentOptionsRef.value[index].find((option) => option.value === value);
    problem.value.sources[index].department.departmentName = matchingOption!.label;
    problem.value.sources[index].department.departmentID = value;
    // 从后端获取postOptions
    getMapping("/users/posts", {
      departmentID: value,
    }).then((res) => {
      postOptionsRef.value = res.data.data.map((item: any) => {
        return {
          label: item.postName,
          value: item.postID
        };
      });
    });
  } else {
    problem.value.sources[index].department.departmentName = value.toString().slice(2);
    problem.value.sources[index].department.departmentID = "-1";
  }
}

const changePost = (index: number, value: string) => {
  // 如果value不为-1，就根据value找到对应的postName和postID，否则postName为newPost, postID为-1
  if (value.toString().slice(0, 2) !== "-1") {
    const matchingOption = postOptionsRef.value[index].find((option) => option.value === value);
    problem.value.sources[index].post.postName = matchingOption!.label;
    problem.value.sources[index].post.postID = value;
  } else {
    problem.value.sources[index].post.postName = value.toString().slice(2);
    problem.value.sources[index].post.postID = "-1";
  }
}

const handleSubmit = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate((errors) => {
    if (!errors) {
      //message.success('验证成功')
    } else {
      console.log(errors);
      //message.error('验证失败')
    }
  });

  if (problem.value.problemTitle === null) {
    alert("请输入题目标题");
    return;
  }
  if (problem.value.problemContent === null) {
    alert("请输入题目内容");
    return;
  }
  console.log(problem);

  const problemID = router.currentRoute.value.query.problemID;
  // 将problem穿到后端
  putMapping(`/problems/${problemID}`, problem.value)
  .then((res) => {
    console.log(res);
    if (res.data.code === 200) {
      alert("修改成功");
      handleCancel();
      
      // 重置表单
      formRef.value?.restoreValidation();
      // 重置problem
      problem.value = {
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
        }] as source[],
      };
      // 重置newTag
      newTag.value = "";
    } else {
      alert("修改失败");
    }
  });
};

const emit = defineEmits(["changeMode"]); // 定义emit, 用于向父组件传递事件
const handleCancel = () => {
  emit("changeMode");
};
</script>

<template>
  <div>
    <!-- dialog form, dialog appear from the top, a dialog with scrollable content -->
    <n-space vertical>
      <n-form
        :ref="formRef"
        :model="problem"
        :rules="rules"
        label-placement="left"
        :label-width="160"
      >
        <n-form-item label="题目标题" path="problemTitle">
          <n-input v-model:value="problem.problemTitle" style="width: 80%"/>
        </n-form-item>

        <n-form-item label="题目描述" path="problemContent">
          <div>
            <QuillEditor
              v-model:content="problem.problemContent"
              toolbar="full"
              theme="snow"
              contentType="html"
            />
          </div>
        </n-form-item>

        <n-form-item label="题目类型" path="problemType">
          <n-radio-group v-model:value="problem.problemType" name="problemType">
            <n-radio :value="0"> 算法题 </n-radio>
            <n-radio :value="1"> 文字题 </n-radio>
          </n-radio-group>
        </n-form-item>

        <n-form-item label="难度" path="difficulty">
          <n-radio-group v-model:value="problem.difficulty" name="difficulty">
            <n-radio-button
              v-for="item in difficulties"
              :key="item.value"
              :value="item.value"
              :label="item.label"
            >
            </n-radio-button>
          </n-radio-group>
        </n-form-item>

        <n-form-item label="掌握程度" path="mastery">
          <n-rate v-model:value="problem.mastery" name="mastery" />
        </n-form-item>

        <n-form-item label="完成情况" path="status">
          <n-radio-group v-model:value="problem.status" name="status">
            <n-radio :value="false"> 未完成 </n-radio>
            <n-radio :value="true"> 已完成 </n-radio>
          </n-radio-group>
        </n-form-item>

        <n-form-item label="类别" path="tags">
          <n-dynamic-tags v-model:value="tagNames" :render-tag="renderTag">
            <template #input="{ deactivate }">
              <n-auto-complete
                ref="autoCompleteInstRef"
                v-model:value="newTag"
                size="small"
                :options="autoFillTagOptions"
                placeholder="类别"
                :clear-after-select="true"
                @select="selectTag($event as string)"
                @blur="deactivate"
              />
            </template>
            <template #trigger="{ activate, disabled }">
              <n-button
                size="small"
                type="primary"
                dashed
                :disabled="disabled"
                @click="activate()"
              >
                <template #icon>
                  <n-icon>
                    <Add />
                  </n-icon>
                </template>
                添加
              </n-button>
            </template>
          </n-dynamic-tags>
        </n-form-item>

        <n-form-item  
          v-for="(item, index) in problem.sources" 
          :key="index"
          :label="`题目来源${index + 1}`" 
          :path="`sources.${index}`"
        >
          <n-form-item>
            <span style="width: 42px; margin-left: 15px; margin-right: 10px;">公司</span>
            <n-select
              v-model:value="newCompanies[index]"
              filterable
              tag
              placeholder="选择公司"
              :options="companyOptionsRef"
              @update:value="changeCompany(index, $event)"
              @create="createOption"
            />
          </n-form-item>
          <n-form-item>
            <span class="source-label">部门</span>
            <n-select
              v-model:value="newDepartments[index]"
              filterable
              tag
              placeholder="选择部门"
              :options="departmentOptionsRef[index]"
              :disabled="!newCompanies[index]"
              @update:value="changeDepartment(index, $event)"
              @create="createOption"
            />
          </n-form-item>
          <n-form-item>
            <span class="source-label">岗位</span>
            <n-select
              v-model:value="newPosts[index]"
              filterable
              tag
              placeholder="选择岗位"
              :options="postOptionsRef[index]"
              :disabled="!newDepartments[index]"
              @update:value="changePost(index, $event)"
              @create="createOption"
            />
          </n-form-item>
          <n-button style="margin-left: 12px;top: -10px;" @click="removeSource(index)">
            删除
          </n-button>
        </n-form-item>
        <n-form-item>
            <n-button attr-type="button" style="left: 80px;" @click="addSource">
              增加来源
            </n-button>
        </n-form-item>

        <div style="display: flex; justify-content: flex-end;">
          <n-space>
            <n-button round attr-type="button" @click="handleCancel">
              取消
            </n-button>
            <n-button round attr-type="submit" @click="handleSubmit">
              修改
            </n-button>
          </n-space>
        </div>
      </n-form>
    </n-space>
  </div>
</template>

<style scoped>
.n-space {
  padding-right:  80px;
  padding-left: -70px;
}
.source-label {
  width: 50px;
  margin-left: 10px;
  text-align: left;
}
</style>

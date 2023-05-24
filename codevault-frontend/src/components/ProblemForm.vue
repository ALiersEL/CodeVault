<script setup lang="ts">
import { ref, watch, computed, onMounted, onUpdated, nextTick, h } from "vue";
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
// 导入富文本编辑器quill
import QuillEditor from "../components/QuillEditor.vue";

// rules
const rules = {
  problemTitle: [
    {
      required: true,
      message: "请输入题目标题",
      trigger: "blur",
    },
  ],
  problemContent: [
    {
      required: true,
      message: "请输入题目描述",
      trigger: "blur",
    },
  ],
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

// 所有tagOptions中元素的type都为”success“
const tagOptionsRef = ref([
  {
    label: "动态规划",
    value: "动态规划",
  },
  {
    label: "贪心算法",
    value: "贪心算法",
  },
  {
    label: "回溯算法",
    value: "回溯算法",
  },
]);

const companyOptionsRef = ref([
  {
    label: "字节跳动", 
    value: "字节跳动",
  },
  {
    label: "阿里巴巴", 
    value: "阿里巴巴",
  },
  {
    label: "腾讯", 
    value: "腾讯",
  },
  {
    label: "百度", 
    value: "百度",
  },
  {
    label: "美团", 
    value: "美团",
  },
  {
    label: "京东", 
    value: "京东",
  },
  {
    label: "华为", 
    value: "华为",
  },
  {
    label: "小米", 
    value: "小米",
  },
]);

type option = {
  label: string;
  value: string;
};


// departmentOptions类型为option[]，用于存储当前用户的部门信息
const departmentOptionsRef = ref<option[]>([]);
const postOptionsRef = ref<option[]>([]);

// onMounted, 从后端获取当前用户的tagOptions, companyOptions
onMounted(() => {
  // console.log("mounted");
  // console.log(tagOptionsRef.value);
  console.log(departmentOptionsRef.value);
});

onUpdated(() => {});

// string类型的数组，用于存储新添加的tag
const newTags = ref<string[]>([]);

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
  // console.log(prefix);
  // 当前输入的选项在tagOptions存在时，只返回tagOptions中含有输入的内容的选项
  // 当前输入的选项在tagOptions不存在时，返回tagOptions中含有输入的内容的选项以及当前输入的选项, 当前输入的选项为prefix + " (自定义)"
  if (tagOptionsRef.value.findIndex((option) => option.value === prefix) !== -1) {
    return tagOptionsRef.value.filter((option) => option.value.includes(prefix));
  } else {
    return [
      ...tagOptionsRef.value.filter((option) => option.value.includes(prefix)),
      {
        label: prefix + " (自定义)",
        value: prefix,
      },
    ];
  }
});

const selectTag = (tag: string) => {
  console.log(tag);
  // 如果problem.tags中已经有了这个tag，就不添加, 否则添加
  if (problem.value.tags.indexOf(tag) === -1) {
    problem.value.tags.push(tag);
  }
  console.log(problem.value.tags);
  console.log(tagOptionsRef.value);
};

// renderTag
const renderTag = (tag: string, index: number) => {
  return h(
    NTag,
    {
      type:  "success",
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

const problem = ref({
  problemTitle: "",
  problemContent: "",
  problemType: 0,
  difficulty: 0,
  mastery: 0,
  status: 0,
  tags: [] as string[],
  companyName: "",
  departmentName: "",
  postName: "",
});

const handleSubmit = () => {
  // newTags为在problem.tags中存在，但是在tagOptions中不存在的tag
  // 将这些tag赋值给newTags
  newTags.value = problem.value.tags.filter((tag) => {
    return tagOptionsRef.value.findIndex((option) => option.value === tag) === -1;
  });
  console.log(newTags.value);

  console.log(problem.value);
};
</script>

<template>
  <!-- dialog form, dialog appear from the top, a dialog with scrollable content -->
  <n-space vertical>
    <n-form
      :model="problem"
      :rule="rules"
      label-placement="left"
      :label-width="160"
      :style="{
        maxWidth: '1000px',
      }"
    >
      <n-form-item label="题目标题" path="problemTitle" class="item">
        <n-input
          v-model:value="problem.problemTitle"
          placeholder="Input"
          class="input"
        />
      </n-form-item>

      <n-form-item label="题目描述" path="problemContent" >
        <!-- <n-input
          v-model:value="problem.problemContent"
          placeholder="Textarea"
          type="textarea"
          :autosize="{
            minRows: 3,
            maxRows: 5,
          }"
        /> -->
        <div>
          <QuillEditor />
        </div>
      </n-form-item>

      <n-form-item label="题目类型" path="problemType">
        <n-radio-group v-model:value="problem.problemType" name="problemType">
          <n-radio value="0"> 算法题 </n-radio>
          <n-radio value="1"> 文字题 </n-radio>
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
          <n-radio value="0"> 未完成 </n-radio>
          <n-radio value="1"> 已完成 </n-radio>
        </n-radio-group>
      </n-form-item>

      <n-form-item label="类别" path="tags">
        <n-dynamic-tags v-model:value="problem.tags" :render-tag="renderTag">
          <template #input="{ deactivate }">
            <n-auto-complete
              ref="autoCompleteInstRef"
              v-model:value="newTag"
              size="small"
              :options="autoFillTagOptions"
              placeholder="类别"
              :clear-after-select="true"
              @select="selectTag($event)"
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

      <n-form-item label="题目来源">
        <n-form-item>
          <span style="width: 42px; margin-left: 2px">公司</span>
          <n-select
            v-model:value="problem.companyName"
            filterable
            tag
            placeholder="Select"
            :options="companyOptionsRef"
          />
        </n-form-item>
        <n-form-item>
          <span class="source-label">部门</span>
          <n-select
            v-model:value="problem.departmentName"
            filterable
            tag
            placeholder="Select"
            :options="departmentOptionsRef"
            :disabled="!problem.companyName"
          />
        </n-form-item>
        <n-form-item>
          <span class="source-label">岗位</span>
          <n-select
            v-model:value="problem.postName"
            filterable
            tag
            placeholder="Select"
            :options="postOptionsRef"
            :disabled="!problem.departmentName"
          />
        </n-form-item>
      </n-form-item>

      <div style="display: flex; justify-content: flex-end">
        <n-button round attr-type="submit" @click="handleSubmit">
          提交
        </n-button>
      </div>
    </n-form>
  </n-space>
</template>

<style scoped>
.source-label {
  width: 50px;
  margin-left: 10px;
  text-align: left;
}
</style>

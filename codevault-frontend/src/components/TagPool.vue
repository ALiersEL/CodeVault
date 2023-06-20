<script setup lang="ts">
import { ref, h, onMounted } from "vue";
import { NCard, NDynamicTags, NTag, NMessageProvider, NInput, NSpace } from "naive-ui";
import ConfirmModal from "./ConfirmModal.vue";
import { getMapping, postMapping, putMapping, deleteMapping } from "../api/request.ts";

// 标签名，标签id，使用次数
type Tag = {
  name: string;
  id: number;
  count: number;
};

const fullTags = ref<Tag[]>([]);
const tags = ref<string[]>(fullTags.value.map((tag) => tag.name));
const editIndex = ref(-1);
const deleteIndex = ref(-1);

const renderTag = (tag: string) => {
  // 点击可修改标签
  if (editIndex.value === tags.value.indexOf(tag)) {
    return h(NInput, {
      value: tag,
      size: "small",
      autofocus: true,
      // 根据内容自动调整宽度
      autosize: { minRows: 1, maxRows: 1 },
      onUpdateValue: (value: string) => {
        const index = tags.value.indexOf(tag);
        tags.value[index] = value;
        // 同时更新fullTags中的标签
        fullTags.value[index].name = value;
        // 获取标签id
        const tagID = fullTags.value[index].id;
        // 发送请求
        putMapping(`users/tags/${tagID}`, { name: value }).then((res) => {
          console.log(res);
        });
        router.go(0);
      },
      onChange: () => {
        editIndex.value = -1;
      },
      onBlur: () => {
        editIndex.value = -1;
      },
    });
  }
  
  return h(
    NTag,
    {
      type: "success",
      closable: true,
      onClose: () => {
        const toDelete = tags.value.indexOf(tag);
        // tag的count是否为0，为0则直接删除，否则警告用户
        if (fullTags.value[toDelete].count !== 0) {
          alert("该标签仍有使用记录，无法删除");
          return;
        }
        showConfirmModal.value = true;
        deleteIndex.value = toDelete;
      },
      onClick: () => {
        editIndex.value = tags.value.indexOf(tag);
      },
    },
    {
      default: () => tag,
    }
  );
};

const showConfirmModal = ref(false);
const showConfirmModal2 = ref(false);

const createTag = (label: string) => {
  console.log('create tag');
  // 判断是否已存在该标签
  if (tags.value.includes(label)) {
    alert("该标签已存在");
    return;
  }
  // 发送请求
  postMapping("users/tags", { name: label }).then((res) => {
    console.log(res);
    // 获取标签id
    const tagID = res.data.data;
    console.log(tagID);
    // 更新fullTags
    fullTags.value.push({
      name: label,
      id: tagID,
      count: 0,
    });
    tags.value = fullTags.value.map((tag) => tag.name);
  });
};

const deleteTag = () => {
  console.log(fullTags.value[deleteIndex.value]);
  tags.value = tags.value.filter((_, index) => index !== deleteIndex.value);
  const tagID = fullTags.value[deleteIndex.value].id;
  // 同时删除fullTags中的标签
  fullTags.value = fullTags.value.filter((_, index) => index !== deleteIndex.value);
  console.log(tagID)
  // 发送请求
  deleteMapping(`users/tags/${tagID}`, {}).then((res) => {
    console.log(res);
  });
  deleteIndex.value = -1;
};

const cancelDeleteTag = () => {
  console.log("cancel delete tag");
};

const updateTags = (value: string[]) => {
  // 去重
  tags.value = Array.from(new Set(value));
};

onMounted(() => {
  getMapping("/users/tags", {}) // 获取标签
    .then((res) => {
      console.log(res);
      fullTags.value = res.data.data;
      tags.value = res.data.data.map((tag) => tag.name);
    })
    .catch((err) => {
      console.log(err);
    });
    getMapping("/users/companies", {}) // 获取公司
    .then((res) => {
      console.log(res);
      fullCompanies.value = res.data.data;
      companies.value = res.data.data.map((company) => company.name);
    })
    .catch((err) => {
      console.log(err);
    });
});

// 公司名，公司id，使用次数
type Company = {
  name: string;
  id: number;
  count: number;
};

const fullCompanies = ref<Company[]>([]);
const companies = ref<string[]>(fullCompanies.value.map((company) => company.name));
const editIndexCompany = ref(-1);
const deleteIndexCompany = ref(-1);

const renderCompany = (company: string) => {
  // 点击可修改公司
  if (editIndexCompany.value === companies.value.indexOf(company)) {
    return h(NInput, {
      value: company,
      size: "small",
      autofocus: true,
      // 根据内容自动调整宽度
      autosize: { minRows: 1, maxRows: 1 },
      onUpdateValue: (value: string) => {
        const index = companies.value.indexOf(company);
        companies.value[index] = value;
        // 同时更新fullCompanies中的公司
        fullCompanies.value[index].name = value;
        // 获取公司id
        const companyID = fullCompanies.value[index].id;
        // 发送请求
        putMapping(`users/companies/${companyID}`, { name: value }).then((res) => {
          console.log(res);
        });
      },
      onChange: () => {
        editIndexCompany.value = -1;
      },
      onBlur: () => {
        editIndexCompany.value = -1;
      },
    });
  }
  
  return h(
    NTag,
    {
      type: "info",
      closable: true,
      onClose: () => {
        const toDelete = companies.value.indexOf(company);
        // company的count是否为0，为0则直接删除，否则警告用户
        if (fullCompanies.value[toDelete].count !== 0) {
          alert("该公司仍有使用记录，无法删除");
          return;
        }
        showConfirmModal2.value = true;
        deleteIndexCompany.value = toDelete;
      },
      onClick: () => {
        editIndexCompany.value = companies.value.indexOf(company);
      },
    },
    {
      default: () => company,
    }
  );
};

const createCompany = (label: string) => {
  console.log('create company');
  // 判断是否已存在该公司
  if (companies.value.includes(label)) {
    alert("该公司已存在");
    return;
  }
  // 发送请求
  postMapping("users/companies", { name: label }).then((res) => {
    console.log(res);
    // 获取公司id
    const companyID = res.data.data;
    console.log(companyID);
    // 更新fullCompanies
    fullCompanies.value.push({
      name: label,
      id: companyID,
      count: 0,
    });
    companies.value = fullCompanies.value.map((company) => company.name);
  });
};

const deleteCompany = () => {
  console.log(fullCompanies.value[deleteIndexCompany.value]);
  companies.value = companies.value.filter((_, index) => index !== deleteIndexCompany.value);
  const companyID = fullCompanies.value[deleteIndexCompany.value].id;
  // 同时删除fullCompanies中的公司
  fullCompanies.value = fullCompanies.value.filter((_, index) => index !== deleteIndexCompany.value);
  console.log(companyID)
  // 发送请求
  deleteMapping(`users/companies/${companyID}`, {}).then((res) => {
    console.log(res);
  });
  deleteIndexCompany.value = -1;
};

const cancelDeleteCompany = () => {
  console.log("cancel delete company");
};

const updateCompanies = (value: string[]) => {
  // 去重
  companies.value = Array.from(new Set(value));
};
</script>

<template>
    <div class="container">
        <n-space vertical>
          <n-card title="所有标签">
              <n-dynamic-tags 
                  v-model:value="tags" 
                  :render-tag="renderTag"
                  :on-update:value="updateTags"
                  @create="createTag"
              />
              <n-message-provider>
                  <ConfirmModal
                      v-model:showConfirmModal="showConfirmModal"
                      :promptMessage="'确定要删除标签 ' + tags[deleteIndex] + ' 吗？'"
                      @confirmed="deleteTag"
                      @canceled="cancelDeleteTag"
                  />
              </n-message-provider>
          </n-card>
          <n-card title="所有公司">
            <n-dynamic-tags 
                  v-model:value="companies" 
                  :render-tag="renderCompany"
                  :on-update:value="updateCompanies"
                  @create="createCompany"
              />
              <n-message-provider>
                  <ConfirmModal
                      v-model:showConfirmModal="showConfirmModal2"
                      :promptMessage="'确定要删除标签 ' + companies[deleteIndexCompany] + ' 吗？'"
                      @confirmed="deleteCompany"
                      @canceled="cancelDeleteCompany"
                  />
              </n-message-provider>
          </n-card>
        </n-space>
    </div>
</template>

<style scoped>
.container {
  width: 350px;
}
</style>

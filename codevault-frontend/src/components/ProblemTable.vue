<script setup lang="ts">
import { h } from "vue";
import { NSpace, NDataTable, NTag, NButton, NMessageProvider } from "naive-ui";
import type { DataTableColumns } from "naive-ui";
import { onMounted, ref } from "vue";
import router from "../router";
import { getMapping, deleteMapping } from "../api/request";
import ConfirmModal from "./ConfirmModal.vue";
// 导入moment.js
import moment from "moment";

type RowData = {
  key: number
  status: string
  problemTitle: string
  problemType: string
  difficulty: string
  lastModified: string
  tags: string[]
}

const showConfirmModal = ref(false);
const rowToDelete = ref<RowData>();

const createColumns = (): DataTableColumns<RowData> => [
    {
      title: "状态",
      key: "status"
    },
    {
      title: "题目标题",
      key: "problemTitle",
      // problemTitle在hover变蓝色，点击后跳转到题目详情页
      render (row) {
        return h(
          "a",
          {
            // hover
            style: 'cursor: pointer;color: #096dd9;',
            // hover
            onClick: () => {
              router.push(
                {
                  path: '/problems',
                  query: {
                    problemID: row.key.toString()
                  }
                }
              )
            },
          },
          { 
            default: () => row.problemTitle
          }
        )
      }
    },
    {
      title: "题目类型",
      key: "problemType",
    },
    {
      title: "难度",
      key: "difficulty",
    },
    {
      title: "类别",
      key: "tags",
      render (row) {
        console.log(row.tags);
        // 如果没有标签(只有一个null)，显示“暂无标签"(仅显示文字)
        if (row.tags?.length === 1 && row.tags[0] === null) {
          return h(
            "span",
            {
              style: {
                color: '#aaa'
              }
            },
            {
              default: () => '暂无标签'
            }
          )
        }
        const tags = row.tags?.map((tagKey) => {
          return h(
            NTag,
            {
              style: {
                marginRight: '6px'
              },
              type: 'info',
              bordered: false
            },
            {
              default: () => tagKey
            }
          )
        })
        return tags
      }
    },
    {
      title: "修改时间",
      key: "lastModified",
    },
    {
      title: "操作",
      key: 'actions',
      render (row) {
        return h(
          NButton,
          {
            size: 'small',
            onClick: () => {
              showConfirmModal.value = true;
              rowToDelete.value = row;
            }
          },
          { default: () => '删除' }
        )
      }
    }
]

const deleteProblem =  () => {
  const problemID = rowToDelete.value?.key;
  deleteMapping(`/problems/${problemID}`, {}).then((res) => {
    console.log(res);
  });
}

const cancelDeleteProblem = () => {
  console.log('cancel');
}

const data = ref<RowData[]>([]);
const columns = createColumns();
const pagination = {
    pageSize: 10
}

onMounted(() => {
    // 从后端获取数据
    getMapping("/users/problemset", {}).then((res) => {
        console.log(res);
        // 将res.data.data中的每个元素中的problemID，改为key，赋值给data
        data.value.push(...res.data.data.map((item) => {
          return {
            key: item.problemID,
            status: item.status ? '已完成' : '未完成',
            problemTitle: item.problemTitle,
            problemType: item.problemType ? '文字题' : '算法题',
            difficulty: item.difficulty === 0 ? '简单' : item.difficulty === 1 ? '中等' : '困难',
            lastModified: moment(item.lastModified).format('YYYY-MM-DD HH:mm:ss'),
            tags: item.tags
          }
        }))
    });
});

</script>

<template>
    <div class="container">
        <n-space vertical :size="12">
            <n-data-table
            :bordered="false"
            :columns="columns"
            :data="data"
            :pagination="pagination"
            />
            <n-message-provider>
              <ConfirmModal 
                v-model:showConfirmModal="showConfirmModal"
                :promptMessage="'确定要删除题目 ' + rowToDelete?.problemTitle + ' 吗？'"
                @confirmed="deleteProblem"
                @canceled="cancelDeleteProblem"
              />
            </n-message-provider>
        </n-space>
    </div>
</template>

<style scoped>
.container {
  width: 1200px;
}

:deep(.hover) {
  color: #1890ff;
}
</style>
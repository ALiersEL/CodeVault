<script setup lang="ts">
import { h } from "vue";
import { NSpace, NDataTable, NTag, NButton } from "naive-ui";
import type { DataTableColumns } from "naive-ui";
import { onMounted } from "vue";
import router from "../router";
import { getMapping, deleteMapping } from "../api/request";

type RowData = {
  key: number
  status: string
  problemTitle: string
  problemType: string
  difficulty: string
  tags: string[]
}

const createColumns = ({
  deleteProblem
}: {
  deleteProblem: (rowData: RowData) => void
}): DataTableColumns<RowData> => [
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
                  path: 'problem',
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
        const tags = row.tags.map((tagKey) => {
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
      title: "操作",
      key: 'actions',
      render (row) {
        return h(
          NButton,
          {
            size: 'small',
            onClick: () => deleteProblem(row)
          },
          { default: () => '删除' }
        )
      }
    }
]

const createData = (): RowData[] => [
  {
    key: 0,
    status: "Accepted",
    problemTitle: "A + B Problem",
    problemType: "Algorithmic",
    difficulty: "easy",
    tags: ['nice', 'developer']
  },
  {
    key: 1,
    status: "Accepted",
    problemTitle: "A + B Problem",
    problemType: "Algorithmic",
    difficulty: "easy",
    tags: ['nice', 'developer']
  },
  {
    key: 2,
    status: "Accepted",
    problemTitle: "A + B Problem",
    problemType: "Algorithmic",
    difficulty: "easy",
    tags: ['nice', 'developer']
  }
]

const data = createData();
const columns = createColumns({
    deleteProblem (rowData) {
        alert(`Deleting ${rowData.problemTitle}`);
        const problemID = rowData.key;
        deleteMapping((`problems/${problemID}`), {}).then((res) => {
            console.log(res);
        });
    }
});
const pagination = {
    pageSize: 10
}

onMounted(() => {
    console.log('mounted');
    // 从后端获取数据
    getMapping("/users/problemset", {}).then((res) => {
        console.log(res);
    });
});

</script>

<template>
    <div>
        <n-space vertical :size="12">
            <n-data-table
            :bordered="false"
            :columns="columns"
            :data="data"
            :pagination="pagination"
            />
        </n-space>
    </div>
</template>

<style scoped>
:deep(.hover) {
  color: #1890ff;
}
</style>
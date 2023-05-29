<script setup lang="ts">
import { h } from "vue";
import { NSpace, NDataTable, NTag, NButton } from "naive-ui";
import type { DataTableColumns } from "naive-ui";
import { onMounted } from "vue";
import { getMapping } from "../api/request";

type RowData = {
  key: number
  problemTitle: string
  problemType: string
  difficulty: string
  tags: string[]
}

const createColumns = ({
  sendMail
}: {
  sendMail: (rowData: RowData) => void
}): DataTableColumns<RowData> => {
  return [
    {
      title: "题目标题",
      key: "problemTitle",
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
            onClick: () => sendMail(row)
          },
          { default: () => 'Send Email' }
        )
      }
    }
  ]
}

const createData = (): RowData[] => [
  {
    key: 0,
    problemTitle: "A + B Problem",
    problemType: "Algorithmic",
    difficulty: "easy",
    tags: ['nice', 'developer']
  },
  {
    key: 1,
    problemTitle: "A + B Problem",
    problemType: "Algorithmic",
    difficulty: "easy",
    tags: ['nice', 'developer']
  },
  {
    key: 2,
    problemTitle: "A + B Problem",
    problemType: "Algorithmic",
    difficulty: "easy",
    tags: ['nice', 'developer']
  }
]

const data = createData();
const columns = createColumns({
    sendMail (rowData) {
        alert(`Send email to ${rowData.problemTitle}`);
    }
});
const pagination = {
    pageSize: 10
}

onMounted(() => {
    console.log('mounted');
    // 从后端获取数据
    // getMapping("/problem/getAll").then((res) => {
    //     console.log(res);
    // });
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
</style>
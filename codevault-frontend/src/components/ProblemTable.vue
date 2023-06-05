<script setup lang="ts">
import { h } from "vue";
import { NSpace, NDataTable, NTag, NButton } from "naive-ui";
import type { DataTableColumns } from "naive-ui";
import { onMounted } from "vue";
import router from "../router";
import { getMapping } from "../api/request";

type RowData = {
  key: number
  status: string
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
      title: "状态",
      key: "status"
    },
    {
      title: "题目标题",
      key: "problemTitle",
      // problemTitle在hover变蓝色，点击后跳转到题目详情页
      render (row) {
        return h(
          'div',
          {
            style: 'cursor: pointer;',
            // hover
            onClick: () => {
              router.push(`/problem/${row.problemID}`)
            }
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

// rowClassName
const rowClassName = (row: RowData) => {
    if (row.selected = true) {
        return "hover";
    }
    return "";
};
</script>

<template>
    <div>
        <n-space vertical :size="12">
            <n-data-table
            :bordered="false"
            :columns="columns"
            :data="data"
            :pagination="pagination"
            :row-class-name="rowClassName"
            />
        </n-space>
    </div>
</template>

<style scoped>
:deep(.hover) {
  color: #1890ff;
}
</style>
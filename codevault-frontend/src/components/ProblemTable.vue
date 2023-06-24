<script setup lang="ts">
import { h } from "vue";
import { NSpace, NDataTable, NTag, NRate, NButton, NMessageProvider, NPagination } from "naive-ui";
import type { DataTableColumns, DataTableSortState } from "naive-ui";
import { onMounted, ref, reactive } from "vue";
import router from "../router";
import { getMapping, deleteMapping, postMapping } from "../api/request";
import ConfirmModal from "./ConfirmModal.vue";
// 导入moment.js
import moment from "moment";
import emitter from "../utils/bus";

type RowData = {
  key: number
  status: string
  problemTitle: string
  problemType: string
  difficulty: string
  lastModified: string
  tags: string[]
  mastery: number
}

const showConfirmModal = ref(false);
const rowToDelete = ref<RowData>();
const isFiltered = ref(false);
const selectedType = ref<number | null>(null);
const selectedDifficulty = ref<number | null>(null);
const selectedStatus = ref<number | null>(null);
const selectedTags = ref<string[] | null>(null);
const searchKey = ref<string | null>(null);

const createColumns = (): DataTableColumns<RowData> => [
    {
      title: "状态",
      key: "status",
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
      },
      sorter: 'default'
    },
    {
      title: "题目类型",
      key: "problemType",
    },
    {
      title: "难度",
      key: "difficulty",
      // 简单 < 中等 < 困难
      sorter: (a, b) => {
        const difficultyMap = {
          简单: 1,
          中等: 2,
          困难: 3
        }
        return difficultyMap[a.difficulty] - difficultyMap[b.difficulty]
      }
    },
    {
      title: "类别",
      key: "tags",
      render (row) {
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
      title: "掌握程度",
      key: "mastery",
      // 使用NRate组件
      render (row) {
        return h(
          NRate,
          {
            value: row.mastery,
            readonly: true,
          }
        )
      },
      sorter: 'default'
    },
    {
      title: "修改时间",
      key: "lastModified",
      defaultSortOrder: 'descend',
      sorter: 'default',
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
  // data.value中也删除
  data.value = data.value.filter((item) => {
    return item.key !== problemID;
  });
}

const cancelDeleteProblem = () => {
  console.log('cancel');
}

const data = ref<RowData[]>([]);
const columns = createColumns();

const paginationReactive = reactive({
    page: 1,
    pageSize: 10,
    pageSizes: [10, 20, 30, 50],
    itemCount: 1000,//总条目数
    onUpdatePage: (page: number) => {//切换第几页时
      paginationReactive.page = page;
      // 如果没有筛选条件，直接获取全部数据，否则获取筛选后的数据
      refetchData();
    },
    onUpdatePageSize: (pageSize: number) => {
      paginationReactive.pageSize = pageSize;
      paginationReactive.page = 1;
      // 如果没有筛选条件，直接获取全部数据，否则获取筛选后的数据
      refetchData();
    }
});

const getFilteredData = (selectedOptions: any) => {
  // 如果没有选择任何筛选条件，直接返回全部数据，设置变量isFiltered为false,否则为true
  isFiltered.value = true;
  if (Object.keys(selectedOptions).length === 0) {
    isFiltered.value = false;
    return data.value;
  }
  selectedType.value = selectedOptions['selectedType'];
  selectedDifficulty.value = selectedOptions['selectedDifficulty'];
  selectedStatus.value = selectedOptions['selectedStatus'];
  selectedTags.value = selectedOptions['selectedTags'];
  searchKey.value = selectedOptions['searchKey'];
  // console.log(selectedType.value, selectedDifficulty.value, selectedStatus.value, selectedTags.value, searchKey.value);
  postMapping("/users/problemset/filtered", {
    selectedType: selectedOptions['selectedType'],
    selectedDifficulty: selectedOptions['selectedDifficulty'],
    selectedStatus: selectedOptions['selectedStatus'],
    selectedTags: selectedOptions['selectedTags'],
    searchKey: selectedOptions['searchKey'],
    problemTitleSort: sorterReactive.problemTitle,
    difficultySort: sorterReactive.difficulty,
    masterySort: sorterReactive.mastery,
    lastModifiedSort: sorterReactive.lastModified,
    page: paginationReactive.page,
    pageSize: paginationReactive.pageSize
  }).then((res) => {
    console.log(res);
    // 将res.data.data中的每个元素中的problemID，改为key，赋值给data
    data.value = res.data.data.data.map((item) => {
      return {
        key: item.problemID,
        status: item.status ? '已完成' : '未完成',
        problemTitle: item.problemTitle,
        problemType: item.problemType ? '文字题' : '算法题',
        difficulty: item.difficulty === 0 ? '简单' : item.difficulty === 1 ? '中等' : '困难',
        lastModified: moment(item.lastModified).format('YYYY-MM-DD HH:mm:ss'),
        tags: item.tags,
        mastery: item.mastery
      }
    })
  });
}

const refetchData = () => {
  console.log("重新获取中...")
 // 重新获取数据， 如果没有筛选条件，直接获取全部数据，否则获取筛选后的数据
  if (!isFiltered.value) {
    getProblems(paginationReactive.page, paginationReactive.pageSize);
  } else {
    getFilteredData({
      selectedType: selectedType.value,
      selectedDifficulty: selectedDifficulty.value,
      selectedStatus: selectedStatus.value,
      selectedTags: selectedTags.value,
      searchKey: searchKey.value
    });
  }
}

const getProblems = (page: number, pageSize: number) => {
  // 从后端获取数据
  getMapping("/users/problemset", {
    page: page, 
    pageSize: pageSize,
    problemTitleSort: sorterReactive.problemTitle,
    difficultySort: sorterReactive.difficulty,
    masterySort: sorterReactive.mastery,
    lastModifiedSort: sorterReactive.lastModified
  }).then((res) => {
        console.log(res);
        // 将res.data.data中的每个元素中的problemID，改为key，赋值给data
        data.value = (res.data.data.map((item) => {
          return {
            key: item.problemID,
            status: item.status ? '已完成' : '未完成',
            problemTitle: item.problemTitle,
            problemType: item.problemType ? '文字题' : '算法题',
            difficulty: item.difficulty === 0 ? '简单' : item.difficulty === 1 ? '中等' : '困难',
            lastModified: moment(item.lastModified).format('YYYY-MM-DD HH:mm:ss'),
            tags: item.tags,
            mastery: item.mastery
          }
        }))
    });
}

type SortState = {
  problemTitle: 0 | 1 | 2,
  difficulty: 0 | 1 | 2,
  mastery: 0 | 1 | 2,
  lastModified: 0 | 1 | 2
}
// state，problemTitle，difficulty，mastery, lastModified的排序状态,
const sorterReactive = reactive<SortState>({
  problemTitle: 0,
  difficulty: 0,
  mastery: 0,
  lastModified: 0
})
const onUpdateSorter = (sorter: DataTableSortState) => {
  console.log(sorter);
  // 0: 无序，1：升序，2：降序
  const { columnKey, order } = sorter;
  const value = order === 'ascend' ? 1 : order === 'descend' ? 2 : 0;
  // 如果value不为0，则将其他的排序状态置为0
  if (value !== 0) {
    for (const key in sorterReactive) {
      if (key !== columnKey) {
        sorterReactive[key as keyof SortState] = 0;
      }
    }
  }
  sorterReactive[columnKey as keyof SortState] = value;
  // 如果没有筛选条件，直接获取全部数据，否则获取筛选后的数据
  refetchData();
}

emitter.on('filter', getFilteredData)
emitter.on('refetchData', refetchData)
onMounted(() => {
  getMapping("/users/problemset/count", {}).then((res) => {
    console.log(res);
    paginationReactive.itemCount = res.data.data;
  });
  getProblems(paginationReactive.page, paginationReactive.pageSize);
});

</script>

<template>
    <div class="container">
        <n-space vertical :size="12">
            <n-data-table
            :bordered="false"
            :columns="columns"
            :data="data"
            :on-update:sorter="onUpdateSorter"
            />
            <n-pagination
              :page="paginationReactive.page"
              :page-size="paginationReactive.pageSize"
              :page-sizes="paginationReactive.pageSizes"
              :item-count="paginationReactive.itemCount"
              show-size-picker
              show-quick-jumper
              :on-update:page-size="paginationReactive.onUpdatePageSize"
              :on-update:page="paginationReactive.onUpdatePage"
              style="margin-top: 12px; justify-content: flex-end;"
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
<script setup lang="ts">
import { onMounted } from "vue";
import { getMapping } from "../api/request";
import router from "../router";
import { NDataTable } from "naive-ui";
import type { DataTableColumns } from "naive-ui";

type RowData = {
  key: number
  content: string
  language: number
  last_modified: string
}

const createColumns = (): DataTableColumns<RowData> => {
  return [
    {
      title: '内容',
      key: 'content'
    },
    {
      title: '语言',
      key: 'language'
    },
    {
      title: '修改时间',
      key: 'last_modified'
    }
  ]
}

const createData = (): RowData[] => [
];


onMounted(() => {
    const problemID = router.currentRoute.value.query.problemID;
    getMapping(`problems/${problemID}/codes`,{}).then((res) => {
        console.log(res);
    });
});

</script>

<template>
    <div>
        <NDataTable :columns="createColumns()" :data="createData()" striped />
    </div>
</template>

<style scoped>

</style>
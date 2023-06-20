<script setup lang="ts">
import { onMounted, ref, h } from "vue";
import { getMapping } from "../api/request";
import router from "../router";
import { NDataTable } from "naive-ui";
import type { DataTableColumns } from "naive-ui";
import moment from "moment";

type RowData = {
  key: number
  content: string
  lastModified: string
}

const createColumns = (): DataTableColumns<RowData> => {
  return [
    {
      title: '内容',
      key: 'content',
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
                  path: '/problems/notes',
                  query: {
                    noteID: row.key.toString()
                  }
                }
              )
            },
          },
          { 
            default: () => row.content
          }
        )
      }
    },
    {
      title: '修改时间',
      key: 'lastModified'
    }
  ]
}

const createData = (): RowData[] => [];
const data = ref<RowData[]>(createData());


onMounted(() => {
    const problemID = router.currentRoute.value.query.problemID;
    getMapping(`problems/${problemID}/notes`,{}).then((res) => {
        console.log(res);
        // key对应noteID, content对应noteText, lastModified对应lastModified 
        data.value = res.data.data.map((item: any) => {
            return {
                key: item.noteID,
                content: item.noteText.slice(0, 20),
                lastModified: moment(item.lastModified).format('YYYY-MM-DD HH:mm:ss')
            }
        })
    });
});

</script>

<template>
    <div>
        <NDataTable :columns="createColumns()" :data="data" striped />
    </div>
</template>

<style scoped>

</style>
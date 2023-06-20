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
  language: number
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
                  path: '/problems/codes',
                  query: {
                    codeID: row.key.toString()
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
      title: '语言',
      key: 'language'
    },
    {
      title: '修改时间',
      key: 'lastModified'
    }
  ]
}

const createData = (): RowData[] => [];

const data = ref<RowData[]>(createData());

// string数组
const languageOptions = ['python', 'cpp', 'csharp', 'go', 'java', 'javascript', 'typescript', 'rust', 'sql', 'swift', 'kotlin', 'lua', 'perl', 'ruby', 'scala', 'php', 'r', 'objective-c', 'erlang', 'elixir', 'dart', 'apex', 'markdown'];

onMounted(() => {
    const problemID = router.currentRoute.value.query.problemID;
    getMapping(`problems/${problemID}/codes`,{}).then((res) => {
        // key对应codeID, content对应codeText, language对应codeLanguage, lastModified对应lastModified
        data.value = res.data.data.map((item: any) => {
            return {
                key: item.codeID,
                content: item.codeText.slice(0, 20),
                language: languageOptions[item.codeLanguage],
                lastModified: moment(item.lastModified).format('YYYY-MM-DD HH:mm:ss')
            }
        });

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
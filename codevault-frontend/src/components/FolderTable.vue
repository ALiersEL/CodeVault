<script setup lang="ts">
import { NDataTable, NIcon, NDropdown, DropdownOption, NMessageProvider } from "naive-ui";
import type { DataTableColumns } from "naive-ui";
import { Folder20Filled } from "@vicons/fluent";
import { FileAltRegular } from "@vicons/fa";
import { h, ref, nextTick } from "vue";
import router from "../router";
import FileTreeModal from "./FileTreeModal.vue";
import ConfirmModal from "./ConfirmModal.vue";

type RowData = {
  key: number
  name: string
  type: string
  dateModified: string
}

const createColumns = (): DataTableColumns<RowData> => [
  {
    type: "selection",
    fixed: "left",
  },
  {
    title: "名称",
    key: "name",
    // 如果row,name是文件夹, 前面加一个文件夹图标，否则不加
    render(row) {
        if ((row.type === "文件夹")) {
            return h(
              "div", [
                h(NIcon, { component: Folder20Filled }),
                h("span", 
                { 
                  style: 'cursor: pointer;color: #096dd9;',
                  onclick : () => {
                    router.push(
                      {
                        path: 'folder',
                        query: {
                          folderID: row.key.toString()
                        }
                      }
                    )
                  }
                },
                " " + row["name"],),
            ],);
        }
        return h("div", [
            h(NIcon, { component: FileAltRegular }),
            h("span", 
            { 
              // 绿色
              style: 'cursor: pointer;color: #5ac38c;',
              onclick : () => {
                router.push(
                  {
                    path: 'problem',
                    query: {
                      problemID: row.key.toString()
                    }
                  }
                )
              }
            },
            " " + row["name"] ),
        ]
        );
    }
  },
  {
    title: "类型",
    key: "type",
  },
  {
    title: "修改时间",
    key: "dateModified",
  },
];

const createData = (): RowData[] => [
  {
    key: 1,
    name: "文件夹1",
    type: "文件夹",
    dateModified: "2021-10-10",
  },
  {
    key: 2,
    name: "文件夹2",
    type: "文件夹",
    dateModified: "2021-10-10",
  },
  {
    key: 3,
    name: "文件3",
    type: "文件",
    dateModified: "2021-10-10",
  },
];

const options: DropdownOption[] = [
  {
    label: '重命名',
    key: 'rename'
  },
  {
    label: '移动到...',
    key: 'move'
  },
  {
    label: () => h('span', { style: { color: 'red' } }, '删除'),
    key: 'delete'
  }
]

const showDropdown = ref(false);
const showFileTreeModal = ref(false);
const showConfirmModal = ref(false);
const x = ref(0);
const y = ref(0);
const id = ref(0);
const type = ref("");

const onClickoutside = () => {
  showDropdown.value = false;
};

const handleSelect = (id: number, type: string, key: string) => {
  console.log(id, type, key);
  if(key === 'rename') {
    console.log('rename');
  } else if(key === 'move') {
    showFileTreeModal.value = true;
    console.log('move');
  } else if(key === 'delete') {
    showConfirmModal.value = true;
    console.log('delete');
  }
  showDropdown.value = false;
};

const rowProps = (row: RowData) => {
  return {
    onContextmenu: (e: MouseEvent) => {
      e.preventDefault();
      showDropdown.value = false;
      nextTick().then(() => {
        x.value = e.clientX;
        y.value = e.clientY;
        showDropdown.value = true;
        id.value = row.key;
        type.value = row.type;
      });
    },
  };
};

</script>

<template>
  <div>
    <n-data-table
      :columns="createColumns()"
      :data="createData()"
      :row-props="rowProps"
    />
    <n-dropdown
      placement="bottom-start"
      trigger="manual"
      :x="x"
      :y="y"
      :options="options"
      :show="showDropdown"
      :on-clickoutside="onClickoutside"
      @select="handleSelect(id, type, $event)"
    />
    <FileTreeModal v-model:showFileTreeModal="showFileTreeModal"/>
    <n-message-provider>
      <ConfirmModal v-model:showConfirmModal="showConfirmModal"/>
    </n-message-provider>
  </div>
</template>

<style scoped></style>

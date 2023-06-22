<script setup lang="ts">
import { NDataTable, NIcon, NDropdown, DropdownOption, NMessageProvider } from "naive-ui";
import type { DataTableColumns, DataTableRowKey } from "naive-ui";
import { Folder20Filled } from "@vicons/fluent";
import { FileAltRegular } from "@vicons/fa";
import {h, ref, nextTick, onUpdated, inject } from "vue";
import router from "../router";
import FileTreeModal from "./FileTreeModal.vue";
import ConfirmModal from "./ConfirmModal.vue";
import { deleteMapping } from "../api/request";

const promptMessage = ref<string>("");
type RowData = {
  id: number
  name: string
  type: string
  lastModified: string
}

const createColumns = (): DataTableColumns<RowData> => [
  {
    type: "selection",
    fixed: "left",
  },
  {
    title: "名称",
    key: "name",
    // 如果row,name是文件夹, 前面加一个文件夹图标，否则加一个文件图标
    render(row) {
        if ((row.type === "文件夹")) {
            return h(
              "div", [
                h(NIcon, { component: Folder20Filled }),
                h("span", 
                { 
                  style: 'cursor: pointer;color: #096dd9;',
                  onclick : () => {
                    // 现有的路由加上/文件夹名
                    router.push(
                      {
                        name: "folders",
                        query: {
                          path: router.currentRoute.value.query.path + "/" + row["name"]
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
                    path: "/problems",
                    query: {
                      problemID: row.id.toString()
                    }
                  }
                )
              }
            },
            " " + row["name"] ),
        ]
        );
    },
    sorter: 'default'
  },
  {
    title: "类型",
    key: "type",
    sorter: 'default'
  },
  {
    title: "修改时间",
    key: "lastModified",
    sorter: 'default'
  },
];

const data = ref<RowData[]>([]);

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

const pagination = {
    pageSize: 10
}

const showDropdown = ref(false);
const showFileTreeModal = ref(false);
const showConfirmModal = ref(false);
const x = ref(0);
const y = ref(0);
const id = ref(0);
const type = ref("");
const name = ref("");

const onClickoutside = () => {
  showDropdown.value = false;
};

const handleSelect = ( key: string) => {
  if(key === 'rename') {
    console.log('rename');
  } else if(key === 'move') {
    showFileTreeModal.value = true;
    console.log('move');
  } else if(key === 'delete') {
    // 如果checkedRowKeys.value为空, promptMessage.value为"确定要删除type.value name.value的文件吗？"
    if(checkedRowKeysRef.value.length === 0) {
      promptMessage.value = `确定要删除${type.value}${name.value}吗？`;
    } else {
      // 否则, promptMessage.value为"确定要删除选中的checkedRowKeys.value.length个文件吗？"
      promptMessage.value = `确定要删除选中的${checkedRowKeysRef.value.length}个文件吗？`;
    }
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
        id.value = row.id;
        type.value = row.type;
        name.value = row.name;
      });
    },
  };
};

const props = defineProps({
  data: {
    type: Object,
    required: true
  }
});

onUpdated(() => {
  data.value = props.data as RowData[];
});

const checkedRowKeysRef = ref<DataTableRowKey[]>([]);

const handleCheck = (keys: DataTableRowKey[]) => {
  checkedRowKeysRef.value = keys;
};

const getFolderContent = inject("getFolderContent") as () => void;

const deleteFile = () => {
  // 如果checkedRowKeys.value为空, 删除id为id.value, type为type.value的文件
  console.log(checkedRowKeysRef.value);
  if(checkedRowKeysRef.value.length === 0) {
    if(type.value === "文件夹") {
      deleteMapping(`/folders/${id.value}`, {}).then((res) => {
        console.log(res);
      });
    } else {
      deleteMapping(`/problems/${id.value}`, {}).then((res) => {
        console.log(res);
      });
    }
    getFolderContent();
    showConfirmModal.value = false;
    return;
  }
  // 从checkedRowKeys.value提取出id和type，删除每一个
  checkedRowKeysRef.value.forEach((key) => {
    key = key as string;
    // 如果第三个字符是数字，说明是文件, 否则是文件夹
    if(key[2] >= '0' && key[2] <= '9') {
      // id为第三位到最后一位
      const id = parseInt(key.slice(2));
      deleteMapping(`/problems/${id}`, {}).then((res) => {
        console.log(res);
      });
    } else {
      const id = parseInt(key.slice(3));
      deleteMapping(`/folders/${id}`, {}).then((res) => {
        console.log(res);
      });
    }
  });
  // 重新渲染文件夹内容
  getFolderContent();
  showConfirmModal.value = false;
};

const cancelDeleteFile = () => {
  showConfirmModal.value = false;
};
</script>

<template>
  <div>
    <n-data-table
      :columns="createColumns()"
      :data="data"
      :row-props="rowProps"
      :row-key = "row => row.type + row.id"
      @update-checked-row-keys="handleCheck"
      :pagination="pagination"
    />
    <n-dropdown
      placement="bottom-start"
      trigger="manual"
      :x="x"
      :y="y"
      :options="options"
      :show="showDropdown"
      :on-clickoutside="onClickoutside"
      @select="handleSelect($event)"
    />
    <FileTreeModal v-model:showFileTreeModal="showFileTreeModal"/>
    <n-message-provider>
      <ConfirmModal 
        v-model:showConfirmModal="showConfirmModal"
        :prompt-message="promptMessage"
        @confirmed="deleteFile"
        @canceled="cancelDeleteFile"
      />
    </n-message-provider>
  </div>
</template>

<style scoped></style>

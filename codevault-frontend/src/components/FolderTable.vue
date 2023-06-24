<script setup lang="ts">
import { NDataTable, NIcon, NDropdown, DropdownOption, NMessageProvider } from "naive-ui";
import type { DataTableColumns, DataTableRowKey } from "naive-ui";
import { Folder20Filled } from "@vicons/fluent";
import { FileAltRegular } from "@vicons/fa";
import {h, ref, nextTick, onUpdated, inject } from "vue";
import router from "../router";
import FileTreeModal from "./FileTreeModal.vue";
import ConfirmModal from "./ConfirmModal.vue";
import { deleteMapping, putMapping } from "../api/request";

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
    render(row) {
        // 如果row.id和editID.value相等, 显示一个input框, 否则显示一个span
        if(row.id === editID.value) {
          // 如果row.type是文件夹, 显示一个文件夹图标, 否则显示一个文件图标
          if(row.type === "文件夹") {
            return h("div", [
              h(NIcon, { component: Folder20Filled }),
              h("input", { 
                value: row["name"],
                style: { width: (row["name"].length + 1) + "ch" },
                // 调用focus()方法, 使input框获得焦点
                onVnodeMounted: (vnode) => {
                  (vnode.el as HTMLInputElement).focus();
                },
                onInput: (e: InputEvent) => {
                  // 将输入的值赋给row.name
                  row["name"] = (e.target as HTMLInputElement).value;
                },
                onKeydown: (e: KeyboardEvent) => {
                  // 如果按下的是回车键, 将name.value作为新的文件夹名, 发送请求
                  if(e.key === "Enter") {
                    // 发送请求
                    editID.value = 0;
                    // folders/${row.id}/name
                    putMapping(`/folders/${row.id}/name`, {
                      newName: row["name"],
                      newPath: router.currentRoute.value.query.path + "/" + row["name"]
                    })
                    .then((res) => {
                      console.log(res);
                    })
                  }
                },
                // 鼠标移除input框时, 将editID.value置为0
                onBlur: () => {
                  editID.value = 0;
                },
              }),
            ]);
          }
          return h("div", [
            h(NIcon, { component: FileAltRegular }),
            h("input", {
              value: row["name"],
              style: { width: (row["name"].length + 1) + "ch" },
              onVnodeMounted: (vnode) => {
                  (vnode.el as HTMLInputElement).focus();
              },
              onInput: (e: InputEvent) => {
                // 将输入的值赋给row.name
                row["name"] = (e.target as HTMLInputElement).value;
              },
              onKeydown: (e: KeyboardEvent) => {
                // 如果按下的是回车键, 将name.value作为新的文件名, 发送请求
                if(e.key === "Enter") {
                  // 发送请求
                  editID.value = 0;
                  // problems/${row.id}/name
                  putMapping(`/problems/${row.id}/name`, {
                    newName: row["name"]
                  })
                  .then((res) => {
                    console.log(res);
                  })
                }
              },
              // 鼠标移除input框时, 将editID.value置为0
              onBlur: () => {
                editID.value = 0;
              },
            }),
          ]);
        } 
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
    pageSize: 20,
}

const showDropdown = ref(false);
const showFileTreeModal = ref(false);
const showConfirmModal = ref(false);
const x = ref(0);
const y = ref(0);
const id = ref(0);
const type = ref("");
const name = ref("");
const editID = ref(0);

const onClickoutside = () => {
  showDropdown.value = false;
};

const handleSelect = ( key: string) => {
  if(key === 'rename') {
    editID.value = id.value;
    console.log('rename' + ' ' + editID.value);
  } else if(key === 'move') {
    showFileTreeModal.value = true;
    console.log('move');
  } else if(key === 'delete') {
    // 如果checkedRowKeys.value为空, promptMessage.value为"确定要删除type.value name.value的文件吗？"
    if(checkedRowKeysRef.value.length === 0) {
      promptMessage.value = `确定要删除${type.value} ${name.value} 吗？`;
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
  // data.value中也删除
  console.log(checkedRowKeysRef.value);
  if(checkedRowKeysRef.value.length === 0) {
    if(type.value === "文件夹") {
      deleteMapping(`/folders/${id.value}`, {}).then((res) => {
        console.log(res);
      });
      data.value = data.value.filter((item) => item.id !== id.value);
    } else {
      deleteMapping(`/problems/${id.value}`, {}).then((res) => {
        console.log(res);
      });
      data.value = data.value.filter((item) => item.id !== id.value);
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
      data.value = data.value.filter((item) => item.id !== id);
    } else {
      const id = parseInt(key.slice(3));
      deleteMapping(`/folders/${id}`, {}).then((res) => {
        console.log(res);
      });
      data.value = data.value.filter((item) => item.id !== id);
    }
  });
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
    <FileTreeModal 
        v-model:showFileTreeModal="showFileTreeModal"
        :id="id"
        :type="type"
        :name="name"
        :checkedRowKeys="checkedRowKeysRef"
    />
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

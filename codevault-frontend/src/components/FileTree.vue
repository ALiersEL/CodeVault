<script setup lang="ts">
import { h, onMounted, ref, computed } from 'vue'
import { NTree, NIcon, TreeOption } from 'naive-ui'
import {
    Folder,
    FolderOpenOutline,
} from '@vicons/ionicons5'
import { getMapping } from '../api/request'

const props = defineProps({
  currentFolderID: {
    type: Number,
    required: true,
  },
});
const emit = defineEmits(["update:currentFolderID"]);

const currentFolderID = computed({
  get: () => props.currentFolderID,
  set: (value: number) => emit("update:currentFolderID", value),
});

const updatePrefixWithExpaned = (
    _keys: Array<string | number>,
    _option: Array<TreeOption | null>,
    meta: {
        node: TreeOption | null
        action: 'expand' | 'collapse' | 'filter'
    }
) => {
    if (!meta.node) return
    switch (meta.action) {
        case 'expand':
        meta.node.prefix = () =>
            h(NIcon, null, {
            default: () => h(FolderOpenOutline)
            })
        break
        case 'collapse':
        meta.node.prefix = () =>
            h(NIcon, null, {
            default: () => h(Folder)
            })
        break
    }
};

const nodeProps = ({ option }: { option: TreeOption }) => {
    return {
        onClick () {
            // 点击文件夹，获取文件夹下的文件
            // 转换成number类型
            currentFolderID.value = Number(option.key);
            getFolders(Number(option.key)).then(res => {
                if (res === 0) {
                    // 没有文件夹，不展开
                    option.expanded = false;
                }
                else if (res > 0) {
                    // 有文件夹，展开
                    option.expanded = true;
                }
                else {
                    // 错误
                    console.log("Error");
                }
            })
        }
    }
};

type FileTree = {
    key: number
    label: string
    prefix?: () => ReturnType<typeof h>
    children?: FileTree[]
}
const data = ref<FileTree[]>([])

// 类型为TreeOption中的key
const getFolders = async (folderID: number): Promise<number> => {
  try {
    const response = await getMapping("/folders/simple", { folderID: folderID });
    if (response.data.code === 200) {
       // 用bfs在data.value中找到folderID对应的key，将其children增加为res.data.data, 其余内容不变
       // folderID对应key, folderName对应label，prefix为图标
        const queue: any[] = [...data.value]; // 创建data.value的副本以用作初始队列
        while (queue.length > 0) {
        const currentItem = queue.shift(); // 从队列中出列第一个item

        if (currentItem.key === folderID) {
            // 如果当前文件夹的key和folderID相同，将其children增加为res.data.data
            // 如果res.data.data为空数组，删除该folderID的children属性
            if (response.data.data.length === 0) {
                delete currentItem.children;
            }
            else {
                currentItem.children = response.data.data.map((folderItem: any) => {
                return {
                    key: folderItem.folderID,
                    label: folderItem.folderName,
                    prefix: () => h(NIcon, null, {
                    default: () => h(Folder),
                    }),
                    children: []
                };
                });
            }
            // 对于当前文件夹的子文件夹，递归调用getFolders
            await Promise.all(currentItem.children.map((child: any) => getFolders(child.key)));
            break; // 一旦找到并修改了item，就停止BFS
        }

        // 对于当前文件夹的每个子文件夹，将其加入队列
        if (currentItem.children && currentItem.children.length > 0) {
            queue.push(...currentItem.children);
        }
        }
        return response.data.data.length;
    } else {
        console.log(response.data.msg);
        return -1;
    }
  } catch (error) {
        console.error(error);
        return -1;
  }
};


onMounted(() => {
    // 获取文件夹, requestParams为folderID
    getMapping("/folders/simple", {folderID: 1}).then(res => {
        if (res.data.code === 200) {
            // folderID对应key, folderName对应label，prefix为图标
            data.value = res.data.data.map((item: any) => {
                return {
                    key: item.folderID,
                    label: item.folderName,
                    prefix: () =>
                        h(NIcon, null, {
                        default: () => h(Folder),
                        }),
                    children: []
                }
            })
            // 对于每个文件夹，获取其children
            Promise.all(data.value.map((child: any) => getFolders(child.key)));
        }
        else {
            console.log(res.data.msg);
        }
    })
});
</script>

<template>
    <n-tree
      block-line
      expand-on-click
      :data="data"
      :node-props="nodeProps"
      :on-update:expanded-keys="updatePrefixWithExpaned"
    />
</template>

<style scoped>
</style>
  
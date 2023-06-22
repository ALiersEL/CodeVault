<script setup lang="ts">
import { NSpace } from "naive-ui";
import NavigationPanel from "../components/NavigationPanel.vue";
import BreadCrumb from "../components/BreadCrumb.vue";
import FolderModal from "../components/FolderModal.vue";
import FolderTable from "../components/FolderTable.vue";
import { getMapping } from "../api/request";
import { ref, onMounted, provide } from "vue";
import router from "../router";
import moment from "moment";
import ProblemModal from "../components/ProblemModal.vue";

type RowData = {
  id: number
  name: string
  type: string
  lastModified: string
}

const data = ref<RowData[]>([]);

const getFolderContent = () => {
  getMapping("/folders", { path: router.currentRoute.value.query.path }).then((res) => {
    if (res.status === 200) {
      console.log("success");
      data.value = res.data.data.map((item: any) => {
        return {
          id: item.id,
          name: item.name,
          type: item.type,
          lastModified: moment(item.lastModified).format("YYYY-MM-DD HH:mm:ss"),
        };
      });
    }
    else {
      console.log("error");
    }
  });
};

// provide getFolderContent function to children
provide("getFolderContent", getFolderContent);
// provide folderNames to children， dynamic injection
const folderNames = () => {
  return data.value.filter(item => item.type === "文件夹").map(item => item.name)
};
provide("folderNames", folderNames);

onMounted(() => {
  getFolderContent();
});

</script>

<template>
    <div>
        <n-space vertical>
            <NavigationPanel />
            <n-space vertical style="margin-left: 50px;margin-right: 50px;margin-top: 20px;">
              <BreadCrumb />
              <FolderModal />
              <ProblemModal />
              <FolderTable :data="data"/>
            </n-space>
        </n-space>
    </div>
</template>

<style scoped>
</style>
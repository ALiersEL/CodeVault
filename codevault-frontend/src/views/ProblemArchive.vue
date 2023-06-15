<script setup lang="ts">
import { NSpace } from "naive-ui";
import NavigationPanel from "../components/NavigationPanel.vue";
import BreadCrumb from "../components/BreadCrumb.vue";
import FolderModal from "../components/FolderModal.vue";
import FolderTable from "../components/FolderTable.vue";

</script>

<template>
    <div>
        <n-space vertical>
            <NavigationPanel />
            <BreadCrumb />
            <FolderModal />
            <FolderTable />
        </n-space>
    </div>
</template>

<style scoped>
</style>
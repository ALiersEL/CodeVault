<script setup lang="ts">
import { NSpace } from "naive-ui";
import NavigationPanel from "../components/NavigationPanel.vue";
import FilterPanel from "../components/FilterPanel.vue";
import ProblemModal from "../components/ProblemModal.vue";
import ProblemTable from "../components/ProblemTable.vue";
import TagPool from "../components/TagPool.vue";
</script>

<template>
    <div>
        <NavigationPanel />
        <n-space vertical style="margin-left: 50px;margin-right: 50px;margin-top: 20px;">
            <n-space>
                <FilterPanel />
            </n-space>
            <ProblemModal />
            <n-space justify="space-between">
                <ProblemTable />
                <TagPool />
            </n-space>
        </n-space>
    </div>
</template>

<style scoped>
</style>
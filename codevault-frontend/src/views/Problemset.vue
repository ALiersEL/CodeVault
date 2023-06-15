<script setup lang="ts">
import { NSpace } from "naive-ui";
import NavigationPanel from "../components/NavigationPanel.vue";
import FilterPanel from "../components/FilterPanel.vue";
import SearchBox from "../components/SearchBox.vue";
import ProblemModal from "../components/ProblemModal.vue";
import ProblemTable from "../components/ProblemTable.vue";
</script>

<template>
    <div>
        <NavigationPanel />
        <n-space vertical>
            <n-space>
                <FilterPanel />
                <SearchBox />
            </n-space>
            <ProblemModal />
            <ProblemTable />
        </n-space>
    </div>
</template>

<style scoped>
</style>
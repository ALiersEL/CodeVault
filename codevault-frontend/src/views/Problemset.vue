<script setup lang="ts">
import NavigationPanel from "../components/NavigationPanel.vue";
import FilterPanel from "../components/FilterPanel.vue";
import ProblemModal from "../components/ProblemModal.vue";
import ProblemTable from "../components/ProblemTable.vue";
</script>

<template>
    <div>
        <NavigationPanel />
        <FilterPanel />
        <ProblemModal />
        <ProblemTable />
    </div>
</template>

<style scoped>
</style>
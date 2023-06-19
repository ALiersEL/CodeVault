<script setup lang="ts">
import NavigationPanel from "../components/NavigationPanel.vue";
import ProblemWindow from "../components/ProblemWindow.vue";
import Tabs from "../components/Tabs.vue";
import { onMounted } from "vue";
import { Splitpanes, Pane } from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'
</script>

<template>
    <div>
        <NavigationPanel />
        <splitpanes class="default-theme">
            <pane>
                <ProblemWindow/>
            </pane>
            <pane>
                <Tabs />
            </pane>
        </splitpanes>
    </div>
</template>

<style scoped>
.splitpanes__pane {
  display: relative;
  font-family: Helvetica, Arial, sans-serif;
}
</style>
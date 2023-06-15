<script setup lang="ts">
import NavigationPanel from "../components/NavigationPanel.vue";
import ProblemWindow from "../components/ProblemWindow.vue";
import Tabs from "../components/Tabs.vue";
import { onMounted } from "vue";
import { getMapping } from "../api/request";
import router from "../router";
import { Splitpanes, Pane } from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'

onMounted(() => {
   const problemID = router.currentRoute.value.query.problemID;
    getMapping(`problem/${problemID}`,{}).then((res) => {
        console.log(res);
    });
});
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
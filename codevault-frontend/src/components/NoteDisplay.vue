<script setup lang="ts">
import { ref, onMounted } from "vue";
import { getMapping } from "../api/request";
const note = ref(null);


onMounted(() => {
    const params = new URLSearchParams(window.location.search)
    const problemID = params.get('problemID');
    const noteID = params.get('noteID');
    getMapping(`problem/${problemID}/notes/${noteID}`,{}).then((res) => {
        console.log(res);
    });
});

</script>

<template>
    <div>
        <div v-html="note">
        </div>
    </div>
</template>

<style scoped>

</style>
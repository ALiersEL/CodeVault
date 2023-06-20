<script setup lang="ts">
import { ref, onMounted } from "vue";
import { getMapping } from "../api/request";
const note = ref(null);

onMounted(() => {
    console.log("mounted");
    const params = new URLSearchParams(window.location.search)
    const noteID = params.get('noteID');
    getMapping(`/problems/notes/${noteID}`,{}).then((res) => {
        console.log(res);
        note.value = res.data.data.noteText;
    });
});

</script>

<template>
    <div>
        <div v-html="note" class="center">
        </div>
    </div>
</template>

<style scoped>
.center {
    text-align: center;
}

</style>
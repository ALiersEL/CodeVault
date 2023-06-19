<script setup lang="ts">
import { NBreadcrumb, NBreadcrumbItem } from "naive-ui";
import router from "../router";
import { ref, watch } from "vue";

const breadcrumbList = ref<string[]>([]);
const initBreadcrumbList = () => {
  breadcrumbList.value = router.currentRoute.value.query.path!.toString().split("/");
  console.log(breadcrumbList.value);
};

watch(
  () => router.currentRoute,
  () => {
    initBreadcrumbList();
  },
  { deep: true, immediate: true }
);

const handleRedirect = (index: number) => {
  const path = breadcrumbList.value.slice(0, index + 1).join("/");
  console.log(path);
  router.push({
    name: "folder",
    query: {
      path: path
    },
  });
  breadcrumbList.value = path;
};
</script>

<template>
  <div>
    <n-breadcrumb>
      <n-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index">
        <span class="no-redirect" v-if="index === breadcrumbList.length - 1">
          {{ (`${item}`) }}
        </span>
        <span class="redirect" v-else @click="handleRedirect(index)">
          {{ (`${item}`) }}
        </span>
      </n-breadcrumb-item>
    </n-breadcrumb>
  </div>
</template>

<style scoped></style>

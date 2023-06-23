<script setup lang="ts">
import { NCarousel } from "naive-ui";
</script>

<template>
  <div>
    <n-carousel trigger="hover" 
               :autoplay=true
     >
      <!-- 循环五张图，路径为../assets/wallhaven-i.jpg -->
      <img class="carousel-img" 
           src="../assets/wallhaven-1.jpg"
      />
      <img class="carousel-img" 
           src="../assets/wallhaven-2.jpg"
      />
      <img class="carousel-img" 
           src="../assets/wallhaven-3.jpg"
      />
      <img class="carousel-img" 
           src="../assets/wallhaven-4.jpg"
      />
      <img class="carousel-img" 
           src="../assets/wallhaven-5.jpg"
      />
    </n-carousel>
  </div>
</template>

<style scoped>
.carousel-img {
  width: 100%;
  height: calc(100vh - 64px);
  object-fit: cover;
}
</style>
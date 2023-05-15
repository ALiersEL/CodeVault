<script setup lang="ts">
</script>

<template>
<div class="navigation-container">
    <router-link to="/">
      <img class="logo" src="../assets/logo.jpeg" alt="Codevault logo" />
    </router-link>
    <router-link to="/" 
                 class="navigation-link" 
                 active-class="navigation-link-active"
                 >Home
    </router-link>
    <router-link to="/problemset" 
                 class="navigation-link" 
                 active-class="navigation-link-active">Problemset
    </router-link>
    <router-link to="/me" 
                 class="navigation-link" 
                 active-class="navigation-link-active">Me
    </router-link>
    <router-link to="/about" 
                 class="navigation-link" 
                 active-class="navigation-link-active">About
    </router-link>
</div>
</template>

<style scoped>
.navigation-container {
    background-image: linear-gradient(to right top, #d16ba5, #c777b9, #ba83ca, #aa8fd8, #9a9ae1, #8aa7ec, #79b3f4, #69bff8, #52cffe, #41dfff, #46eefa, #5ffbf1);
    display: flex;
    align-items: center;
    height: 60px;
    box-shadow: 0 0 20px #eeeeee;
}
.logo {
    margin-top: 5px;
    margin-left: 10px;
    width: 50px;
    height: 50px;
    border-radius: 50%;
}
.navigation-link {
    margin-left: auto;
    margin-left: 50px;
    color: white;
    text-decoration: none;
    font-size: 20px;
}
.navigation-link-active{
    color: black;
    border-bottom: 2px solid black;
}
</style>
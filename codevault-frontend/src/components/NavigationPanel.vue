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
    display: flex;
    align-items: center;
    height: 60px;
    box-shadow: 0 0 20px #eeeeee;
    font-size: 1rem;
}
.logo {
    margin-top: 5px;
    margin-left: 10px;
    width: 40px;
    height: 40px;
    border-radius: 50%;
}
.navigation-link {
    margin-left: auto;
    margin-left: 50px;
    color: white;
    text-decoration: none;
    font-size: 1rem;
}
.navigation-link-active{
    color: black;
    border-bottom: 2px solid black;
}
</style>
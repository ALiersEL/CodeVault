<script setup lang="ts">
import AvatarDropdown from "../components/AvatarDropdown.vue";
import { NDropdown } from "naive-ui";
import router from "../router";

const options = [
    {
       label: "所有题目",
       key: "all",
    },
    {
        label: "归档题目",
        key: "archive",
    }
]
const handleSelect = (key: string) => {
    if(key === "all") {
        // 重定向到所有题目页面
        router.push("/problemset");
    }
    else if(key === "archive") {
        // 重定向到归档题目页面
        router.push("/folders");
    }
};
</script>

<template>
    <div class="navigation-container">
        <router-link to="/">
        <img class="logo" src="../assets/logo.jpeg" alt="Codevault logo" />
        </router-link>
        <router-link to="/" 
                    class="navigation-link" 
                    active-class="navigation-link-active"
                    >首页
        </router-link>
        <router-link to="/problemset" 
                    class="navigation-link" 
                    active-class="navigation-link-active">
            <n-dropdown trigger="hover" 
                        size="huge"
                        :options="options" 
                        @select="handleSelect">
                题库
            </n-dropdown>
        </router-link>
        <router-link to="/me" 
                    class="navigation-link" 
                    active-class="navigation-link-active">个人中心
        </router-link>
        <router-link to="/about" 
                    class="navigation-link" 
                    active-class="navigation-link-active">关于
        </router-link>
        <AvatarDropdown class="avatar" />
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
    color: #242424;
    text-decoration: none;
    font-size: 1rem;
}
.navigation-link-active{
    color: black;
    border-bottom: 2px solid black;
}

.avatar {
    margin-left: auto;
    margin-right: 10px;
}
</style>
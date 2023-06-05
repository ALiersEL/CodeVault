<script setup lang="ts">
import Avatar from "./Avatar.vue";
import { h } from "vue";
import type { Component } from "vue";
import { NIcon, NDropdown } from "naive-ui";
import {
  PersonCircleOutline as UserIcon,
  Pencil as EditIcon,
  LogOutOutline as LogoutIcon,
} from "@vicons/ionicons5";
import router from "../router";

import { postMapping } from "../api/request";

const renderIcon = (icon: Component) => {
  return () => {
    return h(NIcon, null, {
      default: () => h(icon),
    });
  };
};

const options =  [
  {
    label: "用户资料",
    key: "profile",
    icon: renderIcon(UserIcon),
  },
  {
    label: "编辑用户资料",
    key: "editProfile",
    icon: renderIcon(EditIcon),
  },
  {
    label: "退出登录",
    key: "logout",
    icon: renderIcon(LogoutIcon),
  },
];

const handleSelect = (key: string) => {
  if(key === "logout") {
      // 发送请求给后端，使会话失效或删除会话数据
      postMapping("/users/logout", {}).then((res) => {
        if (res.data.code === 200) {
          localStorage.clear();
          // 重定向到登录页面
          router.push("/login");
        }
        else {
          // 失败的提示
          alert(res.data.msg);
        }
      });
  }
};
</script>

<template>
  <div>
    <n-dropdown :options="options" @select="handleSelect">
      <Avatar />
    </n-dropdown>
  </div>
</template>

<style scoped>
</style>

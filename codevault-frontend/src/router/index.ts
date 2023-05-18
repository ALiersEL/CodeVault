import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

//定义路由
const routes: Array<RouteRecordRaw> = [
  { 
    path: "/", 
    component: () => import("../views/Home.vue") },
  {
    path: "/home",
    component: () => import("../views/Home.vue"),
    children: [
    ],
  },
  {
    path: "/problemset",
    component: () => import("../views/Problemset.vue"),
  },
  {
    path: "/me",
    component: () => import("../views/Me.vue"),
  },
  {
    path: "/about",
    component: () => import("../views/About.vue"),
  },
  {
    path: "/login",
    component: () => import("../views/Login.vue"),
  },
  {
    path: "/register",
    component: () => import("../views/Register.vue"),
  }
]

//导入路由，使用history模式
const router =  createRouter({
     history: createWebHistory(),
     routes 
});

export default router;
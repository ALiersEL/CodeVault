import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router";

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
    path: "/about",
    component: () => import("../views/About.vue"),
  },
  {
    path: "/login",
    component: () => import("../views/Login.vue"),
  },
]

//导入路由，使用history模式
const router =  createRouter({
     history: createWebHashHistory(), 
     linkActiveClass: 'link-active',
     routes 
});

export default router;
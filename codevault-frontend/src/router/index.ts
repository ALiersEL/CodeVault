import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import moment from 'moment';

//定义路由
const routes: Array<RouteRecordRaw> = [
  { 
    path: "/", 
    component: () => import("../views/Home.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/home",
    component: () => import("../views/Home.vue"),
    meta: { requiresAuth: true },
    children: [
    ],
  },
  {
    path: "/problemset",
    component: () => import("../views/Problemset.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/me",
    component: () => import("../views/Me.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/about",
    component: () => import("../views/About.vue"),
    meta: { requiresAuth: false },
  },
  {
    path: "/login",
    component: () => import("../views/Login.vue"),
    meta: { requiresAuth: false },
  },
  {
    path: "/register",
    component: () => import("../views/Register.vue"),
    meta: { requiresAuth: false },
  },
  {
    path: "/problems",
    component: () => import("../views/ProblemDetails.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/folders",
    name: "folders",
    component: () => import("../views/ProblemArchive.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/problems/codes",
    component: () => import("../components/CodeDisplay.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/problems/notes",
    component: () => import("../components/NoteDisplay.vue"),
    meta: { requiresAuth: true },
  }
]

//导入路由，使用history模式
const router =  createRouter({
     history: createWebHistory(),
     routes 
});


const checkUserLoggedIn = () => {
  // 检查用户是否登录
  const token = localStorage.getItem('token');
  const expires = localStorage.getItem('expires');
  if(token && expires && moment().isBefore(expires)){
      return true;
  }
  return false;
};

//进入页面之前判断用户是否登录
router.beforeEach((to, from, next) => {
  const isLoggedIn = checkUserLoggedIn(); // 检查用户是否登录的函数
  if (to.meta.requiresAuth && !isLoggedIn) {
    // 如果需要登录才能访问，并且用户未登录
    next({
      path: '/login',
      query: {redirect: from.fullPath}  // 将跳转的路由path作为参数，登录成功后跳转到该路由
    })    
  } else {
    next(); // 放行，继续访问目标页面
  }
  
  //文件夹跳转
  if(to.name === 'folders'){
    // 如果to.query.path不存在，则将其设置为~
    if(!to.query.path){
      to.query.path = '~';
    }
  }
});

export default router;
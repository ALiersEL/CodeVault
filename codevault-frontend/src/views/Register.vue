<script setup lang="ts">
import { ref } from "vue";
import { GlassesOutline, Glasses } from "@vicons/ionicons5";
import { postMapping } from "../api/request";
import router from "../router";
import SHA256 from "crypto-js/sha256";

let isVisible = ref(false);

const togglePassword = () => {
  isVisible.value = !isVisible.value;
};

// // computed
// const autoCompleteOptions = computed(() => {
//   return ["@gmail.com", "@163.com", "@qq.com"].map((suffix) => {
//     const prefix = email.value.split("@")[0];
//     return {
//       label: prefix + suffix,
//       value: prefix + suffix,
//     };
//   });
// });

let username = ref("");
let password = ref("");
let email = ref("");
let phoneNumber = ref("");

const register = () => {
  console.log("register");

  const user = {
    userName: username.value,
    passwordHash: SHA256(password.value + SHA256(username.value) + 'hlytbnpOQA').toString(),
    email: email.value,
    phoneNumber: phoneNumber.value,
  };

  console.log(user);

  //用axios把user传到后端
  postMapping("/users/register", user).then((res) => {
    console.log(res);
    if (res.data.code === 200) {
      alert("注册成功");
      router.push("/login");
    } else {
      alert(res.data.msg);
    }
  });
};
</script>

<template>
  <div class="full-screen">
    <div class="center">
      <div class="left"></div>

      <div class="right">
        <form class="content" @submit.prevent="register">
          <h2>加入 CodeVault</h2>

          <div class="box">
            <div>用户名</div>
            <input type="text" v-model="username" required />
          </div>

          <div class="box">
            <div>密码</div>
            <div class="input-wrapper">
              <input
                :type="isVisible ? 'text' : 'password'"
                v-model="password"
                required
              />
              <div v-if="isVisible" class="eye" @click="togglePassword">
                <GlassesOutline />
              </div>
              <div v-if="!isVisible" class="eye" @click="togglePassword">
                <Glasses />
              </div>
            </div>
          </div>

          <div class="box">
            <div>电子邮箱</div>
            <input type="email" v-model="email" required />
          </div>

          <div class="box">
            <div>手机号</div>
            <input type="tel" v-model="phoneNumber" />
          </div>

          <button type="submit">注 册</button>
          <p class="log-in">
            已经有账号了？ <router-link to="/login">登录</router-link>
          </p>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.full-screen {
  position: absolute;
  top: 0%;
  left: 0%;
  height: 100%;
  width: 100%;
  background: url("../assets/login-background.png") no-repeat;
  background-size: 100% 100%;
  color-scheme: light dark;
}
.center {
  position: relative;
  margin-top: 7.5%;
  margin-left: 15%;
  height: 75%;
  aspect-ratio: 18/10;
  display: flex;
  color: #000;
}

.left {
  position: relative;
  width: 66%;
  height: 100%;
  background: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)),
    url("../assets/rainy_day_in_tokyo.jpg") no-repeat;
  background-size: 100% 100%;
  /* background-color: #777; */
}

.right {
  position: relative;
  width: 33%;
  height: 100%;
  background-color: #f9f9f9;
}

.content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  /* color: white; */
}

.box {
  margin-top: 5%;
  position: relative;
  align-items: center;
  width: 65%;
  height: 5%;
  margin-bottom: 20px;
  border-radius: 5px;
  font-size: 15px;
}

.box input {
  margin-top: 1%;
  padding-left: 3%;
  position: relative;
  width: 97%;
  height: 100%;
  border: none;
  outline: none;
  font-size: 15px;
  border-radius: 10px;
}

.input-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
}

.eye {
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
  top: 60%;
  right: 5%;
  width: 10%;
  transform: translate(0, -50%);
}

.log-in {
  margin-top: 5%;
  font-size: 15px;
}

button {
  /* gradient */
  margin-top: 10%;
  width: 65%;
  height: 5%;
  border: none;
  border-radius: 5px;
  font-size: 15px;
  cursor: pointer;
  background-color: #71e69e;
  transition: background-color 0.3s ease;
}

button:hover {
  background-image: linear-gradient(
    to right,
    #71e69e,
    #6bdd9a,
    #65d495,
    #5fcc91,
    #5ac38c
  );
}

svg {
  color: #777;
}

a {
  color: #71e69e;
}

h2 {
  font-size: 33px;
}
</style>

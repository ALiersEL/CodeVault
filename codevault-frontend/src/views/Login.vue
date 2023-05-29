<script setup lang="ts">
import { GlassesOutline, Glasses } from "@vicons/ionicons5";
import { ref } from "vue";
import { postMapping } from "../api/request";
import router from "../router";
import SHA256 from "crypto-js/sha256";
// import { messageDark, useMessage } from "naive-ui";

const isVisible = ref(false);

const togglePassword = () => {
  isVisible.value = !isVisible.value;
};

let username = ref("");
let password = ref("");

const login = () => {
  console.log("login");

  const user = {
    userName: username.value,
    passwordHash: SHA256(password.value + SHA256(username.value) + 'hlytbnpOQA').toString(),
  };

  console.log(user);

  //用axios把user传到后端
  postMapping("/user/login", user).then((res) => {
    // const message = useMessage();
    console.log(res);
    if (res.data.code === 200) {
      // message.success("登录成功");
      // 将token存入localStorage
      console.log(res.data.data.token);
      localStorage.setItem("token", res.data.data.token);
      router.push("/home");
    } else if(res.data.code === 400){
      // message.error(res.data.msg);
    }
  });
}



</script>

<template>
  <div class="full-screen">
    <div class="center">
      <div class="left">
      </div>

      <div class="right">
        <form class="content" @submit.prevent="login">
          <h2>Log In</h2>

          <div class="box">
            <div>Username</div>
            <input type="text" v-model="username" required autofocus />
          </div>

          <div class="box">
            <div>Password</div>
            <div class="input-wrapper">
              <input :type="isVisible ? 'text' : 'password'" v-model="password" required/>
              <div v-if="isVisible" class="eye" @click="togglePassword">
                  <GlassesOutline />
              </div>
              <div v-if="!isVisible" class="eye" @click="togglePassword">
                  <Glasses />
              </div> 
            </div>
          </div>

          <!-- checkbox named Keep me logged in -->
          <div class="extra-feature">
            <div class="left-extra">
              <input type="checkbox" name="keepLoggedIn" id="keepLoggedIn">
              <label for="keepLoggedIn">&nbsp Keep me logged in</label>
            </div>

            <div class="right-extra">
              <a href="#">Forgot password?</a>
            </div>
            
          </div>

          <button type="submit">Log In</button>
          <div class="sign-up">Become a member <router-link to="/register">Join CodeVault</router-link></div>
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
  background: url("../assets/login-background.png") no-repeat ;
  background-size: 100% 100%;
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
  background: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)),url("../assets/rainy_day_in_tokyo.jpg") no-repeat; 
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
  font-size: 1rem;
}

.box input {
  margin-top: 1%;
  padding-left: 3%;
  position: relative;
  width: 97%;
  height: 100%;
  border: none;
  outline: none;
  font-size: 1rem;
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
  top: 50%;
  right: 5%;
  width: 10%;
  transform: translate(0, -50%);
}

/* 使得checkbox和它的label在同一行 */
.extra-feature {
  position: relative;
  margin-top: 8%;
  width: 80%;
  display: flex;
  flex-direction: row;
  justify-content: center;
  font-size: 1rem;
}

.left-extra {
  position: relative;
  display: flex;
  /* 块内元素居中 */
  justify-content: center;
  align-items: center;
  width: 50%;
}

.right-extra {
  position: relative;
  display: flex;
  justify-content: center;
  width: 50%;
}
.sign-up {
  margin-top: 5%;
  font-size: 1rem;
}

button {
  /* gradient */
  margin-top: 5%;
  width: 65%;
  height: 5%;
  border: none;
  border-radius: 5px;
  font-size: 1rem;
  cursor: pointer;
  background-color: #71e69e;
  transition: background-color 0.3s ease;
}

button:hover {

  background-image: linear-gradient(to right, #71e69e, #6bdd9a, #65d495, #5fcc91, #5ac38c);
}

svg {
  color: #777;
}

input[type="checkbox"] {
  /* 隐藏原始勾选标记 */
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  outline: none;
  box-sizing: border-box;
  overflow: hidden;
  text-overflow: ellipsis;
  vertical-align: middle;
}

input[type="checkbox"]::before {
  /* 创建自定义勾选标记 */
  content: " ";
  font-size: 19px;
  display: inline-block;
  width: 15px;
  height: 15px;
  border: 2px solid #5ac38c;
  border-radius: 3px;
}

input[type="checkbox"]:hover::before {
  background-color: rgba(90, 195, 140, 0.7); 
}

input[type="checkbox"]:checked::before {
  content: "✔️"; /* 使用Unicode字符表示勾选标记 */
  font-size: 0.85rem;
  background-color: #5ac38c; /* 改变勾选标记的颜色 */
}

a {
  color: #71e69e;
}

h2 {
  font-size: 2.2rem;
}
</style>

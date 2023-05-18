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

let username = ref("");
let password = ref("");
let email = ref("");
let phoneNumber = ref("");

const register = () => {
    console.log("register");

    const user = {
        userName: username.value,
        passwordHash: SHA256(password.value).toString(),
        email: email.value,
        phoneNumber: phoneNumber.value
    };

    console.log(user);

    //用axios把user传到后端
    postMapping("/user/register", user).then((res) => {
        console.log(res);
        if (res.data.code === 200) {
            alert("注册成功");
            router.push("/home");
        } else {
            alert(res.data.msg);
        }
    });
};

</script>

<template>
  <div class="full-screen">
    <div class="center">
      <div class="left">
      </div>

      <div class="right">
        <form class="content" @submit.prevent="register">
          <h2>Join CodeVault</h2>

          <div class="box">
            <div>Username</div>
            <input type="text" v-model="username" required/>
          </div>

          <div class="box">
            <div>Password</div>
            <input :type="isVisible ? 'text' : 'password'" v-model="password" required/>
            <div v-if="isVisible" class="eye" @click="togglePassword">
                <GlassesOutline />
            </div>
            <div v-if="!isVisible" class="eye" @click="togglePassword">
                <Glasses />
            </div> 
          </div>
          
          <div class="box">
            <div>Email</div>
            <input type="email" v-model="email" required/>
          </div>

          <div class="box">
            <div>Phone number</div>
            <input type="tel" v-model="phoneNumber"/>
          </div>

          <button type="submit" >Sign Up</button>
          <p class="log-in">Already a member? <router-link to="/login">Log In</router-link></p>
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
  font-size: 1.3rem;
}

.box input {
  margin-top: 1%;
  padding-left: 3%;
  position: relative;
  width: 97%;
  height: 100%;
  border: none;
  outline: none;
  font-size: 1.5rem;
  border-radius: 10px;
}

.eye {
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
  top: 70%;
  right: 5%;
  width: 10%
}

.log-in {
  margin-top: 5%;
  font-size: 1.4rem;
}

button {
  /* gradient */
  margin-top: 10%;
  width: 65%;
  height: 5%;
  border: none;
  border-radius: 5px;
  font-size: 1.2rem;
  cursor: pointer;
  background-color: #71e69e;
  transition: background-color 0.3s ease;
}

button:hover {

  background-image: linear-gradient(to right, #71e69e, #6bdd9a, #65d495, #5fcc91, #5ac38c);
}

a {
  color: #71e69e;
}

h2 {
  font-size: 2.5rem;
}
</style>

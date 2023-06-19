import { createApp } from "vue";
import "./style.css";
import App from "./App.vue";
import router from "./router";
import pinia from "./store";
import { createI18n } from "vue-i18n";

const i18n = createI18n({
    // something vue-i18n options here ...
    locale: navigator.language, // 从浏览器获取用户语言环境
    fallbackLocale: "en",
    globalInjection: true, // 自动注入
});

const app = createApp(App);

app.use(router);
app.use(pinia);
app.use(i18n);

app.mount("#app")

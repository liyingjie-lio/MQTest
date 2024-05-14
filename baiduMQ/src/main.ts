import { createApp } from 'vue'
import App from './App.vue'
import Vant from 'vant';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import * as VueRouter from 'vue-router';
import routes from "./config/route.ts";
import "vant/lib/index.css";
import '../global.css'

const app = createApp(App);
app.use(Vant);
app.use(ElementPlus);

const router = VueRouter.createRouter({
    // // 1. 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式 (url+#)
    // history: VueRouter.createWebHashHistory(),
    // 2. 改History 无#模式
    history: VueRouter.createWebHistory(),
    routes, // `routes: routes` 的缩写
})

app.use(router);
app.mount('#app')
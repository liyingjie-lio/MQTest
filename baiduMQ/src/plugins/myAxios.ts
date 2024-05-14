import axios from "axios";

const myAxios = axios.create({
    baseURL: "http://localhost:8080/api"
});

myAxios.defaults.withCredentials = true; //因为跨域请求默认不会携带cookie

// Add a request interceptor
myAxios.interceptors.request.use(function (config) {
    console.log('请求拦截', config)
    // Do something before request is sent
    return config;
}, function (error) {
    // Do something with request error
    return Promise.reject(error);
});

// Add a response interceptor
myAxios.interceptors.response.use(function (response) {
    console.log('相应拦截', response)
    // // 未登录则跳转到登录页
    // if (response?.data?.code === 40100) {
    //     const redirectUrl = window.location.href; // 获取当前页面的url
    //     window.location.href = `/user/login?redirect=${redirectUrl}`; // 跳转到登录页，并携带登陆前的url参数到登录页，方便登录后跳转回来
    // }
    // Do something with response data
    return response.data;
}, function (error) {
    // Do something with response error
    return Promise.reject(error);
});

export default myAxios;
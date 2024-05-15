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
    console.log('响应拦截', response)
    // return response.data;
    return response;
}, function (error) {
    // Do something with response error
    return Promise.reject(error);
});

export default myAxios;
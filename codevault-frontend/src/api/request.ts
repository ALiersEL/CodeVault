import axios from "axios"; 
import { createDiscreteApi } from "naive-ui";

// axios配置 
const request = axios.create({ 
    baseURL: import.meta.env['VITE_APP_BASE_API'],
    //防止post请求，后端无法接收参数问题 
    /*transformRequest: [ 
        function(data) { 
            return qs.stringify(data) 
        } 
    ],*/ 
    timeout: 5000,
});

// 请求拦截器配置 
request.interceptors.request.use( 
    config => { 
        config.headers['Authorization'] = 'bearer';
        console.log("config===>", config);
        return config;
    },
    error => { 
        return Promise.reject(error);
    } 
)

const { message } = createDiscreteApi(['message']);
// 响应拦截器配置 
request.interceptors.response.use( 
    response => { 
        if (response.status === 404) { 
            message.error("请求未找到"); 
        } else if(response.status != 200){ 
            return Promise.reject("网络请求错误"); 
        }
        return response; 
    }, error => { 
        const {code} = error; 
        if ("ERR_BAD_RESPONSE" === code) { 
            message.error("网络请求异常"); 
        } else { 
            message.error("服务器异常"); 
        }
        // Do something with response error 
        return Promise.reject(error) 
    } 
)

// 1. GET 请求封装,注意get请求传递参数的属性为params,如果写成data:data,则springboot后端的 @getMapping映射不到 
export const getMapping = (url: string, data: any, token: any) => { 
    return request.get(url, { 
        params: data, 
    }) 
}
// 2. POST 请求 
export const postMapping = (url: string, data: any) => { 
    return request({ 
        method: 'post', 
        url: url, 
        data: data, 
    }) 
}
// 3.put请求 
export const putMapping = (url: string, data: any) => { 
    return request({ 
        method: 'put', 
        url: url, 
        data: data,
    }) 
}
// 4.delete 请求 
export const deleteMapping = (url: string, data: any) => {
    return request({ 
        method: 'delete',
        url: url, 
        data: data, 
    }) 
}
// 自定义请求,可传入method、url、data的属性值 
export const requestMapping = (method: string, url: string, data: any) => { 
    if ('get' == method || 'GET' == method) { 
        return getMapping(url, data, null); 
    } else { 
        return request({ 
            method: method,
            url: url, 
            data: data, 
        }) 
    } 
}

// 导出request供其他组件使用 
export default request
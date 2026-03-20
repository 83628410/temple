import axios, { AxiosResponse, InternalAxiosRequestConfig } from 'axios'

import { useUserStore } from '@/store/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const service = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_API,
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json;charset=utf-8',
    },
})

// 请求拦截器 - 每次请求前将 token 添加到请求头中
service.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        if (!config.headers) {
            throw new Error('headers is required')
        }
        const userStoreInstance = useUserStore()
        const { token } = userStoreInstance
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

// 响应拦截器 - 处理接口返回的错误
service.interceptors.response.use(
    // 处理正常响应
    (response: AxiosResponse) => {
        const { code, msg } = response.data
        if (code !== 200) {
            ElMessage.error(msg)
            return Promise.reject(new Error(msg || 'Error'))
        }
        return response.data
    },
    // 处理响应错误
    (error) => {
        const userStoreInstance = useUserStore()
        const { code, msg } = error.response.data
        // 401 表示登录过期，清除 token 并跳转到登录页
        if (code === 401) {
            userStoreInstance.removeToken()
            window.location.href = '/login'
            ElMessageBox.alert('登录过期，请重新登录', '提示', {
                type: 'warning',
            }).then(() => {
            }).catch(() => {

            })
        }else {
            // 其他错误直接提示用户
            ElMessage.error(msg || '系统出错')
        }
        return Promise.reject(new Error(msg || 'Error'))
    }
)
export default service

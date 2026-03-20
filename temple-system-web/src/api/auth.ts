import request from '@/utils/request'

// 登录请求参数
export interface LoginRequest {
    username: string
    password: string
}

// 登录响应数据
export interface LoginResponse {
    code: number
    msg: string
    data: {
        token: string
    }
}

/**
 * 登录服务
 */
export const loginService = {
    /**
     * 用户登录
     * @param data 登录参数
     * @returns 登录结果
     */
    login: (data: LoginRequest): Promise<LoginResponse> => {
        return request({
            url: '/auth/login',
            method: 'POST',
            data
        })
    }
}

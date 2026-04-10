import request from '@/utils/request'

// 用户数据接口
export interface UserData {
    id: number
    username: string
    nickname: string
    email: string
    phone: string
    status: number
    createTime?: string
    updateTime?: string
    roleIds?: number[]
    roles?: any[]
}

// 用户保存请求
export interface UserSaveRequest {
    id?: number
    username: string
    password?: string
    nickname: string
    email: string
    phone: string
    status: number
    roleIds?: number[]
}

// 用户列表响应
export interface UserListResponse {
    code: number
    msg: string
    data: UserData[]
}

// 用户详情响应
export interface UserResponse {
    code: number
    msg: string
    data: UserData
}

export const userService = {
    /**
     * 获取用户列表
     * @returns 用户列表
     */
    getList: (): Promise<UserListResponse> => {
        return request({
            url: '/user/list',
            method: 'GET'
        })
    },

    /**
     * 获取用户详情
     * @param id 用户ID
     * @returns 用户详情
     */
    getById: (id: number): Promise<UserResponse> => {
        return request({
            url: `/user/${id}`,
            method: 'GET'
        })
    },

    /**
     * 保存用户
     * @param user 用户信息
     * @returns 保存结果
     */
    save: (user: UserSaveRequest): Promise<UserResponse> => {
        return request({
            url: '/user/save',
            method: 'POST',
            data: user
        })
    },

    /**
     * 更新用户
     * @param user 用户信息
     * @returns 更新结果
     */
    update: (user: UserSaveRequest): Promise<UserResponse> => {
        return request({
            url: '/user/update',
            method: 'PUT',
            data: user
        })
    },

    /**
     * 删除用户
     * @param id 用户ID
     * @returns 删除结果
     */
    delete: (id: number): Promise<{ code: number; msg: string }> => {
        return request({
            url: `/user/delete/${id}`,
            method: 'DELETE'
        })
    }
}

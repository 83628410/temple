import request from '@/utils/request'

// 角色数据接口
export interface RoleData {
    id: number
    name: string
    description: string
    status: number
    createTime?: string
    updateTime?: string
}

// 角色DTO接口
export interface RoleDTO {
    id?: number
    name: string
    description: string
    status: number
}

// 角色列表响应
export interface RoleListResponse {
    code: number
    msg: string
    data: RoleData[]
}

// 角色详情响应
export interface RoleDetailResponse {
    code: number
    msg: string
    data: RoleData
}

export const roleService = {
    /**
     * 获取角色列表
     * @returns 角色列表
     */
    getList: (): Promise<RoleListResponse> => {
        return request({
            url: '/role/list',
            method: 'GET'
        })
    },

    /**
     * 获取角色详情
     * @param id 角色ID
     * @returns 角色详情
     */
    getById: (id: number): Promise<RoleDetailResponse> => {
        return request({
            url: `/role/${id}`,
            method: 'GET'
        })
    },

    /**
     * 创建角色
     * @param role 角色信息
     * @returns 创建结果
     */
    save: (role: RoleDTO): Promise<{ code: number; msg: string; data?: RoleData }> => {
        return request({
            url: '/role/save',
            method: 'POST',
            data: role
        })
    },

    /**
     * 更新角色
     * @param role 角色信息
     * @returns 更新结果
     */
    update: (role: RoleDTO): Promise<{ code: number; msg: string; data?: RoleData }> => {
        return request({
            url: '/role/update',
            method: 'PUT',
            data: role
        })
    },

    /**
     * 删除角色
     * @param id 角色ID
     * @returns 删除结果
     */
    delete: (id: number): Promise<{ code: number; msg: string }> => {
        return request({
            url: `/role/delete/${id}`,
            method: 'DELETE'
        })
    }
}

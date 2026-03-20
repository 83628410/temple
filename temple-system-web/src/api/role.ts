import request from '@/utils/request'

export interface Role {
    id: number
    name: string
    description: string
    status: number
    createTime?: string
    updateTime?: string
}

export interface RoleDTO {
    id?: number
    name: string
    description: string
    status: number
    menuIds?: number[]
}

export interface RoleListResponse {
    code: number
    msg: string
    data: Role[]
}

export interface RoleResponse {
    code: number
    msg: string
    data: Role
}

export const roleService = {
    getList: (): Promise<RoleListResponse> => {
        return request({
            url: '/role/list',
            method: 'GET'
        })
    },

    getById: (id: number): Promise<RoleResponse> => {
        return request({
            url: `/role/${id}`,
            method: 'GET'
        })
    },

    save: (role: RoleDTO): Promise<RoleResponse> => {
        return request({
            url: '/role/save',
            method: 'POST',
            data: role
        })
    },

    update: (role: RoleDTO): Promise<RoleResponse> => {
        return request({
            url: '/role/update',
            method: 'PUT',
            data: role
        })
    },

    delete: (id: number): Promise<{ code: number; msg: string }> => {
        return request({
            url: `/role/delete/${id}`,
            method: 'DELETE'
        })
    }
}

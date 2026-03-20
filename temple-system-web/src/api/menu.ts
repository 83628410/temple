import request from '@/utils/request'

// 菜单类型
export interface Menu {
    name: string
    path?: string
    component?: string
    children?: Menu[] // 子菜单
    meta?: Meta
}
export interface Meta{
    title: string
    icon?: string
}

// 菜单列表响应
export interface MenuListResponse {
    code: number
    msg: string
    data: Menu[]
}

/**
 * 菜单服务
 */
// 菜单数据接口
export interface MenuData {
    id: number
    name: string
    path: string
    component: string
    icon: string
    orderNum: number
    type: string
    parentId: number
    permission: string
    status: number
    children?: MenuData[]
}

// 菜单列表响应
export interface MenuListDataResponse {
    code: number
    msg: string
    data: MenuData[]
}

// 菜单保存请求
export interface MenuSaveRequest {
    id?: number
    name: string
    path: string
    component: string
    icon: string
    type: string
    parentId: number
    orderNum: number
    status: number
    permission?: string
}

export const menuService = {
    /**
     * 获取菜单列表
     * @returns 菜单列表
     */
    getRoutes: (): Promise<MenuListResponse> => {
        return request({
            url: '/menu/routes',
            method: 'GET'
        })
    },
    
    /**
     * 获取所有菜单列表
     * @returns 所有菜单列表
     */
    getList: (): Promise<MenuListDataResponse> => {
        return request({
            url: '/menu/list',
            method: 'GET'
        })
    },
    
    /**
     * 保存菜单
     * @param menu 菜单信息
     * @returns 保存结果
     */
    save: (menu: MenuSaveRequest): Promise<{ code: number; msg: string; data?: any }> => {
        return request({
            url: '/menu/save',
            method: 'POST',
            data: menu
        })
    },
    
    /**
     * 更新菜单
     * @param menu 菜单信息
     * @returns 更新结果
     */
    update: (menu: MenuSaveRequest): Promise<{ code: number; msg: string; data?: any }> => {
        return request({
            url: '/menu/update',
            method: 'PUT',
            data: menu
        })
    },
    
    /**
     * 删除菜单
     * @param id 菜单ID
     * @returns 删除结果
     */
    delete: (id: number): Promise<{ code: number; msg: string; data?: any }> => {
        return request({
            url: `/menu/delete/${id}`,
            method: 'DELETE'
        })
    },
    
    /**
     * 更新菜单状态
     * @param id 菜单ID
     * @param status 状态
     * @returns 更新结果
     */
    updateStatus: (id: number, status: number): Promise<{ code: number; msg: string; data?: any }> => {
        return request({
            url: `/menu/update/status`,
            method: 'PUT',
            params: { id, status }
        })
    }
}

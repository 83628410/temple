import { defineStore } from "pinia";
import { RouteRecordRaw } from "vue-router";
import { Menu, menuService } from "@/api/menu";
import { constantRoutes } from "@/router/index";
const modules = import.meta.glob('../views/**/*.vue', { eager: false })

const usePermissionStore = defineStore('permission', () => {
    // 路由列表 包含静态路由和动态添加的路由
    const routes = ref<RouteRecordRaw[]>([])
    // 动态添加的路由
    const addRoutes = ref<RouteRecordRaw[]>([])
    // 路由是否加载完成
    const routeLoaded = ref<boolean>(false)

    /** 生成动态路由*/
    async function generateRouters() {
        const _routesdata = await menuService.getRoutes()
        addRoutes.value = transformRoutes(_routesdata.data)
        routes.value = [...constantRoutes, ...addRoutes.value]
        routeLoaded.value = true
        return routes.value
    }
    /** 转换菜单列表为路由列表 */
    function transformRoutes(menus: Menu[]): RouteRecordRaw[] {
        if (!menus || menus.length === 0) return []
        return menus
            .map(menu => {
                const route: any = {
                    path: menu.path || '',
                    name: menu.name,
                    meta: {
                        title: menu.meta?.title,
                        icon: menu.meta?.icon,
                        hidden: menu.meta?.hidden || false,
                    }
                }
                // 处理组件路径
                if (menu.component == 'layout') {
                    route.component = () => import('../views/layout/index.vue')
                } else {
                    route.component = modules[`../views/${menu.component}.vue`]
                }
                // 处理子菜单
                if (menu.children && menu.children.length > 0) {
                    route.children = transformRoutes(menu.children)
                }

                return route as RouteRecordRaw
            })
    }

    return {
        routes,
        addRoutes,
        generateRouters,
        routeLoaded
    }
})

export default usePermissionStore

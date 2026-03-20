import { useUserStore } from '@/store/user'
import router from '@/router/index'
import usePermissionStore  from '@/store/permission'
import { RouteRecordRaw } from 'vue-router'
/**
 * 路由权限卫卫
 * 用于检查路由是否需要登录、是否有权限访问、动态路由
 */
// 路由白名单
const whiteList = ['/login', '/404', '/401']
export function setupRouterGuard() {
    router.beforeEach(async (to, from, next) => {
        const permissionStore = usePermissionStore()

        const userStore = useUserStore()
        const isLogin = userStore.isLogin()
        
        // 未登录处理
        if (!isLogin) {
            if (whiteList.includes(to.path)) {
                next()
            } else {
                next(`/login?redirect=${to.fullPath}`)
            }
            return
        }
        // 已登录跳转到首页
        if (to.path === '/login') {
            next('/')
            return
        }
        // 如果动态路由为空，生成动态路由
        if (!permissionStore.routeLoaded) {
            const _routes = await permissionStore.generateRouters()
            _routes.forEach((route: RouteRecordRaw) => {
                router.addRoute(route)
            })
            next({ ...to, replace: true })
            return
        }

        // 动态标题
        const title = (to.params.title as string) || (to.query.title as string);
        if (title) {
            to.meta.title = title;
        }
        next()
    })
    router.afterEach((to, from) => {
        document.title = to.meta.title as string
    })

}


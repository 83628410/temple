import { App, KeepAlive } from "vue";
import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router";

// 定义静态路由
// 这些路由不需要权限控制，所有用户都可以访问
export const constantRoutes: Array<RouteRecordRaw> = [

    // 登录页面
    {
        path: '/login',
        name: 'login',
        component: () => import('@/views/login/index.vue'),
        meta: {
            hidden: true, // 隐藏在侧边栏
        }
    },

    // 主布局路由
    {
        path: '/',
        name:'/',
        component: () => import('@/views/layout/index.vue'),
        redirect: "/dashboard",
        children: [
            // 仪表盘页面
            {
                path: '/dashboard',
                name: 'dashboard',
                component: () => import('@/views/dashboard/index.vue'),
                meta:{
                    title:'仪表盘',
                    icon:'homepage',
                    affix:true,// 登录成功后是否固定在侧边栏上
                    KeepAlive:true
                }
            },
            // 404 错误页面
            {
                path: '/404',
                name: '404',
                component: () => import('@/views/error-page/404.vue'),
                meta: {
                    hidden: true, // 隐藏在侧边栏
                }
            },
            // 401 无权限页面
            {
                path: '/401',
                name: '401',
                component: () => import('@/views/error-page/401.vue'),
                meta: {
                    hidden: true, // 隐藏在侧边栏
                }
            },
        ]
    }
]

// 创建路由实例
const router = createRouter({
    history: createWebHashHistory(), // 使用 hash 模式
    routes: constantRoutes as RouteRecordRaw[], // 注册静态路由
    scrollBehavior() {
        // 每次路由切换时滚动到顶部
        return { top: 0, left: 0 }
    }
})

// 重置路由函数
// 用于登出或权限变更时重置路由
export function resetRouter() {

}
export function setupRouter(app :App) {
    app.use(router)
}
export default router

/**
 * 应用程序入口文件
 * 用于创建 Vue 应用实例并挂载到 DOM
 */
import '../lang/index.js'

import { createApp } from 'vue' // 导入 Vue 的 createApp 函数
import 'uno.css' // 导入 UnoCSS 样式文件
import App from './App.vue' // 导入根组件
import 'virtual:svg-icons-register' // 注册 SVG 图标（用于 vite-plugin-svg-icons 插件）
import {createPinia} from 'pinia' // 导入 Pinia 状态管理库的 createPinia 函数
import {setupRouter} from './router/index' // 导入路由模块 router
import '@/styles/index.scss' // 导入全局样式文件
import { setupRouterGuard } from './router/permission-guard'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import { setupI18n } from './lang/i18n'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

/**
 * 创建 Vue 应用实例
 * 1. 传入根组件 App
 * 2. 使用 Pinia 状态管理
 * 3. 挂载到 DOM 中的 #app 元素
 */
const app = createApp(App)
// 使用 Pinia 状态管理
const pinia = createPinia()
// 开启状态持久化
pinia.use(piniaPluginPersistedstate)

app.use(pinia)
// 设置路由
setupRouter(app)
// 设置路由守卫
setupRouterGuard()
// 设置 i18n
setupI18n(app)
// 注册 Element Plus 图标组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
// 挂载应用实例到 DOM 中的 #app 元素
app.mount('#app')

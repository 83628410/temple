/**
 * Vite 配置文件
 * 用于配置 Vite 构建工具的各种选项和插件
 */

import { defineConfig, loadEnv, type ConfigEnv, type UserConfig } from 'vite' // 导入 Vite 的 defineConfig 函数
import vue from '@vitejs/plugin-vue' // 导入 Vue 插件
import path from 'path' // 导入 Node.js 路径模块

// 定义 src 目录的绝对路径
const pathSrc = path.resolve(__dirname, 'src')

// 导入自动导入相关插件
import AutoImport from 'unplugin-auto-import/vite' // 自动导入 Vue 组合式 API
import Components from 'unplugin-vue-components/vite' // 自动导入组件
import { ElementPlusResolver } from "unplugin-vue-components/resolvers"; // Element Plus 组件解析器
import IconsResolver from "unplugin-icons/resolver"; // 图标组件解析器
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'; // SVG 图标插件
import Icons from 'unplugin-icons/vite' // 图标插件
import UnoCSS from 'unocss/vite' // UnoCSS 插件
import vitePluginsAutoI18n, { YoudaoTranslator } from 'vite-auto-i18n-plugin'

// https://vite.dev/config/
export default defineConfig(({ mode }: ConfigEnv): UserConfig => {
    const env = loadEnv(mode, process.cwd())
    return {
        // 路径解析配置
        resolve: {
            // 路径别名
            alias: {
                '@': pathSrc, // 将 @ 指向 src 目录
            },
        },
        // 插件配置
        plugins: [
            vue(), // Vue 插件
            UnoCSS({}), // UnoCSS 插件
            // SVG 图标插件配置
            createSvgIconsPlugin(
                {
                    // 指定需要缓存的图标文件夹
                    iconDirs: [path.resolve(process.cwd(), 'src/assets/icons')],
                    // 指定 symbolId 格式
                    symbolId: 'icon-[dir]-[name]',
                }
            ),

            // 自动导入配置
            AutoImport({
                // 解析器配置
                resolvers: [
                    ElementPlusResolver(), // Element Plus 组件自动导入
                    IconsResolver({}), // 图标组件自动导入
                ],
                // 自动导入 Vue 相关函数，如：ref, onMounted 等
                imports: ['vue'],
                // 自动导出函数 TS 类型声明
                dts: path.resolve(pathSrc, 'types', 'auto-imports.d.ts'),
            }),

            // 组件自动导入配置
            Components({
                // 解析器配置
                resolvers: [
                    ElementPlusResolver({ importStyle: "sass" }), // Element Plus 组件自动导入
                    // 自动注册图标组件
                    IconsResolver({
                        // element-plus 图标库，其他图标库 https://icon-sets.iconify.design/
                        enabledCollections: ["ep"]
                    }),
                ],
                // 自动导入组件
                dts: path.resolve(pathSrc, "types", 'components.d.ts'),
            }),

            // 图标插件配置
            Icons({
                autoInstall: true, // 自动安装图标库
            }),
            vitePluginsAutoI18n({
                 targetLangList: ['zh-cn','en','de','fr','it'],
                translator: new YoudaoTranslator({
                    appId: '420f95c7184552b4',
                    appKey: 'e1tKf0mMRiAGkki92J1U8nG6UL87sYLb'
                })
            })
        ],
        css: {

        },
        server: {
            host: '0.0.0.0', // 允许所有网络访问
            port: Number(env.VITE_APP_PORT), // 端口号
            open: true, // 自动打开浏览器
            // 方向代理解决跨域问题，将请求代理到后端服务
            proxy: {
                [env.VITE_APP_BASE_API]: {
                    target: 'http://localhost:8080/system/', // 后端服务地址
                    changeOrigin: true, // 改变源地址为后端服务地址
                    secure: false, // 不使用 HTTPS 协议
                    rewrite: (path) => path.replace(new RegExp('^' + env.VITE_APP_BASE_API), ''), // // 移除 /api 前缀
                },
            },
        },
    }
})

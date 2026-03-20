/// <reference types="vite/client" />

/**
 * 类型声明文件
 * 用于声明 TypeScript 无法自动识别的模块类型
 */

/**
 * 声明 .vue 文件的模块类型
 * 使得 TypeScript 能够正确识别和导入 Vue 组件
 */
declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

/**
 * 声明 virtual:svg-icons-register 模块类型
 * 用于 vite-plugin-svg-icons 插件的虚拟模块
 * 解决 "找不到模块 virtual:svg-icons-register" 的类型错误
 */
declare module 'virtual:svg-icons-register'

/**
 * Vite 环境变量类型定义文件
 * 用于定义 Vite 项目中的环境变量类型，提供 TypeScript 类型提示
 */

/**
 * 环境变量接口
 * 定义项目中使用的环境变量及其类型
 */
interface ImportMetaEnv {
  /** 应用标题 */
  VITE_APP_TITLE: string
  /** 应用端口号 */
  VITE_APP_PORT: number
  /** 应用基础 API 地址 */
  VITE_APP_BASE_API: string
}

/**
 * ImportMeta 接口
 * 扩展 Vite 的 ImportMeta 类型，添加 env 属性
 */
interface ImportMeta {
  /** 环境变量对象 */
  readonly env: ImportMetaEnv
}

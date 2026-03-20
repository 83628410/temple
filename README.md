# 庙观管理系统

## 项目基础信息

- 项目类型：采用vue3 + springboot3开发的前后端分离的庙观在线报名系统
- 前端核心技术栈：vue 3.2.5 + vue-router 4.0.13 + pinia 3.0.4 + axios 1.13.6 + element-plus 2.13.3 + unocss 66.6.5 + typescript 5.9.3
- 后端核心技术栈：springboot 3.2.4 + spring-security 6.2.3 + jwt 0.12.3 + spring boot jpa 3.2.4 + mysql8.0.33
- 项目用途：庙观工作人员登记各类报名信息的管理系统
- 核心约束：禁止使用 any 类型、所有接口需加 Token 权限校验、前端开发遵循vue3 函数式编程规则

## 目录结构
- temple-common：是后端的公共模块
- temple-system: 是管理系统的后台接口
- temple-system-web:是管理系统后台的前端代码

## 环境搭建
- 前端开发：npm run dev
- 后端开发:直接运行根目录下的run.sh文件 
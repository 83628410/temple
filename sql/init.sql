/*
 Navicat Premium Data Transfer

 Source Server         : docker-mysql8
 Source Server Type    : MySQL
 Source Server Version : 80022 (8.0.22)
 Source Host           : localhost:3306
 Source Schema         : temple_system

 Target Server Type    : MySQL
 Target Server Version : 80022 (8.0.22)
 File Encoding         : 65001

 Date: 20/03/2026 14:49:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `component` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `icon` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `order_num` int DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  `path` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` int DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `permission` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `title` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` (`id`, `component`, `create_time`, `icon`, `name`, `order_num`, `parent_id`, `path`, `status`, `update_time`, `permission`, `type`, `title`) VALUES (1, 'user/index', '2026-03-05 07:16:09.788000', 'user', 'UserIndex', 1, 4, '/setting/user', 1, '2026-03-05 07:16:09.788000', NULL, 'M', '用户管理');
INSERT INTO `sys_menu` (`id`, `component`, `create_time`, `icon`, `name`, `order_num`, `parent_id`, `path`, `status`, `update_time`, `permission`, `type`, `title`) VALUES (2, 'role/index', '2026-03-05 07:16:09.796000', 'role', 'RoleIndex', 2, 4, '/setting/role', 1, '2026-03-05 07:16:09.796000', NULL, 'M', '角色管理');
INSERT INTO `sys_menu` (`id`, `component`, `create_time`, `icon`, `name`, `order_num`, `parent_id`, `path`, `status`, `update_time`, `permission`, `type`, `title`) VALUES (3, 'menu/index', '2026-03-05 07:16:09.804000', 'menu', 'MenuIndex', 3, 4, '/setting/menu', 1, '2026-03-05 07:16:09.804000', NULL, 'M', '菜单管理');
INSERT INTO `sys_menu` (`id`, `component`, `create_time`, `icon`, `name`, `order_num`, `parent_id`, `path`, `status`, `update_time`, `permission`, `type`, `title`) VALUES (4, 'layout', NULL, 'Setting', '/setting', 1, 0, '/setting', 1, NULL, NULL, 'C', '系统管理');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `description` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `status` int DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bqy406dtsr7j7d6fawi1ckyn1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`id`, `create_time`, `description`, `name`, `status`, `update_time`) VALUES (1, '2026-03-05 07:16:09.568000', '管理员角色', 'ADMIN', 1, '2026-03-05 07:16:09.838000');
INSERT INTO `sys_role` (`id`, `create_time`, `description`, `name`, `status`, `update_time`) VALUES (2, '2026-03-05 07:16:09.609000', '普通用户角色', 'USER', 1, '2026-03-05 07:16:09.870000');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL,
  `menu_id` bigint NOT NULL,
  KEY `FKf3mud4qoc7ayew8nml4plkevo` (`menu_id`),
  KEY `FKkeitxsgxwayackgqllio4ohn5` (`role_id`),
  CONSTRAINT `FKf3mud4qoc7ayew8nml4plkevo` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`),
  CONSTRAINT `FKkeitxsgxwayackgqllio4ohn5` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 1);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 2);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 3);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nickname` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` int DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_51bvuyvihefoh4kp5syh2jpi4` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `create_time`, `email`, `nickname`, `password`, `phone`, `status`, `update_time`, `username`) VALUES (1, '2026-03-05 07:16:09.694000', 'admin@temple.com', '系统管理员', '$2a$10$R3WzAkSD62mpW8Lq9YS2QeOK8pIZmot8dMKowN4Vw8RljmqdqBQkm', '13800138000', 1, '2026-03-05 07:16:09.694000', 'admin');
INSERT INTO `sys_user` (`id`, `create_time`, `email`, `nickname`, `password`, `phone`, `status`, `update_time`, `username`) VALUES (2, '2026-03-05 07:16:09.779000', 'user@temple.com', '普通用户', '$2a$10$kjdsgpZSYwwmOKciAm1RPuQugA0pWBaefrQIYqHV5GA1D4qVss6p6', '13800138001', 1, '2026-03-05 07:16:09.779000', 'user');
INSERT INTO `sys_user` (`id`, `create_time`, `email`, `nickname`, `password`, `phone`, `status`, `update_time`, `username`) VALUES (3, '2026-03-05 07:16:10.444000', 'test@example.com', '测试用户', '$2a$10$M9VFStFqEEn5fQ9Khn4hrOgDzMQwF6/ZwjmR0KuNwZsCuIr5jKa86', '13800138000', 1, '2026-03-05 07:16:10.444000', 'testuser');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  KEY `FKhh52n8vd4ny9ff4x9fb8v65qx` (`role_id`),
  KEY `FKb40xxfch70f5qnyfw8yme1n1s` (`user_id`),
  CONSTRAINT `FKb40xxfch70f5qnyfw8yme1n1s` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`),
  CONSTRAINT `FKhh52n8vd4ny9ff4x9fb8v65qx` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (2, 2);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

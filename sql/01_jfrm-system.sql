SET NAMES utf8mb4 COLLATE utf8mb4_general_ci;

DROP TABLE IF EXISTS `system_tenant`;
CREATE TABLE `system_tenant`  (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `name` varchar(40) NOT NULL COMMENT '租户名称',
  `contact_user_id` bigint unsigned not null COMMENT '租户对应的系统用户ID',
  `contact_name` varchar(30) DEFAULT '' COMMENT '租户联系人名称',
  `contact_mobile` varchar(11) COMMENT '租户联系电话',
  `status` tinyint(4) DEFAULT '0' COMMENT '是否禁用（0-启用,1-禁用）',
  `expire_time` datetime COMMENT '过期时间',
  `account_count` int unsigned not null default 20 COMMENT '账号数量',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime COMMENT '更新时间',
  `deleted` bit NOT NULL DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
  UNIQUE KEY `uq_tenant_name` (`name`),
  PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '系统租户表';

DROP TABLE IF EXISTS `system_dept`;
CREATE TABLE `system_dept`  (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `sort` int(11) DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT '' COMMENT '负责人',
  `phone` varchar(11) COMMENT '联系电话',
  `email` varchar(50) COMMENT '邮箱',
  `status` tinyint(4) DEFAULT '0' COMMENT '是否禁用（0-启用,1-禁用）',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime COMMENT '更新时间',
  `tenant_id` bigint unsigned DEFAULT 0 COMMENT '租户ID',
  `deleted` bit NOT NULL DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '部门表';

DROP TABLE IF EXISTS system_role;
CREATE TABLE system_role (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(20) COMMENT '角色代码(租户+角色代码 唯一)',
  `name` varchar(50) COMMENT '角色名',
  `sort` int UNSIGNED NOT NULL DEFAULT 1 COMMENT '显示顺序',
  `data_scope` tinyint unsigned not null default 1 COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `data_scope_dept_ids` varchar(500) COMMENT '数据范围(指定部门数组)',
  `status` tinyint(4) DEFAULT '0' COMMENT '是否禁用（0-启用,1-禁用）',
  `type` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '角色类型（1-系统角色,2-自定义角色）',
  `remark` varchar(64) COMMENT '角色备注',
  `creator` varchar(50) DEFAULT '' COMMENT '创建者',
  `create_time` datetime COMMENT '创建时间',
  `updater` varchar(50) DEFAULT '' COMMENT '更新者',
  `update_time` datetime COMMENT '更新时间',
  `tenant_id` bigint unsigned DEFAULT 0 COMMENT '租户ID',
  `deleted` bit DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY `uq_sys_role` (`code`, `tenant_id`),
  PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT='系统角色表';

DROP TABLE IF EXISTS system_users;
CREATE TABLE system_users (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(64) NOT NULL DEFAULT '' COMMENT '密码',
  `nickname` varchar(50) NOT NULL DEFAULT '' COMMENT '昵称',
  `remark` varchar(128) DEFAULT '' comment '备注',
  `dept_id` bigint(20) DEFAULT 0 COMMENT '部门ID',
  `post_ids` varchar(255) COMMENT '岗位ID数组',
  `email` varchar(60) DEFAULT '' COMMENT '邮箱',
  `mobile` varchar(20) DEFAULT '' COMMENT '手机号码',
  `sex` tinyint DEFAULT 0 COMMENT '性别（0保密，1-男，2-女）',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像',
  `status` tinyint(4) DEFAULT 0 COMMENT '是否禁用（0-启用,1-禁用）',
  `login_ip` varchar(50) default '' comment '最后登录ip',
  `login_date` datetime comment '最后登录时间',
  `create_time` datetime COMMENT '创建时间',
  `creator` varchar(50) DEFAULT '' COMMENT '创建者',
  `update_time` datetime COMMENT '更新时间',
  `updater` varchar(50) DEFAULT '' COMMENT '更新者',
  `tenant_id` bigint unsigned NOT NULL DEFAULT 0 COMMENT '租户ID',
  `deleted` bit DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY `uq_sys_user` (`username`, `tenant_id`),
  PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT='系统用户表';

DROP TABLE IF EXISTS system_user_role;
CREATE TABLE system_user_role (
  id bigint unsigned not null AUTO_INCREMENT COMMENT '主键',
  user_id bigint(20) NOT NULL COMMENT '管理员id',
  role_id int(11) NOT NULL COMMENT '角色id',
  create_time datetime COMMENT '创建时间',
  creator varchar(50) DEFAULT '' COMMENT '创建者',
  tenant_id bigint unsigned NOT NULL DEFAULT 0 COMMENT '租户ID',
  UNIQUE KEY uq_ur_rel (user_id,role_id,tenant_id),
  PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT='系统用户与角色关系表';

-- oauth2
DROP TABLE IF EXISTS `system_oauth2_access_token`;
CREATE TABLE `system_oauth2_access_token`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `user_type` tinyint NOT NULL COMMENT '用户类型',
  `user_info` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户信息',
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '访问令牌',
  `refresh_token` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '刷新令牌',
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户端编号',
  `scopes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '授权范围',
  `expires_time` datetime NOT NULL COMMENT '过期时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  INDEX `idx_access_token`(`access_token` ASC),
  INDEX `idx_refresh_token`(`refresh_token` ASC)
) ENGINE = InnoDB COMMENT = 'OAuth2 访问令牌';

DROP TABLE IF EXISTS `system_oauth2_approve`;
CREATE TABLE `system_oauth2_approve`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `user_type` tinyint NOT NULL COMMENT '用户类型',
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户端编号',
  `scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '授权范围',
  `approved` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否接受',
  `expires_time` datetime NOT NULL COMMENT '过期时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = 'OAuth2 批准表';

DROP TABLE IF EXISTS `system_oauth2_client`;
CREATE TABLE `system_oauth2_client`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户端编号',
  `secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户端密钥',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '应用名',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '应用图标',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '应用描述',
  `status` tinyint NOT NULL COMMENT '状态',
  `access_token_validity_seconds` int NOT NULL COMMENT '访问令牌的有效期',
  `refresh_token_validity_seconds` int NOT NULL COMMENT '刷新令牌的有效期',
  `redirect_uris` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '可重定向的 URI 地址',
  `authorized_grant_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '授权类型',
  `scopes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '授权范围',
  `auto_approve_scopes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '自动通过的授权范围',
  `authorities` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限',
  `resource_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '资源',
  `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '附加信息',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = 'OAuth2 客户端表';

DROP TABLE IF EXISTS `system_oauth2_code`;
CREATE TABLE `system_oauth2_code`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `user_type` tinyint NOT NULL COMMENT '用户类型',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '授权码',
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户端编号',
  `scopes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '授权范围',
  `expires_time` datetime NOT NULL COMMENT '过期时间',
  `redirect_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '可重定向的 URI 地址',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '状态',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = 'OAuth2 授权码表';

DROP TABLE IF EXISTS `system_oauth2_refresh_token`;
CREATE TABLE `system_oauth2_refresh_token`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `refresh_token` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '刷新令牌',
  `user_type` tinyint NOT NULL COMMENT '用户类型',
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户端编号',
  `scopes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '授权范围',
  `expires_time` datetime NOT NULL COMMENT '过期时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = 'OAuth2 刷新令牌';

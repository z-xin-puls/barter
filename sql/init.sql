-- ============================================
-- 校园闲置物品互助交换平台 - 数据库初始化脚本
-- 数据库: barter_db
-- ============================================

CREATE DATABASE IF NOT EXISTS barter_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE barter_db;

-- 用户表 sys_user
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(30) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 物品分类表 item_category
CREATE TABLE IF NOT EXISTS `item_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_name` varchar(64) NOT NULL COMMENT '分类名称',
  `sort` int DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_name` (`category_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物品分类表';

-- 闲置物品表 idle_item
CREATE TABLE IF NOT EXISTS `idle_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '发布者用户id',
  `category_id` bigint NOT NULL COMMENT '分类id',
  `item_name` varchar(100) NOT NULL COMMENT '物品名称',
  `item_desc` text COMMENT '物品描述、成色',
  `hope_exchange` varchar(200) DEFAULT NULL COMMENT '希望交换什么物品',
  `status` tinyint DEFAULT 1 COMMENT '1正常上架，2已交换完成，3下架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='闲置物品表';

-- 交换申请表 exchange_apply
CREATE TABLE IF NOT EXISTS `exchange_apply` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apply_user_id` bigint NOT NULL COMMENT '申请人id',
  `item_id` bigint NOT NULL COMMENT '目标闲置物品id',
  `message` varchar(255) DEFAULT NULL COMMENT '申请留言',
  `apply_status` tinyint DEFAULT 0 COMMENT '0待处理 1同意 2拒绝',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交换申请表';

-- AI对话记录表 ai_chat_record
CREATE TABLE IF NOT EXISTS `ai_chat_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '提问用户ID',
  `user_content` text NOT NULL COMMENT '用户提问内容',
  `ai_content` text NOT NULL COMMENT 'AI返回回答',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI聊天记录';

-- 初始化分类数据
INSERT INTO item_category(category_name, sort) VALUES
('教材书籍', 1),
('数码产品', 2),
('运动器材', 3),
('生活用品', 4);

-- 测试数据: 插入一个测试用户 (用户名test, 密码123456)
INSERT INTO sys_user (username, password, real_name, phone) VALUES
('test', '123456', '测试用户', '13800138000');

-- 测试数据: 插入几条闲置物品
INSERT INTO idle_item (user_id, category_id, item_name, item_desc, hope_exchange, status) VALUES
(1, 1, '高等数学教材（同济版）', '九成新，无笔记，适合大一新生使用', '大学物理教材', 1),
(1, 2, '罗技无线鼠标', '使用半年，功能完好，附带接收器', '任何品牌有线鼠标', 1),
(1, 3, '迪卡侬羽毛球拍', '用了几次，成色很好，附拍套', '乒乓球拍', 1),
(1, 4, '宿舍小台灯', 'USB充电款，三档调光，自用半年', '宿舍收纳盒', 1);

SELECT '数据库初始化完成！' AS info;

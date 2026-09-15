-- ============================================
-- 校园闲置物品互助交换平台 - 数据库初始化脚本
-- 数据库: barter_db
-- ============================================

CREATE DATABASE IF NOT EXISTS barter_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE barter_db;

-- 用户表 sys_user（普通用户/学生）
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(100) NOT NULL COMMENT 'BCrypt加密密码',
  `real_name` varchar(30) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `student_no` varchar(30) DEFAULT NULL COMMENT '学号',
  `department` varchar(100) DEFAULT NULL COMMENT '院系',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像URL',
  `status` tinyint DEFAULT 1 COMMENT '1正常 0封禁',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='普通用户表';

-- 管理员表 admin_user
CREATE TABLE IF NOT EXISTS `admin_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '管理员账号',
  `password` varchar(100) NOT NULL COMMENT 'BCrypt加密密码',
  `real_name` varchar(30) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像URL',
  `status` tinyint DEFAULT 1 COMMENT '1正常 0禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_admin_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 物品分类表 item_category
CREATE TABLE IF NOT EXISTS `item_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_name` varchar(64) NOT NULL COMMENT '分类名称',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标(emoji)',
  `parent_id` bigint DEFAULT NULL COMMENT '父分类ID',
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
  `item_desc` text COMMENT '物品描述',
  `hope_exchange` varchar(200) DEFAULT NULL COMMENT '希望交换什么物品',
  `images` varchar(1000) DEFAULT NULL COMMENT '图片URL JSON数组',
  `item_condition` varchar(20) DEFAULT NULL COMMENT '成色:全新/轻微使用/明显使用',
  `campus` varchar(100) DEFAULT NULL COMMENT '校区/交易地点',
  `view_count` int DEFAULT 0 COMMENT '浏览量',
  `status` tinyint DEFAULT 1 COMMENT '1正常上架 2已交换完成 3下架',
  `audit_status` tinyint DEFAULT 0 COMMENT '审核状态:0待审核 1已通过 2已驳回',
  `audit_remark` varchar(255) DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_audit_status` (`audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='闲置物品表';

-- 交换申请表 exchange_apply
CREATE TABLE IF NOT EXISTS `exchange_apply` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apply_user_id` bigint NOT NULL COMMENT '申请人id',
  `item_id` bigint NOT NULL COMMENT '目标闲置物品id',
  `owner_user_id` bigint NOT NULL COMMENT '物品发布者id(冗余)',
  `message` varchar(500) DEFAULT NULL COMMENT '申请留言',
  `apply_status` tinyint DEFAULT 0 COMMENT '0待处理 1已同意 2已拒绝 3已完成',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_apply_user` (`apply_user_id`),
  KEY `idx_owner_user` (`owner_user_id`),
  KEY `idx_item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交换申请表';

-- AI对话记录表 ai_chat_record
CREATE TABLE IF NOT EXISTS `ai_chat_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '提问用户ID',
  `user_content` text NOT NULL COMMENT '用户提问内容',
  `ai_content` text NOT NULL COMMENT 'AI返回回答',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI聊天记录';

-- 通知表 notification
CREATE TABLE IF NOT EXISTS `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '接收用户ID',
  `type` varchar(20) NOT NULL COMMENT '类型:apply/agree/reject/complete/report/system',
  `biz_id` bigint DEFAULT NULL COMMENT '关联业务ID',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` varchar(500) DEFAULT NULL COMMENT '内容',
  `is_read` tinyint DEFAULT 0 COMMENT '0未读 1已读',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_read` (`user_id`, `is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- 评价表 review
CREATE TABLE IF NOT EXISTS `review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apply_id` bigint NOT NULL COMMENT '交换申请ID',
  `item_id` bigint NOT NULL COMMENT '物品ID',
  `from_user_id` bigint NOT NULL COMMENT '评价人ID',
  `to_user_id` bigint NOT NULL COMMENT '被评价人ID',
  `rating` tinyint NOT NULL COMMENT '评分1-5',
  `content` varchar(500) DEFAULT NULL COMMENT '评价内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_to_user` (`to_user_id`),
  KEY `idx_apply` (`apply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交换评价表';

-- 举报表 report
CREATE TABLE IF NOT EXISTS `report` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reporter_id` bigint NOT NULL COMMENT '举报人ID',
  `target_type` varchar(20) NOT NULL COMMENT '目标类型:item/user',
  `target_id` bigint NOT NULL COMMENT '目标ID',
  `reason` varchar(100) NOT NULL COMMENT '举报原因',
  `description` varchar(500) DEFAULT NULL COMMENT '详细描述',
  `status` tinyint DEFAULT 0 COMMENT '0待处理 1已处理 2已驳回',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='举报表';

-- 收藏表 favorite
CREATE TABLE IF NOT EXISTS `favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `item_id` bigint NOT NULL COMMENT '物品ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_item` (`user_id`, `item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 初始化分类数据（含图标）
INSERT INTO item_category(category_name, icon, sort) VALUES
('教材书籍', '📚', 1),
('数码产品', '💻', 2),
('运动器材', '🏀', 3),
('生活用品', '🏠', 4);

-- 注意：默认用户由应用启动时 DataInitializer 自动创建（BCrypt加密）
-- test/123456 (普通用户 → sys_user)  admin/admin123 (管理员 → admin_user)

-- 测试数据: 闲置物品（user_id=1 为 test 用户，audit_status=1 直接通过审核）
INSERT INTO idle_item (user_id, category_id, item_name, item_desc, hope_exchange, item_condition, campus, view_count, status, audit_status) VALUES
(1, 1, '高等数学教材（同济版）', '九成新，无笔记，适合大一新生使用', '大学物理教材', '轻微使用', '主校区', 12, 1, 1),
(1, 2, '罗技无线鼠标', '使用半年，功能完好，附带接收器', '任何品牌有线鼠标', '轻微使用', '东校区', 8, 1, 1),
(1, 3, '迪卡侬羽毛球拍', '用了几次，成色很好，附拍套', '乒乓球拍', '全新', '主校区', 25, 1, 1),
(1, 4, '宿舍小台灯', 'USB充电款，三档调光，自用半年', '宿舍收纳盒', '明显使用', '西校区', 5, 1, 1);

SELECT '数据库初始化完成！默认用户请启动应用后自动创建' AS info;

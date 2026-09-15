-- ============================================
-- 校园闲置物品互助交换平台 - 增量迁移脚本
-- 日期: 2026-09-15
-- 说明: 用户聊天功能 + 交易时间地点
-- 前置条件: 已执行 init.sql
-- ============================================

USE barter_db;

-- 1. exchange_apply 表新增交易时间/地点/填写方字段
ALTER TABLE `exchange_apply`
  ADD COLUMN `trade_time` datetime DEFAULT NULL COMMENT '约定交易时间' AFTER `message`,
  ADD COLUMN `trade_location` varchar(200) DEFAULT NULL COMMENT '约定交易地点' AFTER `trade_time`,
  ADD COLUMN `trade_confirmer` tinyint DEFAULT NULL COMMENT '填写方：0申请人 1物品发布者' AFTER `trade_location`;

-- 2. 新建用户聊天消息表
CREATE TABLE IF NOT EXISTS `chat_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sender_id` bigint NOT NULL COMMENT '发送者ID',
  `receiver_id` bigint NOT NULL COMMENT '接收者ID',
  `apply_id` bigint DEFAULT NULL COMMENT '关联交换申请ID（可空，通用聊天无关联）',
  `content` varchar(1000) NOT NULL COMMENT '消息内容',
  `is_read` tinyint DEFAULT 0 COMMENT '0未读 1已读',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_receiver_read` (`receiver_id`, `is_read`),
  KEY `idx_sender` (`sender_id`),
  KEY `idx_apply` (`apply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户聊天消息表';

SELECT '迁移完成：exchange_apply 新增 trade_time/trade_location/trade_confirmer，新建 chat_message 表' AS info;

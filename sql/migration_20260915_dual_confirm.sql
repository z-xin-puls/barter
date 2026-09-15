-- ============================================
-- 校园闲置物品互助交换平台 - 增量迁移脚本
-- 日期: 2026-09-15-2
-- 说明: 交换确认改为双方确认制
-- 前置条件: 已执行 init.sql + migration_20260915_chat_and_trade.sql
-- ============================================

USE barter_db;

-- exchange_apply 表新增双方确认字段
ALTER TABLE `exchange_apply`
  ADD COLUMN `apply_confirmed` tinyint DEFAULT 0 COMMENT '申请人是否确认完成:0否 1是' AFTER `trade_confirmer`,
  ADD COLUMN `owner_confirmed` tinyint DEFAULT 0 COMMENT '发布者是否确认完成:0否 1是' AFTER `apply_confirmed`;

SELECT '迁移完成：exchange_apply 新增 apply_confirmed/owner_confirmed 字段（双方确认制）' AS info;

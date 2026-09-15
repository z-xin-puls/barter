-- ============================================
-- 校园闲置物品互助交换平台 - 增量迁移脚本
-- 日期: 2026-09-15-3
-- 说明: 交易时间地点改为"一方提出、另一方确认/反对"模式
-- 前置条件: 已执行 init.sql + migration_20260915_chat_and_trade.sql + migration_20260915_dual_confirm.sql
-- ============================================

USE barter_db;

-- exchange_apply 表新增交易确认状态字段
ALTER TABLE `exchange_apply`
  ADD COLUMN `trade_status` tinyint DEFAULT 0 COMMENT '交易信息状态:0未填写 1待对方确认 2双方已确认' AFTER `trade_confirmer`;

-- 存量数据：已有交易时间但无状态的记录，视为"待对方确认"
UPDATE `exchange_apply` SET `trade_status` = 1 WHERE `trade_time` IS NOT NULL AND `trade_status` = 0;

SELECT '迁移完成：exchange_apply 新增 trade_status 字段（交易信息确认制）' AS info;

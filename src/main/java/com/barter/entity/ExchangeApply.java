package com.barter.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 交换申请实体（纯表映射，关联字段见 VO）
 */
@Data
public class ExchangeApply {
    private Long id;
    private Long applyUserId;
    private Long itemId;
    /** 冗余：物品发布者ID */
    private Long ownerUserId;
    private String message;
    /** 0待处理 1已同意 2已拒绝 3已完成（双方确认） */
    private Integer applyStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

package com.barter.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 交换申请实体
 */
@Data
public class ExchangeApply {
    private Long id;
    private Long applyUserId;
    private Long itemId;
    private String message;
    /** 0待处理 1同意 2拒绝 */
    private Integer applyStatus;
    private LocalDateTime createTime;

    // 关联字段
    private String applyUsername;
    private String itemName;
}

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
    /** 约定交易时间 */
    private LocalDateTime tradeTime;
    /** 约定交易地点 */
    private String tradeLocation;
    /** 填写方：0申请人 1物品发布者 */
    private Integer tradeConfirmer;
    /** 交易信息状态:0未填写 1待对方确认 2双方已确认 */
    private Integer tradeStatus;
    /** 申请人是否确认完成:0否 1是 */
    private Integer applyConfirmed;
    /** 发布者是否确认完成:0否 1是 */
    private Integer ownerConfirmed;
    /** 0待处理 1已同意 2已拒绝 3已完成（双方确认） */
    private Integer applyStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

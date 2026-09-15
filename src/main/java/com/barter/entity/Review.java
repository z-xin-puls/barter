package com.barter.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 交换评价实体
 */
@Data
public class Review {
    private Long id;
    /** 交换申请ID */
    private Long applyId;
    /** 物品ID */
    private Long itemId;
    /** 评价人ID */
    private Long fromUserId;
    /** 被评价人ID */
    private Long toUserId;
    /** 评分 1-5 */
    private Integer rating;
    /** 评价内容 */
    private String content;
    private LocalDateTime createTime;
}

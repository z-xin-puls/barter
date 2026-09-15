package com.barter.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知实体
 */
@Data
public class Notification {
    private Long id;
    /** 接收通知的用户ID */
    private Long userId;
    /** 通知类型：apply/agree/reject/complete/report/system */
    private String type;
    /** 关联业务ID（如申请ID） */
    private Long bizId;
    /** 通知标题 */
    private String title;
    /** 通知内容 */
    private String content;
    /** 0未读 1已读 */
    private Integer isRead;
    private LocalDateTime createTime;
}

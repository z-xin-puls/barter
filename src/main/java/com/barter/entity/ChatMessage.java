package com.barter.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户聊天消息实体
 */
@Data
public class ChatMessage {
    private Long id;
    private Long senderId;
    private Long receiverId;
    /** 关联交换申请ID（可空，通用聊天无关联） */
    private Long applyId;
    private String content;
    /** 0未读 1已读 */
    private Integer isRead;
    private LocalDateTime createTime;
}

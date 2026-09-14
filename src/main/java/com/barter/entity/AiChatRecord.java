package com.barter.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI对话记录实体
 */
@Data
public class AiChatRecord {
    private Long id;
    private Long userId;
    private String userContent;
    private String aiContent;
    private LocalDateTime createTime;
}

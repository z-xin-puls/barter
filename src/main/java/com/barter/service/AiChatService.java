package com.barter.service;

import com.barter.entity.AiChatRecord;

import java.util.List;

/**
 * AI对话服务接口
 */
public interface AiChatService {

    /**
     * AI对话
     */
    String chat(Long userId, String content);

    /**
     * 查询当前用户历史对话记录
     */
    List<AiChatRecord> history(Long userId);
}

package com.barter.service;

import com.barter.entity.ChatMessage;

import java.util.List;

/**
 * 聊天服务接口
 */
public interface ChatMessageService {

    /** 发送消息 */
    void sendMessage(Long senderId, Long receiverId, Long applyId, String content);

    /** 获取与某人的聊天记录 */
    List<ChatMessage> getConversation(Long userId, Long otherUserId);

    /** 未读消息数 */
    int countUnread(Long userId);

    /** 标记与某人的消息为已读 */
    void markRead(Long userId, Long otherUserId);
}

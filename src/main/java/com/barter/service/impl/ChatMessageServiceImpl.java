package com.barter.service.impl;

import com.barter.common.BusinessException;
import com.barter.entity.ChatMessage;
import com.barter.mapper.ChatMessageMapper;
import com.barter.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Override
    public void sendMessage(Long senderId, Long receiverId, Long applyId, String content) {
        if (receiverId == null) {
            throw new BusinessException("接收者不能为空");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException("消息内容不能为空");
        }
        ChatMessage msg = new ChatMessage();
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setApplyId(applyId);
        msg.setContent(content.trim());
        chatMessageMapper.insert(msg);
    }

    @Override
    public List<ChatMessage> getConversation(Long userId, Long otherUserId) {
        return chatMessageMapper.selectConversation(userId, otherUserId);
    }

    @Override
    public int countUnread(Long userId) {
        return chatMessageMapper.countUnread(userId);
    }

    @Override
    public void markRead(Long userId, Long otherUserId) {
        chatMessageMapper.markRead(userId, otherUserId);
    }
}

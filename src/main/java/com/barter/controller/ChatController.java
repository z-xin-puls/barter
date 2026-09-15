package com.barter.controller;

import com.barter.common.Result;
import com.barter.entity.ChatMessage;
import com.barter.service.ChatMessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天控制器
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatMessageService chatService;

    /**
     * 发送消息
     * body: { receiverId, applyId?, content }
     */
    @PostMapping("/send")
    public Result<?> send(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long senderId = (Long) request.getAttribute("userId");
        Long receiverId = toLong(body.get("receiverId"));
        Long applyId = toLong(body.get("applyId"));
        String content = (String) body.get("content");
        chatService.sendMessage(senderId, receiverId, applyId, content);
        return Result.success("发送成功", null);
    }

    /**
     * 获取与某人的聊天记录
     */
    @GetMapping("/conversation/{otherUserId}")
    public Result<List<ChatMessage>> conversation(@PathVariable Long otherUserId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<ChatMessage> messages = chatService.getConversation(userId, otherUserId);
        // 标记已读
        chatService.markRead(userId, otherUserId);
        return Result.success(messages);
    }

    /**
     * 未读消息数
     */
    @GetMapping("/unread/count")
    public Result<Map<String, Object>> unreadCount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        int count = chatService.countUnread(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("count", count);
        return Result.success(data);
    }

    private Long toLong(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Number) return ((Number) obj).longValue();
        try { return Long.parseLong(obj.toString()); } catch (NumberFormatException e) { return null; }
    }
}

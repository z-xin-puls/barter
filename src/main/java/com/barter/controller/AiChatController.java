package com.barter.controller;

import com.barter.common.Result;
import com.barter.dto.AiChatDTO;
import com.barter.entity.AiChatRecord;
import com.barter.service.AiChatService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI助手控制器
 */
@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    @Autowired
    private AiChatService aiChatService;

    /**
     * AI 对话接口
     */
    @PostMapping("/chat")
    public Result<String> chat(@RequestBody AiChatDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String reply = aiChatService.chat(userId, dto.getContent());
        return Result.success(reply);
    }

    /**
     * 获取当前用户历史聊天记录
     */
    @GetMapping("/record/list")
    public Result<List<AiChatRecord>> history(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<AiChatRecord> list = aiChatService.history(userId);
        return Result.success(list);
    }
}

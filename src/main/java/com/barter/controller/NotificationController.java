package com.barter.controller;

import com.barter.common.Result;
import com.barter.entity.Notification;
import com.barter.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/list")
    public Result<List<Notification>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(notificationService.list(userId));
    }

    @GetMapping("/unread-count")
    public Result<Map<String, Object>> unreadCount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(notificationService.unreadCount(userId));
    }

    @PutMapping("/read/{id}")
    public Result<?> markRead(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        notificationService.markRead(id, userId);
        return Result.success();
    }

    @PutMapping("/read-all")
    public Result<?> markAllRead(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        notificationService.markAllRead(userId);
        return Result.success();
    }
}

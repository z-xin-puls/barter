package com.barter.service;

import com.barter.entity.Notification;

import java.util.List;
import java.util.Map;

public interface NotificationService {

    void send(Long userId, String type, Long bizId, String title, String content);

    List<Notification> list(Long userId);

    Map<String, Object> unreadCount(Long userId);

    void markRead(Long id, Long userId);

    void markAllRead(Long userId);
}

package com.barter.service.impl;

import com.barter.entity.Notification;
import com.barter.mapper.NotificationMapper;
import com.barter.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public void send(Long userId, String type, Long bizId, String title, String content) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setType(type);
        n.setBizId(bizId);
        n.setTitle(title);
        n.setContent(content);
        n.setIsRead(0);
        notificationMapper.insert(n);
    }

    @Override
    public List<Notification> list(Long userId) {
        return notificationMapper.selectByUserId(userId);
    }

    @Override
    public Map<String, Object> unreadCount(Long userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("count", notificationMapper.countUnread(userId));
        return map;
    }

    @Override
    public void markRead(Long id, Long userId) {
        notificationMapper.markAsRead(id, userId);
    }

    @Override
    public void markAllRead(Long userId) {
        notificationMapper.markAllAsRead(userId);
    }
}

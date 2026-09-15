package com.barter.mapper;

import com.barter.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {

    int insert(Notification notification);

    List<Notification> selectByUserId(@Param("userId") Long userId);

    /** 未读数 */
    int countUnread(@Param("userId") Long userId);

    /** 标记单条已读 */
    int markAsRead(@Param("id") Long id, @Param("userId") Long userId);

    /** 全部标记已读 */
    int markAllAsRead(@Param("userId") Long userId);
}

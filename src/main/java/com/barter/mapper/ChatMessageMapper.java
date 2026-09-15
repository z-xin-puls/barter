package com.barter.mapper;

import com.barter.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 聊天消息Mapper
 */
@Mapper
public interface ChatMessageMapper {

    int insert(ChatMessage msg);

    /** 获取与某人的聊天记录（双向） */
    List<ChatMessage> selectConversation(@Param("userId") Long userId,
                                         @Param("otherUserId") Long otherUserId);

    /** 未读消息数 */
    int countUnread(@Param("userId") Long userId);

    /** 标记与某人的消息为已读 */
    int markRead(@Param("userId") Long userId, @Param("otherUserId") Long otherUserId);
}

package com.barter.mapper;

import com.barter.entity.AiChatRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI对话记录Mapper
 */
@Mapper
public interface AiChatRecordMapper {

    int insert(AiChatRecord record);

    /**
     * 查询用户历史对话记录
     */
    List<AiChatRecord> selectByUserId(@Param("userId") Long userId);
}

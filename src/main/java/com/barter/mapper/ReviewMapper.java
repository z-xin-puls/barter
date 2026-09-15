package com.barter.mapper;

import com.barter.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReviewMapper {

    int insert(Review review);

    /** 查询某用户收到的评价（含评价人信息） */
    List<Map<String, Object>> selectByToUserId(@Param("toUserId") Long toUserId);

    /** 查询某申请关联的评价 */
    List<Review> selectByApplyId(@Param("applyId") Long applyId);

    /** 用户平均评分 */
    Double selectAvgRating(@Param("toUserId") Long toUserId);
}

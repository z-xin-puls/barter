package com.barter.mapper;

import com.barter.entity.ExchangeApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 交换申请Mapper
 */
@Mapper
public interface ExchangeApplyMapper {

    int insert(ExchangeApply apply);

    /**
     * 查询我发起的全部申请
     */
    List<ExchangeApply> selectMyApply(@Param("applyUserId") Long applyUserId);

    /**
     * 查询我收到的申请（发布者视角）
     */
    List<ExchangeApply> selectReceivedApply(@Param("userId") Long userId);

    /**
     * 处理申请
     */
    int updateStatus(@Param("id") Long id, @Param("applyStatus") Integer applyStatus);

    /**
     * 根据ID查询
     */
    ExchangeApply selectById(Long id);

    /** 管理员：查询全部申请 */
    List<ExchangeApply> selectAll();
}

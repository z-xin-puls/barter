package com.barter.mapper;

import com.barter.entity.ExchangeApply;
import com.barter.vo.ExchangeApplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 交换申请Mapper
 */
@Mapper
public interface ExchangeApplyMapper {

    int insert(ExchangeApply apply);

    /** 我发起的申请（含物品和发布者信息） */
    List<ExchangeApplyVO> selectMyApply(@Param("applyUserId") Long applyUserId);

    /** 我收到的申请（含申请人和物品信息） */
    List<ExchangeApplyVO> selectReceivedApply(@Param("ownerUserId") Long ownerUserId);

    int updateStatus(@Param("id") Long id, @Param("applyStatus") Integer applyStatus);

    /** 基础查询 */
    ExchangeApply selectById(Long id);

    /** 详情（含关联信息） */
    ExchangeApplyVO selectVOById(Long id);

    /** 管理后台：全部申请 */
    List<ExchangeApplyVO> selectAll();

    /** 统计申请总数 */
    int countAll();
}

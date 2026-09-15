package com.barter.service;

import com.barter.dto.ExchangeApplyDTO;
import com.barter.dto.ExchangeHandleDTO;
import com.barter.vo.ExchangeApplyVO;

import java.util.List;

/**
 * 交换申请服务接口
 */
public interface ExchangeApplyService {

    void apply(Long userId, ExchangeApplyDTO dto);

    List<ExchangeApplyVO> myApply(Long userId);

    List<ExchangeApplyVO> receivedApply(Long userId);

    /** 发布者同意/拒绝 */
    void handle(Long userId, ExchangeHandleDTO dto);

    /** 申请人确认交换完成 */
    void confirm(Long userId, Long applyId);

    /** 填写/更新交易时间和地点 */
    void updateTrade(Long userId, Long applyId, String tradeTime, String tradeLocation);

    /** 确认对方提出的交易时间地点 */
    void confirmTrade(Long userId, Long applyId);
}

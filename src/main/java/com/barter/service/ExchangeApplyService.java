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
}

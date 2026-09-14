package com.barter.service;

import com.barter.dto.ExchangeApplyDTO;
import com.barter.dto.ExchangeHandleDTO;
import com.barter.entity.ExchangeApply;

import java.util.List;

/**
 * 交换申请服务接口
 */
public interface ExchangeApplyService {

    /**
     * 发起交换申请
     */
    void apply(Long userId, ExchangeApplyDTO dto);

    /**
     * 查询我发起的全部申请记录
     */
    List<ExchangeApply> myApply(Long userId);

    /**
     * 查询我收到的申请
     */
    List<ExchangeApply> receivedApply(Long userId);

    /**
     * 处理申请（同意/拒绝）
     */
    void handle(Long userId, ExchangeHandleDTO dto);
}

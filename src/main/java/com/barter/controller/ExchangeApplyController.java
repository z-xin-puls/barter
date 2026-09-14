package com.barter.controller;

import com.barter.common.Result;
import com.barter.dto.ExchangeApplyDTO;
import com.barter.dto.ExchangeHandleDTO;
import com.barter.entity.ExchangeApply;
import com.barter.service.ExchangeApplyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 交换申请控制器
 */
@RestController
@RequestMapping("/api/exchange")
public class ExchangeApplyController {

    @Autowired
    private ExchangeApplyService applyService;

    /**
     * 发起交换申请
     */
    @PostMapping("/apply")
    public Result<?> apply(@RequestBody ExchangeApplyDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        applyService.apply(userId, dto);
        return Result.success("申请成功", null);
    }

    /**
     * 查询我发起的全部申请记录
     */
    @GetMapping("/my")
    public Result<List<ExchangeApply>> my(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<ExchangeApply> list = applyService.myApply(userId);
        return Result.success(list);
    }

    /**
     * 查询我收到的申请（用于处理）
     */
    @GetMapping("/received")
    public Result<List<ExchangeApply>> received(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<ExchangeApply> list = applyService.receivedApply(userId);
        return Result.success(list);
    }

    /**
     * 处理申请（同意/拒绝）
     */
    @PutMapping("/handle")
    public Result<?> handle(@RequestBody ExchangeHandleDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        applyService.handle(userId, dto);
        return Result.success("处理成功", null);
    }
}

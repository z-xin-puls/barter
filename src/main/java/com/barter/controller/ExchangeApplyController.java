package com.barter.controller;

import com.barter.common.Result;
import com.barter.dto.ExchangeApplyDTO;
import com.barter.dto.ExchangeHandleDTO;
import com.barter.service.ExchangeApplyService;
import com.barter.vo.ExchangeApplyVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeApplyController {

    @Autowired
    private ExchangeApplyService applyService;

    @PostMapping("/apply")
    public Result<?> apply(@RequestBody ExchangeApplyDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        applyService.apply(userId, dto);
        return Result.success("申请成功", null);
    }

    @GetMapping("/my")
    public Result<List<ExchangeApplyVO>> my(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(applyService.myApply(userId));
    }

    @GetMapping("/received")
    public Result<List<ExchangeApplyVO>> received(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(applyService.receivedApply(userId));
    }

    @PutMapping("/handle")
    public Result<?> handle(@RequestBody ExchangeHandleDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        applyService.handle(userId, dto);
        return Result.success("处理成功", null);
    }

    /** 申请人确认交换完成 */
    @PutMapping("/confirm/{id}")
    public Result<?> confirm(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        applyService.confirm(userId, id);
        return Result.success("交换已确认完成", null);
    }

    /** 填写/更新交易时间和地点 */
    @PutMapping("/trade/{id}")
    public Result<?> updateTrade(@PathVariable Long id, @RequestBody java.util.Map<String, String> body,
                                 HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        applyService.updateTrade(userId, id, body.get("tradeTime"), body.get("tradeLocation"));
        return Result.success("交易信息已提交，请等待对方确认", null);
    }

    /** 确认对方提出的交易时间地点 */
    @PutMapping("/trade/confirm/{id}")
    public Result<?> confirmTrade(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        applyService.confirmTrade(userId, id);
        return Result.success("已确认交易信息", null);
    }
}

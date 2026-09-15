package com.barter.controller;

import com.barter.common.Result;
import com.barter.dto.ItemAddDTO;
import com.barter.dto.ItemQueryDTO;
import com.barter.service.IdleItemService;
import com.barter.vo.IdleItemVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 闲置物品控制器
 */
@RestController
@RequestMapping("/api/item")
public class IdleItemController {

    @Autowired
    private IdleItemService itemService;

    @PostMapping("/add")
    public Result<?> add(@RequestBody ItemAddDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        itemService.add(userId, dto);
        return Result.success("发布成功", null);
    }

    /**
     * 分页查询（支持关键词搜索、分类、成色、校区筛选）
     */
    @GetMapping("/page")
    public Result<Map<String, Object>> page(ItemQueryDTO query, HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("userId");
        return Result.success(itemService.page(query, currentUserId));
    }

    /**
     * 物品详情
     */
    @GetMapping("/detail/{id}")
    public Result<IdleItemVO> detail(@PathVariable Long id, HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("userId");
        return Result.success(itemService.detail(id, currentUserId));
    }

    @PutMapping("/off/{id}")
    public Result<?> off(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        itemService.off(userId, id);
        return Result.success("下架成功", null);
    }

    @GetMapping("/my")
    public Result<List<IdleItemVO>> myItems(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(itemService.myItems(userId));
    }
}

package com.barter.controller;

import com.barter.common.Result;
import com.barter.dto.ItemAddDTO;
import com.barter.entity.IdleItem;
import com.barter.service.IdleItemService;
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

    /**
     * 发布闲置物品
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody ItemAddDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        itemService.add(userId, dto);
        return Result.success("发布成功", null);
    }

    /**
     * 分页查询闲置物品
     */
    @GetMapping("/page")
    public Result<Map<String, Object>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long categoryId) {
        Map<String, Object> data = itemService.page(pageNum, pageSize, categoryId);
        return Result.success(data);
    }

    /**
     * 下架物品
     */
    @PutMapping("/off/{id}")
    public Result<?> off(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        itemService.off(userId, id);
        return Result.success("下架成功", null);
    }

    /**
     * 查询我的发布
     */
    @GetMapping("/my")
    public Result<List<IdleItem>> myItems(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<IdleItem> list = itemService.myItems(userId);
        return Result.success(list);
    }
}

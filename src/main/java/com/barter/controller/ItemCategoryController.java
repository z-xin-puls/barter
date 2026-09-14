package com.barter.controller;

import com.barter.common.Result;
import com.barter.entity.ItemCategory;
import com.barter.service.ItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物品分类控制器
 */
@RestController
@RequestMapping("/api/category")
public class ItemCategoryController {

    @Autowired
    private ItemCategoryService categoryService;

    /**
     * 查询全部物品分类
     */
    @GetMapping("/list")
    public Result<List<ItemCategory>> list() {
        List<ItemCategory> list = categoryService.list();
        return Result.success(list);
    }
}

package com.barter.service.impl;

import com.barter.entity.ItemCategory;
import com.barter.mapper.ItemCategoryMapper;
import com.barter.service.ItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 物品分类服务实现
 */
@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {

    @Autowired
    private ItemCategoryMapper categoryMapper;

    @Override
    public List<ItemCategory> list() {
        return categoryMapper.selectAll();
    }
}

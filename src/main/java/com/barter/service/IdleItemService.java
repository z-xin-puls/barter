package com.barter.service;

import com.barter.dto.ItemAddDTO;
import com.barter.entity.IdleItem;

import java.util.List;
import java.util.Map;

/**
 * 闲置物品服务接口
 */
public interface IdleItemService {

    /**
     * 发布闲置物品
     */
    void add(Long userId, ItemAddDTO dto);

    /**
     * 分页查询闲置物品
     */
    Map<String, Object> page(Integer pageNum, Integer pageSize, Long categoryId);

    /**
     * 下架闲置物品
     */
    void off(Long userId, Long id);

    /**
     * 查询我的发布
     */
    List<IdleItem> myItems(Long userId);
}

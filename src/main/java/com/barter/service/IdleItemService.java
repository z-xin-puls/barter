package com.barter.service;

import com.barter.dto.ItemAddDTO;
import com.barter.dto.ItemQueryDTO;
import com.barter.vo.IdleItemVO;

import java.util.List;
import java.util.Map;

/**
 * 闲置物品服务接口
 */
public interface IdleItemService {

    void add(Long userId, ItemAddDTO dto);

    /** 分页搜索 */
    Map<String, Object> page(ItemQueryDTO query, Long currentUserId);

    /** 物品详情（浏览量+1） */
    IdleItemVO detail(Long id, Long currentUserId);

    void off(Long userId, Long id);

    List<IdleItemVO> myItems(Long userId);
}

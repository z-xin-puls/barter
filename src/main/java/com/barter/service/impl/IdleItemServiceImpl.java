package com.barter.service.impl;

import com.barter.dto.ItemAddDTO;
import com.barter.entity.IdleItem;
import com.barter.mapper.IdleItemMapper;
import com.barter.service.IdleItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 闲置物品服务实现
 */
@Service
public class IdleItemServiceImpl implements IdleItemService {

    @Autowired
    private IdleItemMapper itemMapper;

    @Override
    public void add(Long userId, ItemAddDTO dto) {
        if (dto.getItemName() == null || dto.getItemName().trim().isEmpty()) {
            throw new RuntimeException("物品名称不能为空");
        }
        if (dto.getCategoryId() == null) {
            throw new RuntimeException("请选择分类");
        }
        IdleItem item = new IdleItem();
        item.setUserId(userId);
        item.setCategoryId(dto.getCategoryId());
        item.setItemName(dto.getItemName());
        item.setItemDesc(dto.getItemDesc());
        item.setHopeExchange(dto.getHopeExchange());
        item.setStatus(1); // 正常上架
        itemMapper.insert(item);
    }

    @Override
    public Map<String, Object> page(Integer pageNum, Integer pageSize, Long categoryId) {
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;

        int offset = (pageNum - 1) * pageSize;
        int total = itemMapper.selectCount(categoryId);
        List<IdleItem> records = itemMapper.selectPage(categoryId, offset, pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    public void off(Long userId, Long id) {
        IdleItem item = itemMapper.selectById(id);
        if (item == null) {
            throw new RuntimeException("物品不存在");
        }
        // 校验是否是自己发布的
        if (!item.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作，只能下架自己发布的物品");
        }
        if (item.getStatus() == 3) {
            throw new RuntimeException("物品已下架");
        }
        itemMapper.updateStatus(id, 3);
    }

    @Override
    public List<IdleItem> myItems(Long userId) {
        return itemMapper.selectByUserId(userId);
    }
}

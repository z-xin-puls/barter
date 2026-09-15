package com.barter.service.impl;

import com.barter.common.BusinessException;
import com.barter.dto.ItemAddDTO;
import com.barter.dto.ItemQueryDTO;
import com.barter.entity.IdleItem;
import com.barter.mapper.IdleItemMapper;
import com.barter.service.IdleItemService;
import com.barter.vo.IdleItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IdleItemServiceImpl implements IdleItemService {

    @Autowired
    private IdleItemMapper itemMapper;

    @Override
    public void add(Long userId, ItemAddDTO dto) {
        if (dto.getItemName() == null || dto.getItemName().trim().isEmpty()) {
            throw new BusinessException("物品名称不能为空");
        }
        if (dto.getCategoryId() == null) {
            throw new BusinessException("请选择分类");
        }
        IdleItem item = new IdleItem();
        item.setUserId(userId);
        item.setCategoryId(dto.getCategoryId());
        item.setItemName(dto.getItemName());
        item.setItemDesc(dto.getItemDesc());
        item.setHopeExchange(dto.getHopeExchange());
        item.setImages(dto.getImages());
        item.setItemCondition(dto.getItemCondition());
        item.setCampus(dto.getCampus());
        item.setViewCount(0);
        item.setStatus(1);
        item.setAuditStatus(0); // 默认待审核
        itemMapper.insert(item);
    }

    @Override
    public Map<String, Object> page(ItemQueryDTO query, Long currentUserId) {
        int pageNum = query.getPageNum() == null || query.getPageNum() < 1 ? 1 : query.getPageNum();
        int pageSize = query.getPageSize() == null || query.getPageSize() < 1 ? 10 : query.getPageSize();
        int offset = (pageNum - 1) * pageSize;

        int total = itemMapper.selectCount(query.getCategoryId(), query.getKeyword(),
                query.getItemCondition(), query.getCampus());
        List<IdleItemVO> records = itemMapper.selectPage(query.getCategoryId(), query.getKeyword(),
                query.getItemCondition(), query.getCampus(), offset, pageSize, currentUserId);

        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    public IdleItemVO detail(Long id, Long currentUserId) {
        IdleItemVO item = itemMapper.selectVOById(id, currentUserId);
        if (item == null) {
            throw new BusinessException("物品不存在");
        }
        itemMapper.incrementViewCount(id);
        item.setViewCount(item.getViewCount() == null ? 1 : item.getViewCount() + 1);
        return item;
    }

    @Override
    public void off(Long userId, Long id) {
        IdleItem item = itemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException("物品不存在");
        }
        if (!item.getUserId().equals(userId)) {
            throw new BusinessException("无权操作，只能下架自己发布的物品");
        }
        if (item.getStatus() == 3) {
            throw new BusinessException("物品已下架");
        }
        itemMapper.updateStatus(id, 3);
    }

    @Override
    public List<IdleItemVO> myItems(Long userId) {
        return itemMapper.selectByUserId(userId);
    }
}

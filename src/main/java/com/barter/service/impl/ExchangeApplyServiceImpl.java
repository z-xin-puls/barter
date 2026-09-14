package com.barter.service.impl;

import com.barter.dto.ExchangeApplyDTO;
import com.barter.dto.ExchangeHandleDTO;
import com.barter.entity.ExchangeApply;
import com.barter.entity.IdleItem;
import com.barter.mapper.ExchangeApplyMapper;
import com.barter.mapper.IdleItemMapper;
import com.barter.service.ExchangeApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交换申请服务实现
 */
@Service
public class ExchangeApplyServiceImpl implements ExchangeApplyService {

    @Autowired
    private ExchangeApplyMapper applyMapper;

    @Autowired
    private IdleItemMapper itemMapper;

    @Override
    public void apply(Long userId, ExchangeApplyDTO dto) {
        if (dto.getItemId() == null) {
            throw new RuntimeException("请选择目标物品");
        }
        IdleItem item = itemMapper.selectById(dto.getItemId());
        if (item == null) {
            throw new RuntimeException("目标物品不存在");
        }
        if (item.getStatus() != 1) {
            throw new RuntimeException("该物品当前不可交换");
        }
        // 不能对自己的物品发起申请
        if (item.getUserId().equals(userId)) {
            throw new RuntimeException("不能对自己发布的物品发起申请");
        }

        ExchangeApply apply = new ExchangeApply();
        apply.setApplyUserId(userId);
        apply.setItemId(dto.getItemId());
        apply.setMessage(dto.getMessage());
        apply.setApplyStatus(0); // 待处理
        applyMapper.insert(apply);
    }

    @Override
    public List<ExchangeApply> myApply(Long userId) {
        return applyMapper.selectMyApply(userId);
    }

    @Override
    public List<ExchangeApply> receivedApply(Long userId) {
        return applyMapper.selectReceivedApply(userId);
    }

    @Override
    public void handle(Long userId, ExchangeHandleDTO dto) {
        if (dto.getId() == null) {
            throw new RuntimeException("申请ID不能为空");
        }
        if (dto.getApplyStatus() == null || (dto.getApplyStatus() != 1 && dto.getApplyStatus() != 2)) {
            throw new RuntimeException("处理状态无效，只能为1(同意)或2(拒绝)");
        }

        ExchangeApply apply = applyMapper.selectById(dto.getId());
        if (apply == null) {
            throw new RuntimeException("申请不存在");
        }
        if (apply.getApplyStatus() != 0) {
            throw new RuntimeException("申请已处理，不能重复操作");
        }

        // 校验是否是物品发布者
        IdleItem item = itemMapper.selectById(apply.getItemId());
        if (item == null || !item.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作，只有物品发布者能处理申请");
        }

        applyMapper.updateStatus(dto.getId(), dto.getApplyStatus());

        // 如果同意，将物品状态改为2（已交换完成）
        if (dto.getApplyStatus() == 1) {
            itemMapper.updateStatus(apply.getItemId(), 2);
        }
    }
}

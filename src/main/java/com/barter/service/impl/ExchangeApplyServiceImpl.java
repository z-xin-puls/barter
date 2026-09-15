package com.barter.service.impl;

import com.barter.common.BusinessException;
import com.barter.dto.ExchangeApplyDTO;
import com.barter.dto.ExchangeHandleDTO;
import com.barter.entity.ExchangeApply;
import com.barter.entity.IdleItem;
import com.barter.mapper.ExchangeApplyMapper;
import com.barter.mapper.IdleItemMapper;
import com.barter.service.ExchangeApplyService;
import com.barter.service.NotificationService;
import com.barter.vo.ExchangeApplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExchangeApplyServiceImpl implements ExchangeApplyService {

    @Autowired
    private ExchangeApplyMapper applyMapper;

    @Autowired
    private IdleItemMapper itemMapper;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void apply(Long userId, ExchangeApplyDTO dto) {
        if (dto.getItemId() == null) {
            throw new BusinessException("请选择目标物品");
        }
        IdleItem item = itemMapper.selectById(dto.getItemId());
        if (item == null) {
            throw new BusinessException("目标物品不存在");
        }
        if (item.getStatus() != 1) {
            throw new BusinessException("该物品当前不可交换");
        }
        if (item.getUserId().equals(userId)) {
            throw new BusinessException("不能对自己发布的物品发起申请");
        }

        ExchangeApply apply = new ExchangeApply();
        apply.setApplyUserId(userId);
        apply.setItemId(dto.getItemId());
        apply.setOwnerUserId(item.getUserId());
        apply.setMessage(dto.getMessage());
        apply.setApplyStatus(0);
        applyMapper.insert(apply);

        // 通知物品发布者
        notificationService.send(item.getUserId(), "apply", apply.getId(),
                "收到新的交换申请", "有人对你的物品「" + item.getItemName() + "」发起了交换申请");
    }

    @Override
    public List<ExchangeApplyVO> myApply(Long userId) {
        return applyMapper.selectMyApply(userId);
    }

    @Override
    public List<ExchangeApplyVO> receivedApply(Long userId) {
        return applyMapper.selectReceivedApply(userId);
    }

    @Override
    @Transactional
    public void handle(Long userId, ExchangeHandleDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("申请ID不能为空");
        }
        if (dto.getApplyStatus() == null || (dto.getApplyStatus() != 1 && dto.getApplyStatus() != 2)) {
            throw new BusinessException("处理状态无效，只能为1(同意)或2(拒绝)");
        }

        ExchangeApply apply = applyMapper.selectById(dto.getId());
        if (apply == null) {
            throw new BusinessException("申请不存在");
        }
        if (apply.getApplyStatus() != 0) {
            throw new BusinessException("申请已处理，不能重复操作");
        }
        if (!apply.getOwnerUserId().equals(userId)) {
            throw new BusinessException("无权操作，只有物品发布者能处理申请");
        }

        applyMapper.updateStatus(dto.getId(), dto.getApplyStatus());

        ExchangeApplyVO vo = applyMapper.selectVOById(dto.getId());
        String itemName = vo != null ? vo.getItemName() : "物品";

        if (dto.getApplyStatus() == 1) {
            notificationService.send(apply.getApplyUserId(), "agree", apply.getId(),
                    "交换申请已同意", "你对「" + itemName + "」的交换申请已被同意，请确认完成交换");
        } else {
            notificationService.send(apply.getApplyUserId(), "reject", apply.getId(),
                    "交换申请已拒绝", "你对「" + itemName + "」的交换申请已被拒绝");
        }
    }

    @Override
    @Transactional
    public void confirm(Long userId, Long applyId) {
        ExchangeApply apply = applyMapper.selectById(applyId);
        if (apply == null) {
            throw new BusinessException("申请不存在");
        }
        if (!apply.getApplyUserId().equals(userId)) {
            throw new BusinessException("只有申请人能确认交换完成");
        }
        if (apply.getApplyStatus() != 1) {
            throw new BusinessException("当前状态不能确认，仅已同意的申请可确认完成");
        }

        // 申请状态 → 已完成
        applyMapper.updateStatus(applyId, 3);
        // 物品状态 → 已交换完成
        itemMapper.updateStatus(apply.getItemId(), 2);

        ExchangeApplyVO vo = applyMapper.selectVOById(applyId);
        String itemName = vo != null ? vo.getItemName() : "物品";

        notificationService.send(apply.getOwnerUserId(), "complete", applyId,
                "交换已确认完成", "对方已确认「" + itemName + "」交换完成，你可以对对方进行评价");
    }
}

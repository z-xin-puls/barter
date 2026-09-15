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
        if (apply.getApplyStatus() != 1) {
            throw new BusinessException("当前状态不能确认，仅已同意的申请可确认完成");
        }

        boolean isApplier = apply.getApplyUserId().equals(userId);
        boolean isOwner = apply.getOwnerUserId().equals(userId);
        if (!isApplier && !isOwner) {
            throw new BusinessException("无权操作，只有交换双方可确认");
        }

        // 防止重复确认
        if (isApplier && apply.getApplyConfirmed() != null && apply.getApplyConfirmed() == 1) {
            throw new BusinessException("你已确认过，请等待对方确认");
        }
        if (isOwner && apply.getOwnerConfirmed() != null && apply.getOwnerConfirmed() == 1) {
            throw new BusinessException("你已确认过，请等待对方确认");
        }

        ExchangeApplyVO vo = applyMapper.selectVOById(applyId);
        String itemName = vo != null ? vo.getItemName() : "物品";

        // 标记当前方确认
        if (isApplier) {
            applyMapper.updateApplyConfirmed(applyId);
        } else {
            applyMapper.updateOwnerConfirmed(applyId);
        }

        // 重新查询最新状态，判断是否双方都已确认
        ExchangeApply latest = applyMapper.selectById(applyId);
        boolean applyConfirmed = latest.getApplyConfirmed() != null && latest.getApplyConfirmed() == 1;
        boolean ownerConfirmed = latest.getOwnerConfirmed() != null && latest.getOwnerConfirmed() == 1;

        if (applyConfirmed && ownerConfirmed) {
            // 双方都确认 → 申请状态已完成 + 物品状态已交换
            applyMapper.updateStatus(applyId, 3);
            itemMapper.updateStatus(apply.getItemId(), 2);

            // 通知双方
            notificationService.send(apply.getApplyUserId(), "complete", applyId,
                    "交换已完成", "「" + itemName + "」交换已双方确认完成，你可以对对方进行评价");
            notificationService.send(apply.getOwnerUserId(), "complete", applyId,
                    "交换已完成", "「" + itemName + "」交换已双方确认完成，你可以对对方进行评价");
        } else {
            // 单方确认 → 通知对方
            Long receiver = isApplier ? apply.getOwnerUserId() : apply.getApplyUserId();
            String who = isApplier ? "申请人" : "物品发布者";
            notificationService.send(receiver, "confirm", applyId,
                    "对方已确认完成", who + "已确认「" + itemName + "」交换完成，请你也确认完成");
        }
    }

    @Override
    @Transactional
    public void updateTrade(Long userId, Long applyId, String tradeTime, String tradeLocation) {
        ExchangeApply apply = applyMapper.selectById(applyId);
        if (apply == null) {
            throw new BusinessException("申请不存在");
        }
        // 只有申请人或物品发布者可以填写
        boolean isApplier = apply.getApplyUserId().equals(userId);
        boolean isOwner = apply.getOwnerUserId().equals(userId);
        if (!isApplier && !isOwner) {
            throw new BusinessException("无权操作，只有交换双方可填写交易信息");
        }
        if (apply.getApplyStatus() != 1) {
            throw new BusinessException("只有已同意的申请可填写交易信息");
        }
        if (apply.getTradeStatus() != null && apply.getTradeStatus() == 2) {
            throw new BusinessException("交易信息已双方确认，不能再修改");
        }
        int confirmer = isApplier ? 0 : 1;
        applyMapper.updateTradeInfo(applyId, tradeTime, tradeLocation, confirmer);

        // 通知对方：如果是反对对方方案后给出的新方案，提示对方确认或再次反对
        Long receiver = isApplier ? apply.getOwnerUserId() : apply.getApplyUserId();
        ExchangeApplyVO vo = applyMapper.selectVOById(applyId);
        String itemName = vo != null ? vo.getItemName() : "物品";
        String who = isApplier ? "申请人" : "物品发布者";
        notificationService.send(receiver, "trade", applyId,
                "交易时间/地点待确认", who + "给出了「" + itemName + "」的交易时间/地点，请确认或反对");
    }

    @Override
    @Transactional
    public void confirmTrade(Long userId, Long applyId) {
        ExchangeApply apply = applyMapper.selectById(applyId);
        if (apply == null) {
            throw new BusinessException("申请不存在");
        }
        boolean isApplier = apply.getApplyUserId().equals(userId);
        boolean isOwner = apply.getOwnerUserId().equals(userId);
        if (!isApplier && !isOwner) {
            throw new BusinessException("无权操作，只有交换双方可确认");
        }
        if (apply.getApplyStatus() != 1) {
            throw new BusinessException("只有已同意的申请可确认交易信息");
        }
        if (apply.getTradeStatus() == null || apply.getTradeStatus() != 1) {
            throw new BusinessException("当前没有待确认的交易信息");
        }
        // 填写方本人不能确认自己的方案
        Integer filler = apply.getTradeConfirmer();
        if ((isApplier && filler != null && filler == 0) || (isOwner && filler != null && filler == 1)) {
            throw new BusinessException("不能确认自己填写的交易信息，请等待对方确认");
        }

        applyMapper.updateTradeStatus(applyId, 2);

        ExchangeApplyVO vo = applyMapper.selectVOById(applyId);
        String itemName = vo != null ? vo.getItemName() : "物品";
        Long receiver = isApplier ? apply.getOwnerUserId() : apply.getApplyUserId();
        notificationService.send(receiver, "trade", applyId,
                "交易信息已确认", "对方已确认「" + itemName + "」的交易时间/地点，请按时交易");
    }
}

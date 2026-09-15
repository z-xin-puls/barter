package com.barter.service;

import com.barter.dto.ItemStatusDTO;
import com.barter.dto.ResetPasswordDTO;
import com.barter.entity.Report;
import com.barter.entity.SysUser;
import com.barter.vo.ExchangeApplyVO;
import com.barter.vo.IdleItemVO;

import java.util.List;
import java.util.Map;

/**
 * 管理员服务接口
 */
public interface AdminService {

    // 用户管理
    List<SysUser> userList();
    void userDelete(Long id);
    void resetPassword(ResetPasswordDTO dto);
    void updateUserStatus(Long id, Integer status);

    // 物品管理
    List<IdleItemVO> itemList();
    void updateItemStatus(ItemStatusDTO dto);
    void itemDelete(Long id);
    /** 审核物品：status 1通过 2驳回 */
    void auditItem(Long id, Integer status, String remark);

    // 申请管理
    List<ExchangeApplyVO> applyList();

    // 举报管理
    List<Report> reportList(Integer status);
    void handleReport(Long id, Integer status);

    // 统计
    Map<String, Object> stats();
}

package com.barter.service.impl;

import com.barter.common.BusinessException;
import com.barter.dto.ItemStatusDTO;
import com.barter.dto.ResetPasswordDTO;
import com.barter.entity.Report;
import com.barter.entity.SysUser;
import com.barter.mapper.ExchangeApplyMapper;
import com.barter.mapper.IdleItemMapper;
import com.barter.mapper.ReportMapper;
import com.barter.mapper.SysUserMapper;
import com.barter.service.AdminService;
import com.barter.vo.ExchangeApplyVO;
import com.barter.vo.IdleItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private IdleItemMapper itemMapper;
    @Autowired
    private ExchangeApplyMapper applyMapper;
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ============ 用户管理 ============

    @Override
    public List<SysUser> userList() {
        return userMapper.selectAll();
    }

    @Override
    public void userDelete(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) throw new BusinessException("用户不存在");
        userMapper.deleteById(id);
    }

    @Override
    public void resetPassword(ResetPasswordDTO dto) {
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new BusinessException("新密码不能为空");
        }
        SysUser user = userMapper.selectById(dto.getId());
        if (user == null) throw new BusinessException("用户不存在");
        userMapper.updatePassword(dto.getId(), passwordEncoder.encode(dto.getPassword()));
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        SysUser user = userMapper.selectById(id);
        if (user == null) throw new BusinessException("用户不存在");
        userMapper.updateStatus(id, status);
    }

    // ============ 物品管理 ============

    @Override
    public List<IdleItemVO> itemList() {
        return itemMapper.selectAll();
    }

    @Override
    public void updateItemStatus(ItemStatusDTO dto) {
        if (dto.getStatus() != 1 && dto.getStatus() != 3) {
            throw new BusinessException("status 只能是 1(上架) 或 3(下架)");
        }
        itemMapper.updateStatus(dto.getId(), dto.getStatus());
    }

    @Override
    public void itemDelete(Long id) {
        itemMapper.deleteById(id);
    }

    @Override
    public void auditItem(Long id, Integer status, String remark) {
        if (status != 1 && status != 2) {
            throw new BusinessException("审核状态只能是 1(通过) 或 2(驳回)");
        }
        int rows = itemMapper.updateAuditStatus(id, status, remark);
        if (rows == 0) throw new BusinessException("物品不存在");
    }

    // ============ 申请管理 ============

    @Override
    public List<ExchangeApplyVO> applyList() {
        return applyMapper.selectAll();
    }

    // ============ 举报管理 ============

    @Override
    public List<Report> reportList(Integer status) {
        if (status != null) {
            return reportMapper.selectByStatus(status);
        }
        return reportMapper.selectAll();
    }

    @Override
    public void handleReport(Long id, Integer status) {
        reportMapper.updateStatus(id, status);
    }

    // ============ 统计（COUNT 查询，不加载全表） ============

    @Override
    public Map<String, Object> stats() {
        Map<String, Object> map = new HashMap<>();
        map.put("userCount", userMapper.countAll());
        map.put("itemCount", itemMapper.countByStatus(1));
        map.put("totalItemCount", itemMapper.countAll());
        map.put("applyCount", applyMapper.countAll());
        map.put("pendingAuditCount", itemMapper.countByAuditStatus(0));
        return map;
    }
}

package com.barter.controller;

import com.barter.common.BusinessException;
import com.barter.common.JwtUtil;
import com.barter.common.Result;
import com.barter.dto.ItemStatusDTO;
import com.barter.dto.LoginDTO;
import com.barter.dto.ResetPasswordDTO;
import com.barter.entity.AdminUser;
import com.barter.entity.Report;
import com.barter.entity.SysUser;
import com.barter.mapper.AdminUserMapper;
import com.barter.service.AdminService;
import com.barter.vo.ExchangeApplyVO;
import com.barter.vo.IdleItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员控制器 —— /api/admin/**
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 管理员登录（独立接口，查询 admin_user 表）
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO dto) {
        if (dto.getUsername() == null || dto.getPassword() == null) {
            throw new BusinessException("账号或密码不能为空");
        }
        AdminUser admin = adminUserMapper.selectByUsername(dto.getUsername());
        if (admin == null) {
            throw new BusinessException("管理员账号不存在");
        }
        if (admin.getStatus() != null && admin.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        if (!passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new BusinessException("密码错误");
        }
        String token = jwtUtil.generateToken(admin.getId(), admin.getUsername(), "admin");

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", admin.getId());
        result.put("username", admin.getUsername());
        result.put("realName", admin.getRealName());
        result.put("avatar", admin.getAvatar());
        result.put("userType", "admin");
        return Result.success("登录成功", result);
    }

    // ============ 用户管理 ============

    @GetMapping("/user/list")
    public Result<List<SysUser>> userList() {
        return Result.success(adminService.userList());
    }

    @DeleteMapping("/user/delete/{id}")
    public Result<?> userDelete(@PathVariable Long id) {
        adminService.userDelete(id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/user/reset-pwd")
    public Result<?> resetPassword(@RequestBody ResetPasswordDTO dto) {
        adminService.resetPassword(dto);
        return Result.success("密码重置成功", null);
    }

    @PutMapping("/user/status/{id}/{status}")
    public Result<?> updateUserStatus(@PathVariable Long id, @PathVariable Integer status) {
        adminService.updateUserStatus(id, status);
        return Result.success(status == 0 ? "已封禁" : "已解封", null);
    }

    // ============ 物品管理 ============

    @GetMapping("/item/list")
    public Result<List<IdleItemVO>> itemList() {
        return Result.success(adminService.itemList());
    }

    @PutMapping("/item/status")
    public Result<?> itemStatus(@RequestBody ItemStatusDTO dto) {
        adminService.updateItemStatus(dto);
        return Result.success("操作成功", null);
    }

    /**
     * 审核物品：status 1通过 2驳回
     */
    @PutMapping("/item/audit/{id}/{status}")
    public Result<?> auditItem(@PathVariable Long id,
                               @PathVariable Integer status,
                               @RequestParam(required = false) String remark) {
        adminService.auditItem(id, status, remark);
        return Result.success(status == 1 ? "审核通过" : "已驳回", null);
    }

    @DeleteMapping("/item/delete/{id}")
    public Result<?> itemDelete(@PathVariable Long id) {
        adminService.itemDelete(id);
        return Result.success("删除成功", null);
    }

    // ============ 申请管理 ============

    @GetMapping("/apply/list")
    public Result<List<ExchangeApplyVO>> applyList() {
        return Result.success(adminService.applyList());
    }

    // ============ 举报管理 ============

    @GetMapping("/report/list")
    public Result<List<Report>> reportList(@RequestParam(required = false) Integer status) {
        return Result.success(adminService.reportList(status));
    }

    @PutMapping("/report/handle/{id}/{status}")
    public Result<?> handleReport(@PathVariable Long id, @PathVariable Integer status) {
        adminService.handleReport(id, status);
        return Result.success("处理成功", null);
    }

    // ============ 数据统计 ============

    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        return Result.success(adminService.stats());
    }
}

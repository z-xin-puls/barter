package com.barter.controller;

import com.barter.common.Result;
import com.barter.entity.SysUser;
import com.barter.mapper.ExchangeApplyMapper;
import com.barter.mapper.IdleItemMapper;
import com.barter.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员控制器 —— /api/admin/**  （AdminAuthInterceptor 保证只有 role=1 能访问）
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private IdleItemMapper itemMapper;

    @Autowired
    private ExchangeApplyMapper applyMapper;

    // ============ 用户管理 ============

    /** 查询全部用户 */
    @GetMapping("/user/list")
    public Result<List<SysUser>> userList() {
        List<SysUser> list = userMapper.selectAll();
        // 不返回密码
        list.forEach(u -> u.setPassword(null));
        return Result.success(list);
    }

    /** 删除用户 */
    @DeleteMapping("/user/delete/{id}")
    public Result<?> userDelete(@PathVariable Long id) {
        userMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    /** 重置用户密码 */
    @PutMapping("/user/reset-pwd")
    public Result<?> resetPassword(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        String newPassword = (String) body.get("password");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.error("新密码不能为空");
        }
        SysUser u = userMapper.selectById(id);
        if (u == null) return Result.error("用户不存在");
        userMapper.updatePassword(id, newPassword);
        return Result.success("密码重置成功", null);
    }

    // ============ 物品管理 ============

    /** 查询全部物品（含下架） */
    @GetMapping("/item/list")
    public Result<?> itemList() {
        List list = itemMapper.selectAll();
        return Result.success(list);
    }

    /** 下架/上架物品 */
    @PutMapping("/item/status")
    public Result<?> itemStatus(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        Integer status = Integer.valueOf(body.get("status").toString());
        // status: 1上架 3下架
        if (status != 1 && status != 3) {
            return Result.error("status 只能是 1(上架) 或 3(下架)");
        }
        itemMapper.updateStatus(id, status);
        return Result.success("操作成功", null);
    }

    /** 删除物品 */
    @DeleteMapping("/item/delete/{id}")
    public Result<?> itemDelete(@PathVariable Long id) {
        itemMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    // ============ 申请管理 ============

    /** 查询全部交换申请 */
    @GetMapping("/apply/list")
    public Result<?> applyList() {
        List list = applyMapper.selectAll();
        return Result.success(list);
    }

    // ============ 数据统计 ============

    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        Map<String, Object> map = new HashMap<>();
        map.put("userCount", userMapper.selectAll().size());
        map.put("itemCount", itemMapper.selectCount(null));
        map.put("totalItemCount", itemMapper.selectAll().size());
        map.put("applyCount", applyMapper.selectAll().size());
        return Result.success(map);
    }
}

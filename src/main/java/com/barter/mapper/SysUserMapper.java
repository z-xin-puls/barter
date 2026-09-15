package com.barter.mapper;

import com.barter.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper
 */
@Mapper
public interface SysUserMapper {

    /** 登录用：含密码 */
    SysUser selectByUsername(String username);

    SysUser selectById(Long id);

    int insert(SysUser user);

    /** 管理后台：查询全部用户（不含密码） */
    List<SysUser> selectAll();

    /** 统计用户总数 */
    int countAll();

    int deleteById(Long id);

    /** 重置密码（传入已加密的密码） */
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /** 更新用户状态（封禁/解封） */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /** 更新用户信息 */
    int updateById(SysUser user);
}

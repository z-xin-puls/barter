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

    SysUser selectByUsername(String username);

    SysUser selectById(Long id);

    int insert(SysUser user);

    /** 管理员：查询全部用户 */
    List<SysUser> selectAll();

    /** 管理员：删除用户 */
    int deleteById(Long id);

    /** 管理员：重置密码 */
    int updatePassword(@Param("id") Long id, @Param("password") String password);
}

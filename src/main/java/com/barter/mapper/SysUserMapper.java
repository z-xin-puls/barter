package com.barter.mapper;

import com.barter.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 */
@Mapper
public interface SysUserMapper {

    SysUser selectByUsername(String username);

    SysUser selectById(Long id);

    int insert(SysUser user);
}

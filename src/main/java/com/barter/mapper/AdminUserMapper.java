package com.barter.mapper;

import com.barter.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理员Mapper
 */
@Mapper
public interface AdminUserMapper {

    /** 登录用：含密码 */
    AdminUser selectByUsername(String username);

    AdminUser selectById(Long id);

    int insert(AdminUser admin);

    /** 管理后台：查询全部管理员（不含密码） */
    List<AdminUser> selectAll();

    int countAll();

    int deleteById(Long id);

    /** 重置密码（传入已加密的密码） */
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /** 更新管理员状态（启用/禁用） */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}

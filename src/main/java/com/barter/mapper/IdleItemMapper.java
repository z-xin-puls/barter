package com.barter.mapper;

import com.barter.entity.IdleItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 闲置物品Mapper
 */
@Mapper
public interface IdleItemMapper {

    int insert(IdleItem item);

    IdleItem selectById(Long id);

    /**
     * 分页查询
     */
    List<IdleItem> selectPage(@Param("categoryId") Long categoryId,
                              @Param("offset") Integer offset,
                              @Param("pageSize") Integer pageSize);

    /**
     * 查询总数
     */
    int selectCount(@Param("categoryId") Long categoryId);

    /**
     * 下架物品
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 查询我的发布
     */
    List<IdleItem> selectByUserId(@Param("userId") Long userId);

    /** 管理员：查询全部物品（含下架） */
    List<IdleItem> selectAll();

    /** 管理员：删除物品 */
    int deleteById(Long id);
}

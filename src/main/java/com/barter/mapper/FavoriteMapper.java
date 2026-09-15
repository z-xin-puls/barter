package com.barter.mapper;

import com.barter.entity.Favorite;
import com.barter.vo.IdleItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper {

    int insert(Favorite favorite);

    int deleteByUserAndItem(@Param("userId") Long userId, @Param("itemId") Long itemId);

    Favorite selectByUserAndItem(@Param("userId") Long userId, @Param("itemId") Long itemId);

    /** 我的收藏列表（含物品信息） */
    List<IdleItemVO> selectMyFavorites(@Param("userId") Long userId);

    int countByItemId(@Param("itemId") Long itemId);
}

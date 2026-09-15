package com.barter.service.impl;

import com.barter.common.BusinessException;
import com.barter.entity.Favorite;
import com.barter.mapper.FavoriteMapper;
import com.barter.service.FavoriteService;
import com.barter.vo.IdleItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public void add(Long userId, Long itemId) {
        if (favoriteMapper.selectByUserAndItem(userId, itemId) != null) {
            throw new BusinessException("已收藏该物品");
        }
        Favorite fav = new Favorite();
        fav.setUserId(userId);
        fav.setItemId(itemId);
        favoriteMapper.insert(fav);
    }

    @Override
    public void remove(Long userId, Long itemId) {
        favoriteMapper.deleteByUserAndItem(userId, itemId);
    }

    @Override
    public List<IdleItemVO> myFavorites(Long userId) {
        return favoriteMapper.selectMyFavorites(userId);
    }
}

package com.barter.service;

import com.barter.vo.IdleItemVO;

import java.util.List;

public interface FavoriteService {
    void add(Long userId, Long itemId);
    void remove(Long userId, Long itemId);
    List<IdleItemVO> myFavorites(Long userId);
}

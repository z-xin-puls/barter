package com.barter.controller;

import com.barter.common.Result;
import com.barter.service.FavoriteService;
import com.barter.vo.IdleItemVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/add/{itemId}")
    public Result<?> add(@PathVariable Long itemId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.add(userId, itemId);
        return Result.success("收藏成功", null);
    }

    @DeleteMapping("/remove/{itemId}")
    public Result<?> remove(@PathVariable Long itemId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.remove(userId, itemId);
        return Result.success("已取消收藏", null);
    }

    @GetMapping("/my")
    public Result<List<IdleItemVO>> myFavorites(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(favoriteService.myFavorites(userId));
    }
}

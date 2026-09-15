package com.barter.controller;

import com.barter.common.Result;
import com.barter.dto.ReviewDTO;
import com.barter.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public Result<?> add(@RequestBody ReviewDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        reviewService.create(userId, dto);
        return Result.success("评价成功", null);
    }

    @GetMapping("/user/{userId}")
    public Result<List<Map<String, Object>>> listByUser(@PathVariable Long userId) {
        return Result.success(reviewService.listByUser(userId));
    }

    @GetMapping("/rating/{userId}")
    public Result<Map<String, Object>> rating(@PathVariable Long userId) {
        return Result.success(reviewService.userRating(userId));
    }
}

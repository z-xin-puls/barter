package com.barter.service;

import com.barter.dto.ReviewDTO;

import java.util.List;
import java.util.Map;

public interface ReviewService {
    void create(Long userId, ReviewDTO dto);
    List<Map<String, Object>> listByUser(Long toUserId);
    Map<String, Object> userRating(Long toUserId);
}

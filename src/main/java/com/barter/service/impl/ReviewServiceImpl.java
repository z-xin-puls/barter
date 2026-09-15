package com.barter.service.impl;

import com.barter.common.BusinessException;
import com.barter.dto.ReviewDTO;
import com.barter.entity.Review;
import com.barter.mapper.ReviewMapper;
import com.barter.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public void create(Long userId, ReviewDTO dto) {
        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new BusinessException("评分必须在1-5之间");
        }
        if (dto.getToUserId() == null || dto.getToUserId().equals(userId)) {
            throw new BusinessException("不能评价自己");
        }
        Review review = new Review();
        review.setApplyId(dto.getApplyId());
        review.setItemId(dto.getItemId());
        review.setFromUserId(userId);
        review.setToUserId(dto.getToUserId());
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        reviewMapper.insert(review);
    }

    @Override
    public List<Map<String, Object>> listByUser(Long toUserId) {
        return reviewMapper.selectByToUserId(toUserId);
    }

    @Override
    public Map<String, Object> userRating(Long toUserId) {
        Map<String, Object> map = new HashMap<>();
        map.put("avgRating", reviewMapper.selectAvgRating(toUserId));
        return map;
    }
}

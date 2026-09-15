package com.barter.dto;

import lombok.Data;

/**
 * 评价请求
 */
@Data
public class ReviewDTO {
    private Long applyId;
    private Long itemId;
    private Long toUserId;
    private Integer rating;
    private String content;
}

package com.barter.dto;

import lombok.Data;

/**
 * 发布闲置物品请求
 */
@Data
public class ItemAddDTO {
    private Long categoryId;
    private String itemName;
    private String itemDesc;
    private String hopeExchange;
    /** 图片（JSON数组字符串） */
    private String images;
    /** 成色 */
    private String itemCondition;
    /** 校区/交易地点 */
    private String campus;
}

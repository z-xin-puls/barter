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
}

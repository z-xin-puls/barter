package com.barter.entity;

import lombok.Data;

/**
 * 物品分类实体
 */
@Data
public class ItemCategory {
    private Long id;
    private String categoryName;
    private Integer sort;
}

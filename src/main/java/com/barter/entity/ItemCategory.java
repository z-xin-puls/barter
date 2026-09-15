package com.barter.entity;

import lombok.Data;

/**
 * 物品分类实体
 */
@Data
public class ItemCategory {
    private Long id;
    private String categoryName;
    /** 图标（emoji 或图标URL） */
    private String icon;
    /** 父分类ID，null为一级分类 */
    private Long parentId;
    private Integer sort;
}

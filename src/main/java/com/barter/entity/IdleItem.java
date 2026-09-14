package com.barter.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 闲置物品实体
 */
@Data
public class IdleItem {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String itemName;
    private String itemDesc;
    private String hopeExchange;
    /** 1正常上架 2已交换完成 3下架 */
    private Integer status;
    private LocalDateTime createTime;

    // 关联字段
    private String username;
    private String categoryName;
}

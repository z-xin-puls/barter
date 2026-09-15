package com.barter.dto;

import lombok.Data;

/**
 * 物品上下架请求
 */
@Data
public class ItemStatusDTO {
    private Long id;
    /** 1上架 3下架 */
    private Integer status;
}

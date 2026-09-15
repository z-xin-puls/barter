package com.barter.dto;

import lombok.Data;

/**
 * 物品搜索/分页查询请求
 */
@Data
public class ItemQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Long categoryId;
    /** 关键词（匹配物品名称/描述） */
    private String keyword;
    /** 成色筛选 */
    private String itemCondition;
    /** 校区筛选 */
    private String campus;
}

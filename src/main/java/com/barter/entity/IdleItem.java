package com.barter.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 闲置物品实体（纯表映射，关联字段见 VO）
 */
@Data
public class IdleItem {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String itemName;
    private String itemDesc;
    private String hopeExchange;
    /** 物品图片（JSON数组字符串，最多5张） */
    private String images;
    /** 成色：全新/轻微使用/明显使用 */
    private String itemCondition;
    /** 校区/交易地点 */
    private String campus;
    /** 浏览量 */
    private Integer viewCount;
    /** 1正常上架 2已交换完成 3下架 */
    private Integer status;
    /** 审核状态：0待审核 1已通过 2已驳回 */
    private Integer auditStatus;
    /** 审核备注 */
    private String auditRemark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

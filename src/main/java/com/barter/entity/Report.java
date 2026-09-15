package com.barter.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 举报实体
 */
@Data
public class Report {
    private Long id;
    /** 举报人ID */
    private Long reporterId;
    /** 举报目标类型：item/user */
    private String targetType;
    /** 被举报目标ID */
    private Long targetId;
    /** 举报原因 */
    private String reason;
    /** 举报描述 */
    private String description;
    /** 0待处理 1已处理 2已驳回 */
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime handleTime;
}

package com.barter.dto;

import lombok.Data;

/**
 * 举报请求
 */
@Data
public class ReportDTO {
    /** item 或 user */
    private String targetType;
    private Long targetId;
    private String reason;
    private String description;
}

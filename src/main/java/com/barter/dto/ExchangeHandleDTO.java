package com.barter.dto;

import lombok.Data;

/**
 * 处理申请请求
 */
@Data
public class ExchangeHandleDTO {
    private Long id;
    /** 1同意 2拒绝 */
    private Integer applyStatus;
}

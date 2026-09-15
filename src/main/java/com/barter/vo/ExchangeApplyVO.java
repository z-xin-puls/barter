package com.barter.vo;

import com.barter.entity.ExchangeApply;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交换申请视图对象（含双方用户与物品信息）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExchangeApplyVO extends ExchangeApply {
    /** 申请人用户名 */
    private String applyUsername;
    /** 申请人头像 */
    private String applyUserAvatar;
    /** 申请人学号 */
    private String applyStudentNo;
    /** 物品发布者用户名 */
    private String ownerUsername;
    /** 物品名称 */
    private String itemName;
    /** 物品图片 */
    private String itemImages;
}

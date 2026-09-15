package com.barter.vo;

import com.barter.entity.IdleItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 闲置物品视图对象（含关联信息）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IdleItemVO extends IdleItem {
    /** 发布者用户名 */
    private String username;
    /** 发布者头像 */
    private String userAvatar;
    /** 发布者学号 */
    private String studentNo;
    /** 发布者院系 */
    private String department;
    /** 分类名称 */
    private String categoryName;
    /** 当前用户是否已收藏 */
    private Boolean favorited;
}

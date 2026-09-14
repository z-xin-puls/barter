package com.barter.mapper;

import com.barter.entity.ItemCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 物品分类Mapper
 */
@Mapper
public interface ItemCategoryMapper {

    List<ItemCategory> selectAll();
}

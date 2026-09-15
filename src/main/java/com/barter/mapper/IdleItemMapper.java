package com.barter.mapper;

import com.barter.entity.IdleItem;
import com.barter.vo.IdleItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 闲置物品Mapper
 */
@Mapper
public interface IdleItemMapper {

    int insert(IdleItem item);

    /** 详情（含关联信息） */
    IdleItemVO selectVOById(@Param("id") Long id, @Param("currentUserId") Long currentUserId);

    /** 基础查询（无JOIN，内部校验用） */
    IdleItem selectById(Long id);

    /** 分页查询（含搜索/筛选） */
    List<IdleItemVO> selectPage(@Param("categoryId") Long categoryId,
                                @Param("keyword") String keyword,
                                @Param("itemCondition") String itemCondition,
                                @Param("campus") String campus,
                                @Param("offset") Integer offset,
                                @Param("pageSize") Integer pageSize,
                                @Param("currentUserId") Long currentUserId);

    int selectCount(@Param("categoryId") Long categoryId,
                    @Param("keyword") String keyword,
                    @Param("itemCondition") String itemCondition,
                    @Param("campus") String campus);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /** 更新审核状态：status 1通过 2驳回 */
    int updateAuditStatus(@Param("id") Long id, @Param("auditStatus") Integer auditStatus,
                          @Param("auditRemark") String auditRemark);

    /** 统计上架物品数 */
    int countByStatus(@Param("status") Integer status);

    /** 按审核状态统计 */
    int countByAuditStatus(@Param("auditStatus") Integer auditStatus);

    /** 浏览量+1 */
    int incrementViewCount(Long id);

    List<IdleItemVO> selectByUserId(@Param("userId") Long userId);

    /** 管理后台：全部物品 */
    List<IdleItemVO> selectAll();

    /** 统计物品总数 */
    int countAll();

    int deleteById(Long id);
}

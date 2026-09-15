package com.barter.mapper;

import com.barter.entity.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper {

    int insert(Report report);

    List<Report> selectAll();

    List<Report> selectByStatus(@Param("status") Integer status);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}

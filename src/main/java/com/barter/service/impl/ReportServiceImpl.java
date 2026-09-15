package com.barter.service.impl;

import com.barter.common.BusinessException;
import com.barter.dto.ReportDTO;
import com.barter.entity.Report;
import com.barter.mapper.ReportMapper;
import com.barter.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public void create(Long userId, ReportDTO dto) {
        if (dto.getTargetType() == null || dto.getTargetId() == null) {
            throw new BusinessException("举报目标不完整");
        }
        if (dto.getReason() == null || dto.getReason().trim().isEmpty()) {
            throw new BusinessException("请填写举报原因");
        }
        Report report = new Report();
        report.setReporterId(userId);
        report.setTargetType(dto.getTargetType());
        report.setTargetId(dto.getTargetId());
        report.setReason(dto.getReason());
        report.setDescription(dto.getDescription());
        report.setStatus(0);
        reportMapper.insert(report);
    }

    @Override
    public List<Report> list(Integer status) {
        if (status != null) {
            return reportMapper.selectByStatus(status);
        }
        return reportMapper.selectAll();
    }

    @Override
    public void handle(Long id, Integer status) {
        if (status != 1 && status != 2) {
            throw new BusinessException("处理状态无效");
        }
        reportMapper.updateStatus(id, status);
    }
}

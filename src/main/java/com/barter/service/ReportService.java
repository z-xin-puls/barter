package com.barter.service;

import com.barter.dto.ReportDTO;
import com.barter.entity.Report;

import java.util.List;

public interface ReportService {
    void create(Long userId, ReportDTO dto);
    List<Report> list(Integer status);
    void handle(Long id, Integer status);
}

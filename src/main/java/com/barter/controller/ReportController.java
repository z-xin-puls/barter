package com.barter.controller;

import com.barter.common.Result;
import com.barter.dto.ReportDTO;
import com.barter.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/add")
    public Result<?> add(@RequestBody ReportDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        reportService.create(userId, dto);
        return Result.success("举报已提交，我们会尽快处理", null);
    }
}

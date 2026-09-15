package com.barter.controller;

import com.barter.common.BusinessException;
import com.barter.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/upload")
public class FileController {

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    /**
     * 上传单张图片，返回可访问的 URL（如 /uploads/20240101/xxx.jpg）
     */
    @PostMapping("/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file,
                                      HttpServletRequest request) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("请选择要上传的文件");
        }
        // 校验类型：仅允许图片
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException("仅支持上传图片文件");
        }
        // 校验大小：5MB
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BusinessException("图片大小不能超过 5MB");
        }
        // 校验扩展名白名单
        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.')).toLowerCase();
        }
        if (!".jpg".equals(ext) && !".jpeg".equals(ext) && !".png".equals(ext)
                && !".gif".equals(ext) && !".webp".equals(ext)) {
            throw new BusinessException("仅支持 jpg/jpeg/png/gif/webp 格式");
        }

        // 按日期分桶
        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 转为绝对路径，避免 Spring 将相对路径解析到临时目录
        File baseDir = new File(uploadPath).getAbsoluteFile();
        File dir = new File(baseDir, dateDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new BusinessException("创建上传目录失败");
        }

        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;
        File dest = new File(dir, fileName);
        try {
            // 使用绝对路径写入文件
            file.transferTo(dest.getAbsoluteFile());
        } catch (IOException e) {
            throw new BusinessException("文件保存失败: " + e.getMessage());
        }

        // 返回可访问的相对 URL
        String url = "/uploads/" + dateDir + "/" + fileName;
        return Result.success("上传成功", url);
    }
}

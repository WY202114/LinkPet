package com.wzc.linkpet.app.controller;

import com.wzc.linkpet.common.result.Result;
import com.wzc.linkpet.common.util.MinioUtil;
import io.minio.MinioClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 用户端 - 文件上传接口
 * 将文件上传至 MinIO 对象存储，返回文件访问路径（对象名称）。
 * 前端拿到对象名称后，在保存帖子/宠物信息时传给对应接口字段。
 */
@Tag(name = "文件上传", description = "上传图片到 MinIO 对象存储")
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @return 图片对象名称（可拼接 MinIO 域名访问）
     */
    @Operation(summary = "上传图片")
    @PostMapping("/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 生成唯一对象名称，防止文件覆盖
            String originalFilename = file.getOriginalFilename();
            String suffix = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String objectName = "images/" + UUID.randomUUID() + suffix;
            String result = MinioUtil.upload(
                    minioClient,
                    bucketName,
                    objectName,
                    file.getInputStream(),
                    file.getSize(),
                    file.getContentType()
            );
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(5001, "文件上传失败：" + e.getMessage());
        }
    }
}

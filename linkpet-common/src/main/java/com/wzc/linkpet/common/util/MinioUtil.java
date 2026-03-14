package com.wzc.linkpet.common.util;

import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * MinIO 对象存储工具类
 * 封装常用的文件上传、删除、获取预签名URL等操作。
 *
 * <p>说明：MinioClient 是线程安全的，应作为 Spring Bean 注入，
 * 本类的方法均接受 MinioClient 参数，避免与 Spring 容器强耦合，
 * 方便单元测试时注入 Mock 对象。</p>
 */
@Slf4j
public class MinioUtil {

    private MinioUtil() {
        // 工具类，禁止实例化
    }

    /**
     * 上传文件到 MinIO
     *
     * @param minioClient MinIO 客户端实例
     * @param bucketName  存储桶名称
     * @param objectName  对象名称（含路径，如 "pet/avatar/xxx.jpg"）
     * @param inputStream 文件输入流
     * @param size        文件大小
     * @param contentType 文件 MIME 类型（如 "image/jpeg"）
     * @return 文件访问路径（对象名称）
     * @throws RuntimeException 上传失败时抛出
     */
    public static String upload(MinioClient minioClient, String bucketName,
                                String objectName, InputStream inputStream, long size, String contentType) {
        try {
            // 检查存储桶是否存在，不存在则创建
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("MinIO 存储桶 [{}] 不存在，已自动创建", bucketName);
            }
            // 执行上传
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );
            log.info("文件上传成功：bucket={}, object={}", bucketName, objectName);
            return objectName;
        } catch (Exception e) {
            log.error("文件上传失败：bucket={}, object={}", bucketName, objectName, e);
            throw new RuntimeException("文件上传到 MinIO 失败", e);
        }
    }

    /**
     * 删除 MinIO 中的文件
     *
     * @param minioClient MinIO 客户端实例
     * @param bucketName  存储桶名称
     * @param objectName  对象名称
     */
    public static void delete(MinioClient minioClient, String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            log.info("文件删除成功：bucket={}, object={}", bucketName, objectName);
        } catch (Exception e) {
            log.error("文件删除失败：bucket={}, object={}", bucketName, objectName, e);
            throw new RuntimeException("从 MinIO 删除文件失败", e);
        }
    }

    /**
     * 获取文件的预签名访问 URL（有时效性）
     *
     * @param minioClient MinIO 客户端实例
     * @param bucketName  存储桶名称
     * @param objectName  对象名称
     * @param expiryHours URL 有效时长（小时）
     * @return 预签名 URL
     */
    public static String getPresignedUrl(MinioClient minioClient, String bucketName,
                                         String objectName, int expiryHours) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expiryHours, TimeUnit.HOURS)
                            .build()
            );
        } catch (Exception e) {
            log.error("获取预签名URL失败：bucket={}, object={}", bucketName, objectName, e);
            throw new RuntimeException("获取 MinIO 预签名URL失败", e);
        }
    }
}

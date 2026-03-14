package com.wzc.linkpet.common.exception;

import lombok.Getter;

/**
 * 业务错误码枚举
 * 每个枚举值关联一个 HTTP 状态码（code）和可读的描述消息（message）。
 * 全局统一管理，便于前后端联调和错误追踪。
 */
@Getter
public enum ErrorCode {

    // ==================== 通用错误 ====================
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    // ==================== 用户模块 ====================
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户名已被注册"),
    PASSWORD_ERROR(1003, "密码错误"),
    USER_DISABLED(1004, "账号已被禁用"),

    // ==================== 宠物模块 ====================
    PET_NOT_FOUND(2001, "宠物不存在"),
    PET_TYPE_NOT_FOUND(2002, "宠物类型不存在"),
    PET_NOT_AVAILABLE(2003, "该宠物当前不可领养"),
    PET_TYPE_REVIEW_NOT_PENDING(2004, "该品种审核已处理，无法重复操作"),

    // ==================== 领养模块 ====================
    ADOPTION_NOT_FOUND(3001, "领养申请不存在"),
    ADOPTION_ALREADY_APPLIED(3002, "您已申请领养该宠物，请勿重复提交"),
    ADOPTION_STATUS_ERROR(3003, "领养申请状态异常，无法操作"),

    // ==================== 帖子/评论模块 ====================
    POST_NOT_FOUND(4001, "帖子不存在"),
    COMMENT_NOT_FOUND(4002, "评论不存在"),

    // ==================== 文件上传模块 ====================
    FILE_UPLOAD_FAILED(5001, "文件上传失败"),
    FILE_TYPE_NOT_SUPPORTED(5002, "不支持的文件类型");

    /** 业务错误码 */
    private final int code;

    /** 错误描述 */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

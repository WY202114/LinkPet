package com.wzc.linkpet.common.result;

import lombok.Getter;

/**
 * 通用结果码枚举
 * 提供常见的 HTTP 语义状态码，供 {@link Result} 快速构建响应。
 * 业务特定错误码见 ErrorCode 枚举。
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数有误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

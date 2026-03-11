package com.wzc.linkpet.common.exception;

import lombok.Getter;

/**
 * 业务异常
 * 所有业务逻辑中的预期错误均通过抛出此异常来中断流程，
 * 由 {@link GlobalExceptionHandler} 统一捕获并转换为规范的响应体。
 *
 * <p>使用方式：
 * <pre>
 *   throw new BusinessException(ErrorCode.USER_NOT_FOUND);
 * </pre>
 * </p>
 */
@Getter
public class BusinessException extends RuntimeException {

    /** 错误码（包含 HTTP 状态码语义与业务描述） */
    private final ErrorCode errorCode;

    /**
     * 使用标准错误码构造业务异常
     *
     * @param errorCode 错误码枚举
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * 使用标准错误码 + 自定义消息构造业务异常（用于动态错误描述）
     *
     * @param errorCode 错误码枚举
     * @param message   自定义消息，覆盖错误码默认消息
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

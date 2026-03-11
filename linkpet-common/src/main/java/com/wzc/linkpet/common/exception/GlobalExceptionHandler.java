package com.wzc.linkpet.common.exception;

import com.wzc.linkpet.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 使用 {@code @RestControllerAdvice} 拦截所有 Controller 层抛出的异常，
 * 统一转换为 {@link Result} 格式返回，避免在每个 Controller 中重复处理异常。
 *
 * <p>处理的异常类型：
 * <ul>
 *   <li>{@link BusinessException} — 业务异常，直接取错误码信息返回</li>
 *   <li>{@link MethodArgumentNotValidException} — 参数校验失败，提取字段错误信息返回</li>
 *   <li>{@link Exception} — 兜底处理，防止未知异常泄漏堆栈信息给前端</li>
 * </ul>
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     * 业务异常是预期内的错误，日志级别用 warn 即可，不需要打印堆栈
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常：code={}, message={}", e.getErrorCode().getCode(), e.getMessage());
        return Result.error(e.getErrorCode().getCode(), e.getMessage());
    }

    /**
     * 处理参数校验失败异常（@Valid / @Validated 触发）
     * 将所有字段错误信息拼接成一条可读的消息返回
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败：{}", message);
        return Result.error(ErrorCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * 兜底异常处理
     * 对于未预期的系统级错误，记录完整堆栈，返回通用错误信息（不暴露内部细节）
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.error(ErrorCode.INTERNAL_ERROR.getCode(), ErrorCode.INTERNAL_ERROR.getMessage());
    }
}

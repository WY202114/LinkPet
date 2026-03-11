package com.wzc.linkpet.common.result;

import lombok.Data;

/**
 * 统一响应结果封装
 * 所有接口均返回此类型，保证前后端契约一致。
 *
 * <p>字段说明：
 * <ul>
 *   <li>code  — 业务状态码，200 表示成功，其余见 {@link ResultCode} 或 ErrorCode</li>
 *   <li>message — 提示信息</li>
 *   <li>data  — 响应数据，失败时为 null</li>
 * </ul>
 * </p>
 *
 * <p>链式调用示例：
 * <pre>
 *   return Result.success(userVO);
 *   return Result.error(ErrorCode.USER_NOT_FOUND);
 * </pre>
 * </p>
 *
 * @param <T> 响应数据类型
 */
@Data
public class Result<T> {

    private int code;
    private String message;
    private T data;

    // ==================== 成功 ====================

    /**
     * 成功，无数据
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功，携带数据
     *
     * @param data 响应数据
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    // ==================== 失败 ====================

    /**
     * 失败，使用结果码枚举
     *
     * @param resultCode 结果码
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        return result;
    }

    /**
     * 失败，指定 code 和 message（供 GlobalExceptionHandler 使用）
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}

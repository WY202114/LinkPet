package com.wzc.linkpet.common.context;

/**
 * 基础上下文持有器（ThreadLocal 存储当前登录用户信息）
 *
 * <p>工作原理：
 * 每个 HTTP 请求由一个独立线程处理，ThreadLocal 为每个线程提供独立的变量副本。
 * JWT 过滤器在请求进入时解析令牌并调用 {@link #setCurrentId} 存储用户 ID，
 * 后续同一请求的 Service / Controller 层可通过 {@link #getCurrentId} 获取，
 * 无需层层传参。请求结束时须调用 {@link #removeCurrentId} 清理，防止线程池复用导致数据泄漏。
 * </p>
 *
 * <p>存储字段：当前登录用户的主键 ID（Long 类型）</p>
 */
public class BaseContext {

    /**
     * ThreadLocal 存储当前线程（即当前请求）的用户 ID
     */
    private static final ThreadLocal<Long> CURRENT_ID = new ThreadLocal<>();

    private BaseContext() {
        // 工具类，禁止实例化
    }

    /**
     * 设置当前登录用户 ID
     * 通常在 JWT 认证过滤器中调用
     *
     * @param id 用户主键 ID
     */
    public static void setCurrentId(Long id) {
        CURRENT_ID.set(id);
    }

    /**
     * 获取当前登录用户 ID
     *
     * @return 用户主键 ID，未登录时返回 null
     */
    public static Long getCurrentId() {
        return CURRENT_ID.get();
    }

    /**
     * 清除当前线程存储的用户 ID
     * 必须在请求处理完毕后（如过滤器 finally 块中）调用，防止内存泄漏
     */
    public static void removeCurrentId() {
        CURRENT_ID.remove();
    }
}

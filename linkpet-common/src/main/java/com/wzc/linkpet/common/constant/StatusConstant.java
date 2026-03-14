package com.wzc.linkpet.common.constant;

/**
 * 状态常量
 * 统一管理系统中各业务实体的状态值，避免硬编码魔法数字。
 */
public class StatusConstant {

    private StatusConstant() {
        // 工具类，禁止实例化
    }

    // ==================== 通用状态 ====================

    /** 启用 / 正常 */
    public static final Integer ENABLE = 1;

    /** 禁用 / 封禁 */
    public static final Integer DISABLE = 0;

    // ==================== 宠物状态 ====================

    /** 宠物：待领养 */
    public static final Integer PET_AVAILABLE = 1;

    /** 宠物：已领养 */
    public static final Integer PET_ADOPTED = 2;

    /** 宠物：已下架 */
    public static final Integer PET_OFFLINE = 0;

    // ==================== 领养申请状态 ====================

    /** 领养申请：待审核 */
    public static final Integer ADOPTION_PENDING = 0;

    /** 领养申请：已通过 */
    public static final Integer ADOPTION_APPROVED = 1;

    /** 领养申请：已拒绝 */
    public static final Integer ADOPTION_REJECTED = 2;

    // ==================== 自定义品种审核状态 ====================

    /** 品种审核：待审核 */
    public static final Integer TYPE_REVIEW_PENDING = 0;

    /** 品种审核：已通过 */
    public static final Integer TYPE_REVIEW_APPROVED = 1;

    /** 品种审核：已拒绝 */
    public static final Integer TYPE_REVIEW_REJECTED = 2;

    // ==================== 帖子状态 ====================

    /** 帖子：正常可见 */
    public static final Integer POST_VISIBLE = 1;

    /** 帖子：已隐藏 */
    public static final Integer POST_HIDDEN = 0;
}

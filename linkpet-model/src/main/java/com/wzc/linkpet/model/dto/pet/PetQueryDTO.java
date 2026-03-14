package com.wzc.linkpet.model.dto.pet;

import lombok.Data;

/**
 * 宠物列表查询 DTO（分页 + 筛选条件）
 */
@Data
public class PetQueryDTO {

    /** 宠物类型 ID（可选） */
    private Long typeId;

    /** 性别：0-未知，1-雄，2-雌（可选） */
    private Integer gender;

    /** 状态（可选，管理端使用） */
    private Integer status;

    /** 搜索关键词（宠物名称模糊匹配） */
    private String keyword;

    /** 发布用户 ID（用于"我的宠物"查询） */
    private Long userId;

    /** 当前页，从 1 开始，默认 1 */
    private int page = 1;

    /** 每页条数，默认 10 */
    private int pageSize = 10;
}

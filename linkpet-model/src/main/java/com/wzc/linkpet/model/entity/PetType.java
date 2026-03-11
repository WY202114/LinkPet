package com.wzc.linkpet.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 宠物类型实体类
 * 对应数据库表：pet_type
 * 例如：猫、狗、兔子、鸟类等
 */
@Data
@TableName("pet_type")
public class PetType {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 类型名称，例如"猫"、"狗" */
    private String name;

    /** 类型图标（MinIO 对象名称） */
    private String icon;

    /** 类型描述 */
    private String description;

    /** 排序权重（越大越靠前） */
    private Integer sort;

    /** 状态：1-启用，0-禁用 */
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

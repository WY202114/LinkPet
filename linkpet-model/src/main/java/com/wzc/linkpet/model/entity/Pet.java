package com.wzc.linkpet.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 宠物实体类
 * 对应数据库表：pet
 */
@Data
@TableName(value = "pet", autoResultMap = true)
public class Pet {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 宠物名称 */
    private String name;

    /** 宠物类型 ID（关联 pet_type 表） */
    private Long typeId;

    /** 年龄（月） */
    private Integer ageMonth;

    /** 性别：0-未知，1-雄，2-雌 */
    private Integer gender;

    /** 描述 */
    private String description;

    /** 宠物图片（JSON 数组存储） */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images;

    /** 所在地址 */
    private String address;

    /** 发现地点（适用于流浪动物） */
    private String location;

    /** 发布用户 ID */
    private Long userId;

    /**
     * 状态：
     * 0-已下架，1-待领养，2-已领养
     * 见 {@link com.wzc.linkpet.common.constant.StatusConstant}
     */
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

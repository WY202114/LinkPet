package com.wzc.linkpet.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 社区帖子实体类
 * 对应数据库表：post
 */
@Data
@TableName(value = "post", autoResultMap = true)
public class Post {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 发布者用户 ID */
    private Long userId;

    /** 帖子标题 */
    private String title;

    /** 帖子正文内容 */
    private String content;

    /** 图片列表（JSON 数组存储） */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images;

    /** 话题标签（逗号分隔） */
    private String tags;

    /** 点赞数 */
    private Integer likeCount;

    /** 评论数 */
    private Integer commentCount;

    /**
     * 状态：1-正常，0-隐藏
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

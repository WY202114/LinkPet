package com.wzc.linkpet.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论实体类
 * 对应数据库表：comment
 * 支持一级评论和二级回复（通过 parentId 区分）
 */
@Data
@TableName("comment")
public class Comment {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 所属帖子 ID */
    private Long postId;

    /** 评论者用户 ID */
    private Long userId;

    /**
     * 父评论 ID，为 0 表示一级评论，非 0 表示回复某条评论
     */
    private Long parentId;

    /** 被回复用户 ID（二级回复时有效） */
    private Long replyUserId;

    /** 评论内容 */
    private String content;

    /** 点赞数 */
    private Integer likeCount;

    /** 状态：1-正常，0-隐藏 */
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

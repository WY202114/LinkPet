package com.wzc.linkpet.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户行为实体类
 * 对应数据库表：user_action
 * 记录用户对帖子或评论的点赞、收藏等互动行为
 */
@Data
@TableName("user_action")
public class UserAction {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 操作用户 ID */
    private Long userId;

    /**
     * 目标类型：1-帖子，2-评论
     */
    private Integer targetType;

    /** 目标 ID（帖子 ID 或评论 ID） */
    private Long targetId;

    /**
     * 行为类型：1-点赞，2-收藏
     */
    private Integer actionType;

    /** 创建时间（即发生行为的时间） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}

package com.wzc.linkpet.model.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发布评论请求 DTO
 */
@Data
public class CommentDTO {

    /** 所属帖子 ID */
    @NotNull(message = "帖子ID不能为空")
    private Long postId;

    /** 评论内容 */
    @NotBlank(message = "评论内容不能为空")
    private String content;

    /**
     * 父评论 ID（回复时传入，一级评论传 0 或不传）
     */
    private Long parentId = 0L;

    /** 被回复用户 ID（二级回复时传入） */
    private Long replyUserId;
}

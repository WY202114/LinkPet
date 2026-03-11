package com.wzc.linkpet.model.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论 VO
 */
@Data
public class CommentVO {

    private Long id;
    private Long postId;
    private Long userId;
    private String userNickname;
    private String userAvatar;
    private Long parentId;
    private Long replyUserId;
    private String replyUserNickname;
    private String content;
    private Integer likeCount;
    private LocalDateTime createTime;

    /** 当前用户是否已点赞 */
    private Boolean liked;

    /** 子评论列表（二级回复，懒加载） */
    private List<CommentVO> children;
}

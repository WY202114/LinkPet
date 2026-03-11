package com.wzc.linkpet.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帖子 VO
 */
@Data
public class PostVO {

    private Long id;
    private Long userId;
    private String userNickname;
    private String userAvatar;
    private String title;
    private String content;
    private String images;
    private String tags;
    private Integer likeCount;
    private Integer commentCount;
    private Integer status;
    private LocalDateTime createTime;

    /** 当前用户是否已点赞（需结合用户行为表查询） */
    private Boolean liked;

    /** 当前用户是否已收藏 */
    private Boolean collected;
}

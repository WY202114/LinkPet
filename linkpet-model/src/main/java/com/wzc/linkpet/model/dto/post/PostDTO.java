package com.wzc.linkpet.model.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 发布/编辑帖子请求 DTO
 */
@Data
public class PostDTO {

    /** 帖子标题 */
    @NotBlank(message = "帖子标题不能为空")
    private String title;

    /** 帖子正文 */
    @NotBlank(message = "帖子内容不能为空")
    private String content;

    /** 图片列表（可为空） */
    private List<String> images;

    /** 话题标签（逗号分隔，可为空） */
    private String tags;
}

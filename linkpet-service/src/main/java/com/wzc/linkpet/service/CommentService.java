package com.wzc.linkpet.service;

import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.model.dto.post.CommentDTO;
import com.wzc.linkpet.model.vo.CommentVO;

/**
 * 评论服务接口
 */
public interface CommentService {

    /** 分页查询某帖子的评论列表 */
    PageResult<CommentVO> pageQuery(Long postId, int page, int pageSize);

    /** 发表评论/回复 */
    void add(CommentDTO commentDTO);

    /** 删除评论 */
    void delete(Long id);

    /** 点赞/取消点赞评论 */
    void toggleLike(Long commentId);
}

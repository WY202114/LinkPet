package com.wzc.linkpet.service;

import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.model.dto.post.PostDTO;
import com.wzc.linkpet.model.vo.PostVO;

/**
 * 帖子服务接口
 */
public interface PostService {

    /** 分页查询帖子列表 */
    PageResult<PostVO> pageQuery(String keyword, int page, int pageSize);

    /** 查询帖子详情 */
    PostVO getById(Long id);

    /** 发布帖子 */
    void add(PostDTO postDTO);

    /** 修改帖子 */
    void update(Long id, PostDTO postDTO);

    /** 删除帖子 */
    void delete(Long id);

    /** 点赞/取消点赞 */
    void toggleLike(Long postId);
}

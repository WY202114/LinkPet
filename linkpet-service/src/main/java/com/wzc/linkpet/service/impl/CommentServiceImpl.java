package com.wzc.linkpet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.linkpet.common.context.BaseContext;
import com.wzc.linkpet.common.exception.BusinessException;
import com.wzc.linkpet.common.exception.ErrorCode;
import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.mapper.CommentMapper;
import com.wzc.linkpet.mapper.PostMapper;
import com.wzc.linkpet.mapper.UserActionMapper;
import com.wzc.linkpet.model.dto.post.CommentDTO;
import com.wzc.linkpet.model.entity.Comment;
import com.wzc.linkpet.model.entity.Post;
import com.wzc.linkpet.model.entity.UserAction;
import com.wzc.linkpet.model.vo.CommentVO;
import com.wzc.linkpet.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final PostMapper postMapper;
    private final UserActionMapper userActionMapper;

    @Override
    public PageResult<CommentVO> pageQuery(Long postId, int page, int pageSize) {
        // 只查一级评论
        Page<Comment> pageResult = commentMapper.selectPage(
                new Page<>(page, pageSize),
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getPostId, postId)
                        .eq(Comment::getParentId, 0L)
                        .orderByAsc(Comment::getCreateTime)
        );
        List<CommentVO> vos = pageResult.getRecords().stream().map(c -> {
            CommentVO vo = new CommentVO();
            BeanUtils.copyProperties(c, vo);
            return vo;
        }).collect(Collectors.toList());
        return new PageResult<>(vos, pageResult.getTotal());
    }

    @Override
    @Transactional
    public void add(CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setUserId(BaseContext.getCurrentId());
        comment.setLikeCount(0);
        comment.setStatus(1);
        commentMapper.insert(comment);
        // 更新帖子评论数
        Post post = postMapper.selectById(commentDTO.getPostId());
        if (post != null) {
            post.setCommentCount(post.getCommentCount() + 1);
            postMapper.updateById(post);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }
        commentMapper.deleteById(id);
        // 更新帖子评论数
        Post post = postMapper.selectById(comment.getPostId());
        if (post != null) {
            post.setCommentCount(Math.max(0, post.getCommentCount() - 1));
            postMapper.updateById(post);
        }
    }

    @Override
    @Transactional
    public void toggleLike(Long commentId) {
        Long userId = BaseContext.getCurrentId();
        // 2-评论, 1-点赞
        UserAction existing = userActionMapper.selectOne(
                new LambdaQueryWrapper<UserAction>()
                        .eq(UserAction::getUserId, userId)
                        .eq(UserAction::getTargetType, 2)
                        .eq(UserAction::getTargetId, commentId)
                        .eq(UserAction::getActionType, 1)
        );
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) return;
        if (existing != null) {
            userActionMapper.deleteById(existing.getId());
            comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
        } else {
            UserAction action = new UserAction();
            action.setUserId(userId);
            action.setTargetType(2);
            action.setTargetId(commentId);
            action.setActionType(1);
            userActionMapper.insert(action);
            comment.setLikeCount(comment.getLikeCount() + 1);
        }
        commentMapper.updateById(comment);
    }
}

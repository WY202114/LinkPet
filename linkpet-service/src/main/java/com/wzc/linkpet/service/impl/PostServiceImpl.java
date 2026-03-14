package com.wzc.linkpet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.linkpet.common.context.BaseContext;
import com.wzc.linkpet.common.exception.BusinessException;
import com.wzc.linkpet.common.exception.ErrorCode;
import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.mapper.PostMapper;
import com.wzc.linkpet.mapper.UserActionMapper;
import com.wzc.linkpet.mapper.UserMapper;
import com.wzc.linkpet.model.dto.post.PostDTO;
import com.wzc.linkpet.model.entity.Post;
import com.wzc.linkpet.model.entity.User;
import com.wzc.linkpet.model.entity.UserAction;
import com.wzc.linkpet.model.vo.PostVO;
import com.wzc.linkpet.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子服务实现类
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final UserActionMapper userActionMapper;
    private final UserMapper userMapper;

    @Override
    public PageResult<PostVO> pageQuery(String keyword, int page, int pageSize) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Post::getTitle, keyword).or().like(Post::getContent, keyword);
        }
        wrapper.orderByDesc(Post::getCreateTime);
        Page<Post> pageResult = postMapper.selectPage(new Page<>(page, pageSize), wrapper);
        List<PostVO> vos = pageResult.getRecords().stream().map(p -> {
            PostVO vo = new PostVO();
            BeanUtils.copyProperties(p, vo);
            User user = userMapper.selectById(p.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserAvatar(user.getAvatar());
            }
            return vo;
        }).collect(Collectors.toList());
        return new PageResult<>(vos, pageResult.getTotal());
    }

    @Override
    public PostVO getById(Long id) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }
        PostVO vo = new PostVO();
        BeanUtils.copyProperties(post, vo);
        User user = userMapper.selectById(post.getUserId());
        if (user != null) {
            vo.setUserNickname(user.getNickname());
            vo.setUserAvatar(user.getAvatar());
        }
        return vo;
    }

    @Override
    public void add(PostDTO postDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        post.setUserId(BaseContext.getCurrentId());
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setStatus(1);
        postMapper.insert(post);
    }

    @Override
    public void update(Long id, PostDTO postDTO) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }
        BeanUtils.copyProperties(postDTO, post);
        post.setId(id);
        postMapper.updateById(post);
    }

    @Override
    public void delete(Long id) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }
        // 只有管理员或帖子作者可以删除
        Long currentUserId = BaseContext.getCurrentId();
        boolean isAdmin = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        }
        if (!isAdmin && !post.getUserId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        postMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void toggleLike(Long postId) {
        Long userId = BaseContext.getCurrentId();
        // 1-帖子, 1-点赞
        UserAction existing = userActionMapper.selectOne(
                new LambdaQueryWrapper<UserAction>()
                        .eq(UserAction::getUserId, userId)
                        .eq(UserAction::getTargetType, 1)
                        .eq(UserAction::getTargetId, postId)
                        .eq(UserAction::getActionType, 1)
        );
        Post post = postMapper.selectById(postId);
        if (post == null) return;
        if (existing != null) {
            // 已点赞 → 取消点赞
            userActionMapper.deleteById(existing.getId());
            post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
        } else {
            // 未点赞 → 点赞
            UserAction action = new UserAction();
            action.setUserId(userId);
            action.setTargetType(1);
            action.setTargetId(postId);
            action.setActionType(1);
            userActionMapper.insert(action);
            post.setLikeCount(post.getLikeCount() + 1);
        }
        postMapper.updateById(post);
    }

    @Override
    public PageResult<PostVO> myPosts(int page, int pageSize) {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getUserId, userId).orderByDesc(Post::getCreateTime);
        Page<Post> pageResult = postMapper.selectPage(new Page<>(page, pageSize), wrapper);
        List<PostVO> vos = pageResult.getRecords().stream().map(p -> {
            PostVO vo = new PostVO();
            BeanUtils.copyProperties(p, vo);
            User user = userMapper.selectById(p.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserAvatar(user.getAvatar());
            }
            return vo;
        }).collect(Collectors.toList());
        return new PageResult<>(vos, pageResult.getTotal());
    }
}

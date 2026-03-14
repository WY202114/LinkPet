package com.wzc.linkpet.app.controller;

import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.common.result.Result;
import com.wzc.linkpet.model.dto.post.PostDTO;
import com.wzc.linkpet.model.vo.PostVO;
import com.wzc.linkpet.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端 - 帖子接口
 */
@Tag(name = "帖子（社区）", description = "帖子发布、浏览与点赞")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "分页查询帖子列表")
    @GetMapping
    public Result<PageResult<PostVO>> page(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(postService.pageQuery(keyword, page, pageSize));
    }

    @Operation(summary = "查询帖子详情")
    @GetMapping("/{id}")
    public Result<PostVO> getById(@PathVariable Long id) {
        return Result.success(postService.getById(id));
    }

    @Operation(summary = "发布帖子")
    @PostMapping
    public Result<Void> add(@RequestBody @Valid PostDTO dto) {
        postService.add(dto);
        return Result.success();
    }

    @Operation(summary = "修改帖子")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Valid PostDTO dto) {
        postService.update(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除帖子")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return Result.success();
    }

    @Operation(summary = "点赞/取消点赞帖子")
    @PostMapping("/{id}/like")
    public Result<Void> toggleLike(@PathVariable Long id) {
        postService.toggleLike(id);
        return Result.success();
    }

    @Operation(summary = "我发布的帖子列表（需登录）")
    @GetMapping("/my")
    public Result<PageResult<PostVO>> myPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(postService.myPosts(page, pageSize));
    }
}

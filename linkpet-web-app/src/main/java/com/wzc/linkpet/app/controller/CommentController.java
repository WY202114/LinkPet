package com.wzc.linkpet.app.controller;

import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.common.result.Result;
import com.wzc.linkpet.model.dto.post.CommentDTO;
import com.wzc.linkpet.model.vo.CommentVO;
import com.wzc.linkpet.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端 - 评论接口
 */
@Tag(name = "评论", description = "帖子评论发布与互动")
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "查询帖子评论列表（分页）")
    @GetMapping
    public Result<PageResult<CommentVO>> page(
            @RequestParam Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(commentService.pageQuery(postId, page, pageSize));
    }

    @Operation(summary = "发表评论/回复")
    @PostMapping
    public Result<Void> add(@RequestBody @Valid CommentDTO dto) {
        commentService.add(dto);
        return Result.success();
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return Result.success();
    }

    @Operation(summary = "点赞/取消点赞评论")
    @PostMapping("/{id}/like")
    public Result<Void> toggleLike(@PathVariable Long id) {
        commentService.toggleLike(id);
        return Result.success();
    }
}

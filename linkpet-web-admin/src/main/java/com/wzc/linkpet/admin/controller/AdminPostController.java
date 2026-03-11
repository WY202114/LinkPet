package com.wzc.linkpet.admin.controller;

import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.common.result.Result;
import com.wzc.linkpet.model.vo.PostVO;
import com.wzc.linkpet.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端 - 帖子管理接口
 */
@Tag(name = "帖子管理（Admin）", description = "管理员查看和删除社区帖子")
@RestController
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final PostService postService;

    @Operation(summary = "分页查询帖子列表")
    @GetMapping
    public Result<PageResult<PostVO>> page(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(postService.pageQuery(keyword, page, pageSize));
    }

    @Operation(summary = "查看帖子详情")
    @GetMapping("/{id}")
    public Result<PostVO> getById(@PathVariable Long id) {
        return Result.success(postService.getById(id));
    }

    @Operation(summary = "删除帖子（违规处理）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return Result.success();
    }
}

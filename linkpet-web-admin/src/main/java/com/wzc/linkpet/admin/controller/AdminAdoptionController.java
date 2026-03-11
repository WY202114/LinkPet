package com.wzc.linkpet.admin.controller;

import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.common.result.Result;
import com.wzc.linkpet.model.dto.adoption.AdoptionReviewDTO;
import com.wzc.linkpet.model.vo.AdoptionVO;
import com.wzc.linkpet.service.AdoptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端 - 领养申请管理接口
 */
@Tag(name = "领养审核（Admin）", description = "管理员查看并审核领养申请")
@RestController
@RequestMapping("/admin/adoptions")
@RequiredArgsConstructor
public class AdminAdoptionController {

    private final AdoptionService adoptionService;

    @Operation(summary = "分页查询领养申请列表")
    @GetMapping
    public Result<PageResult<AdoptionVO>> page(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(adoptionService.pageQuery(status, page, pageSize));
    }

    @Operation(summary = "审核领养申请（通过/拒绝）")
    @PostMapping("/review")
    public Result<Void> review(@RequestBody @Valid AdoptionReviewDTO dto) {
        adoptionService.review(dto);
        return Result.success();
    }
}

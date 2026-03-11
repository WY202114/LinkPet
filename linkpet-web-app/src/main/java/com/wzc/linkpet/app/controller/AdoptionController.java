package com.wzc.linkpet.app.controller;

import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.common.result.Result;
import com.wzc.linkpet.model.dto.adoption.AdoptionApplyDTO;
import com.wzc.linkpet.model.vo.AdoptionVO;
import com.wzc.linkpet.service.AdoptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端 - 领养申请接口
 */
@Tag(name = "领养申请", description = "用户提交和查看领养申请")
@RestController
@RequestMapping("/adoptions")
@RequiredArgsConstructor
public class AdoptionController {

    private final AdoptionService adoptionService;

    @Operation(summary = "提交领养申请")
    @PostMapping
    public Result<Void> apply(@RequestBody @Valid AdoptionApplyDTO dto) {
        adoptionService.apply(dto);
        return Result.success();
    }

    @Operation(summary = "我的领养申请列表")
    @GetMapping("/my")
    public Result<PageResult<AdoptionVO>> myList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(adoptionService.myApplyList(page, pageSize));
    }
}

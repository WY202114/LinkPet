package com.wzc.linkpet.app.controller;

import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.common.result.Result;
import com.wzc.linkpet.model.dto.pet.PetDTO;
import com.wzc.linkpet.model.dto.pet.PetQueryDTO;
import com.wzc.linkpet.model.vo.PetVO;
import com.wzc.linkpet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端 - 宠物接口
 */
@Tag(name = "宠物", description = "宠物浏览与发布")
@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @Operation(summary = "分页查询宠物列表（访客可访问）")
    @GetMapping
    public Result<PageResult<PetVO>> page(PetQueryDTO query) {
        return Result.success(petService.pageQuery(query));
    }

    @Operation(summary = "查询宠物详情")
    @GetMapping("/{id}")
    public Result<PetVO> getById(@PathVariable Long id) {
        return Result.success(petService.getById(id));
    }

    @Operation(summary = "发布宠物（需登录）")
    @PostMapping
    public Result<Void> add(@RequestBody @Valid PetDTO dto) {
        petService.add(dto);
        return Result.success();
    }

    @Operation(summary = "修改宠物信息（需登录）")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Valid PetDTO dto) {
        petService.update(id, dto);
        return Result.success();
    }

    @Operation(summary = "下架宠物（需登录）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        petService.delete(id);
        return Result.success();
    }

    @Operation(summary = "我发布的宠物列表（需登录）")
    @GetMapping("/my")
    public Result<PageResult<PetVO>> myPets(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(petService.myPets(page, pageSize));
    }
}

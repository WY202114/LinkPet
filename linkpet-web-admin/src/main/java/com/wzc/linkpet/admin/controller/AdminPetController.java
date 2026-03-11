package com.wzc.linkpet.admin.controller;

import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.common.result.Result;
import com.wzc.linkpet.model.dto.pet.PetQueryDTO;
import com.wzc.linkpet.model.vo.PetVO;
import com.wzc.linkpet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端 - 宠物管理接口
 */
@Tag(name = "宠物管理（Admin）", description = "管理员对宠物信息的增删改查")
@RestController
@RequestMapping("/admin/pets")
@RequiredArgsConstructor
public class AdminPetController {

    private final PetService petService;

    @Operation(summary = "分页查询宠物列表")
    @GetMapping
    public Result<PageResult<PetVO>> page(PetQueryDTO query) {
        return Result.success(petService.pageQuery(query));
    }

    @Operation(summary = "查询宠物详情")
    @GetMapping("/{id}")
    public Result<PetVO> getById(@PathVariable Long id) {
        return Result.success(petService.getById(id));
    }

    @Operation(summary = "下架宠物")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        petService.delete(id);
        return Result.success();
    }
}

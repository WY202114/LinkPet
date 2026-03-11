package com.wzc.linkpet.admin.controller;

import com.wzc.linkpet.common.result.Result;
import com.wzc.linkpet.model.entity.PetType;
import com.wzc.linkpet.model.vo.PetTypeVO;
import com.wzc.linkpet.service.PetTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端 - 宠物类型管理接口
 */
@Tag(name = "宠物类型管理（Admin）", description = "管理员对宠物类型的增删改")
@RestController
@RequestMapping("/admin/pet-types")
@RequiredArgsConstructor
public class AdminPetTypeController {

    private final PetTypeService petTypeService;

    @Operation(summary = "查询所有宠物类型")
    @GetMapping
    public Result<List<PetTypeVO>> list() {
        return Result.success(petTypeService.listAll());
    }

    @Operation(summary = "新增宠物类型")
    @PostMapping
    public Result<Void> add(@RequestBody PetType petType) {
        petTypeService.add(petType);
        return Result.success();
    }

    @Operation(summary = "修改宠物类型")
    @PutMapping
    public Result<Void> update(@RequestBody PetType petType) {
        petTypeService.update(petType);
        return Result.success();
    }

    @Operation(summary = "删除宠物类型")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        petTypeService.delete(id);
        return Result.success();
    }
}

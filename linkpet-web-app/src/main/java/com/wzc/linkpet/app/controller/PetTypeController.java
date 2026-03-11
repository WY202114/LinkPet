package com.wzc.linkpet.app.controller;

import com.wzc.linkpet.common.result.Result;
import com.wzc.linkpet.model.vo.PetTypeVO;
import com.wzc.linkpet.service.PetTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户端 - 宠物类型接口
 */
@Tag(name = "宠物类型", description = "查询宠物类型列表")
@RestController
@RequestMapping("/pet-types")
@RequiredArgsConstructor
public class PetTypeController {

    private final PetTypeService petTypeService;

    @Operation(summary = "查询所有宠物类型（访客可访问）")
    @GetMapping
    public Result<List<PetTypeVO>> list() {
        return Result.success(petTypeService.listAll());
    }
}

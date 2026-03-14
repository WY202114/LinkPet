package com.wzc.linkpet.app.controller;

import com.wzc.linkpet.common.result.Result;
import com.wzc.linkpet.model.vo.StatsVO;
import com.wzc.linkpet.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户端 - 首页统计接口
 */
@Tag(name = "首页统计", description = "首页统计数据")
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @Operation(summary = "获取首页统计数据（访客可访问）")
    @GetMapping
    public Result<StatsVO> getHomeStats() {
        return Result.success(statsService.getHomeStats());
    }
}

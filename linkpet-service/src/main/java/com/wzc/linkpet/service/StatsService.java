package com.wzc.linkpet.service;

import com.wzc.linkpet.model.vo.StatsVO;

/**
 * 首页统计服务接口
 */
public interface StatsService {

    /** 获取首页统计数据 */
    StatsVO getHomeStats();
}

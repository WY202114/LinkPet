package com.wzc.linkpet.service;

import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.model.dto.adoption.AdoptionApplyDTO;
import com.wzc.linkpet.model.dto.adoption.AdoptionReviewDTO;
import com.wzc.linkpet.model.vo.AdoptionVO;

/**
 * 领养申请服务接口
 */
public interface AdoptionService {

    /**
     * 提交领养申请（用户端）
     */
    void apply(AdoptionApplyDTO dto);

    /**
     * 审核领养申请（管理端）
     */
    void review(AdoptionReviewDTO dto);

    /**
     * 分页查询领养申请列表（管理端）
     *
     * @param status   状态过滤（null 查全部）
     * @param page     当前页
     * @param pageSize 每页条数
     */
    PageResult<AdoptionVO> pageQuery(Integer status, int page, int pageSize);

    /**
     * 查询当前用户的申请列表（用户端）
     */
    PageResult<AdoptionVO> myApplyList(int page, int pageSize);
}

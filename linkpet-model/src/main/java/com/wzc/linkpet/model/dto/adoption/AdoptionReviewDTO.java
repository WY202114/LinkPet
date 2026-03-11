package com.wzc.linkpet.model.dto.adoption;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 领养申请审核请求 DTO（管理端使用）
 */
@Data
public class AdoptionReviewDTO {

    /** 领养申请 ID */
    @NotNull(message = "申请ID不能为空")
    private Long applyId;

    /**
     * 审核结果：1-通过，2-拒绝
     * 见 {@link com.wzc.linkpet.common.constant.StatusConstant}
     */
    @NotNull(message = "审核结果不能为空")
    private Integer status;

    /** 审核意见 */
    private String reviewComment;
}

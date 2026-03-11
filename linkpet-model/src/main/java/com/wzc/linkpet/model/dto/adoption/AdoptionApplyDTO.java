package com.wzc.linkpet.model.dto.adoption;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 提交领养申请请求 DTO
 */
@Data
public class AdoptionApplyDTO {

    /** 目标宠物 ID */
    @NotNull(message = "宠物ID不能为空")
    private Long petId;

    /** 申请理由 */
    @NotBlank(message = "申请理由不能为空")
    private String reason;

    /** 联系电话 */
    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;
}

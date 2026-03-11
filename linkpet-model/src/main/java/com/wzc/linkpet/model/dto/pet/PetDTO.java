package com.wzc.linkpet.model.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发布/编辑宠物请求 DTO
 */
@Data
public class PetDTO {

    /** 宠物名称 */
    @NotBlank(message = "宠物名称不能为空")
    private String name;

    /** 宠物类型 ID */
    @NotNull(message = "宠物类型不能为空")
    private Long typeId;

    /** 年龄（月） */
    private Integer ageMonth;

    /** 性别：0-未知，1-雄，2-雌 */
    private Integer gender;

    /** 健康状况描述 */
    private String healthDesc;

    /** 性格描述 */
    private String personalityDesc;

    /** 图片列表（逗号分隔的 MinIO 对象名称） */
    private String images;

    /** 所在地址 */
    @NotBlank(message = "地址不能为空")
    private String address;
}

package com.wzc.linkpet.model.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

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

    /** 用户自定义品种名称（选择"其他"时填写） */
    private String customTypeName;

    /** 年龄（月） */
    private Integer ageMonth;

    /** 性别：0-未知，1-雄，2-雌 */
    private Integer gender;

    /** 健康状况描述 */
    private String healthDesc;

    /** 性格描述 */
    private String personalityDesc;

    /** 图片列表（JSON 数组存储） */
    private List<String> images;

    /** 所在地址 */
    @NotBlank(message = "地址不能为空")
    private String address;

    /** 发现地点（选填，适用于流浪动物） */
    private String location;
}

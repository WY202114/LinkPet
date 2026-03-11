package com.wzc.linkpet.model.vo;

import lombok.Data;

/**
 * 宠物类型 VO
 */
@Data
public class PetTypeVO {

    private Long id;
    private String name;
    private String icon;
    private String description;
    private Integer sort;
    private Integer status;
}

package com.wzc.linkpet.model.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 宠物详情 VO
 */
@Data
public class PetVO {

    private Long id;
    private String name;
    private Long typeId;
    private String typeName;      // 关联查询宠物类型名称
    private String customTypeName;    // 用户自定义品种名称
    private Integer typeReviewStatus; // 品种审核状态
    private Integer ageMonth;
    private Integer gender;
    private String genderDesc;    // 性别文字描述
//    private String healthDesc;
//    private String personalityDesc;
    private String description;
    private List<String> images;
    private String address;
    private String location;
    private Long userId;
    private String userNickname;  // 发布者昵称
    private String userAvatar;    // 发布者头像
    private Integer status;
    private String statusDesc;    // 状态文字描述
    private LocalDateTime createTime;
}

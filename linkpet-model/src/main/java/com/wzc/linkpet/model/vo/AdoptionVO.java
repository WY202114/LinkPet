package com.wzc.linkpet.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 领养申请 VO
 */
@Data
public class AdoptionVO {

    private Long id;
    private Long userId;
    private String userNickname;
    private Long petId;
    private String petName;
    private String reason;
    private String contactPhone;
    private Integer status;
    private String statusDesc;
    private String reviewComment;
    private LocalDateTime reviewTime;
    private LocalDateTime createTime;
}

package com.wzc.linkpet.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 领养申请实体类
 * 对应数据库表：adoption_apply
 * 记录用户对某宠物发起的领养申请及审核状态
 */
@Data
@TableName("adoption_apply")
public class AdoptionApply {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 申请人用户 ID */
    private Long userId;

    /** 目标宠物 ID */
    private Long petId;

    /** 申请理由 */
    private String reason;

    /** 联系电话 */
    private String contactPhone;

    /**
     * 申请状态：
     * 0-待审核，1-已通过，2-已拒绝
     * 见 {@link com.wzc.linkpet.common.constant.StatusConstant}
     */
    private Integer status;

    /** 审核意见（管理员填写） */
    private String reviewComment;

    /** 审核人 ID */
    private Long reviewUserId;

    /** 审核时间 */
    private LocalDateTime reviewTime;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

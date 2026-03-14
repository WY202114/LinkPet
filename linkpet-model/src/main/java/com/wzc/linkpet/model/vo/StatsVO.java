package com.wzc.linkpet.model.vo;

import lombok.Data;

/**
 * 首页统计数据 VO
 * adoptionCount: 成功领养数 (adoption_apply.status=1)
 * petWaitingCount: 待领养宠物数 (pet.status=1)
 * userCount: 社区成员数 (user 总数)
 */
@Data
public class StatsVO {

    /** 成功领养数 */
    private Long adoptionCount;

    /** 待领养宠物数 */
    private Long petWaitingCount;

    /** 社区成员数 */
    private Long userCount;
}

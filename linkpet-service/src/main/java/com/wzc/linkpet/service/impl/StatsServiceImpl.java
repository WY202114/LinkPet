package com.wzc.linkpet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzc.linkpet.mapper.AdoptionApplyMapper;
import com.wzc.linkpet.mapper.PetMapper;
import com.wzc.linkpet.mapper.UserMapper;
import com.wzc.linkpet.model.entity.AdoptionApply;
import com.wzc.linkpet.model.entity.Pet;
import com.wzc.linkpet.model.vo.StatsVO;
import com.wzc.linkpet.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 首页统计服务实现
 * - adoptionCount:  adoption_apply 中 status = 1（审核通过）的记录数
 * - petWaitingCount: pet 中 status = 1（待领养）的记录数
 * - userCount:       user 表总记录数
 */
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final AdoptionApplyMapper adoptionApplyMapper;
    private final PetMapper petMapper;
    private final UserMapper userMapper;

    @Override
    public StatsVO getHomeStats() {
        StatsVO vo = new StatsVO();

        // 成功领养数：审核通过的申请
        vo.setAdoptionCount(adoptionApplyMapper.selectCount(
                new LambdaQueryWrapper<AdoptionApply>().eq(AdoptionApply::getStatus, 1)
        ));

        // 待领养宠物数
        vo.setPetWaitingCount(petMapper.selectCount(
                new LambdaQueryWrapper<Pet>().eq(Pet::getStatus, 1)
        ));

        // 社区成员数
        vo.setUserCount(userMapper.selectCount(null));

        return vo;
    }
}

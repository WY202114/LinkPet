package com.wzc.linkpet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.linkpet.common.constant.StatusConstant;
import com.wzc.linkpet.common.context.BaseContext;
import com.wzc.linkpet.common.exception.BusinessException;
import com.wzc.linkpet.common.exception.ErrorCode;
import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.mapper.AdoptionApplyMapper;
import com.wzc.linkpet.mapper.PetMapper;
import com.wzc.linkpet.model.dto.adoption.AdoptionApplyDTO;
import com.wzc.linkpet.model.dto.adoption.AdoptionReviewDTO;
import com.wzc.linkpet.model.entity.AdoptionApply;
import com.wzc.linkpet.model.entity.Pet;
import com.wzc.linkpet.model.vo.AdoptionVO;
import com.wzc.linkpet.service.AdoptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 领养申请服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdoptionServiceImpl implements AdoptionService {

    private final AdoptionApplyMapper adoptionApplyMapper;
    private final PetMapper petMapper;

    @Override
    @Transactional
    public void apply(AdoptionApplyDTO dto) {
        Long userId = BaseContext.getCurrentId();
        // 检查宠物是否存在且可领养
        Pet pet = petMapper.selectById(dto.getPetId());
        if (pet == null) {
            throw new BusinessException(ErrorCode.PET_NOT_FOUND);
        }
        if (!StatusConstant.PET_AVAILABLE.equals(pet.getStatus())) {
            throw new BusinessException(ErrorCode.PET_NOT_AVAILABLE);
        }
        // 检查是否已有待审核申请
        Long count = adoptionApplyMapper.selectCount(
                new LambdaQueryWrapper<AdoptionApply>()
                        .eq(AdoptionApply::getUserId, userId)
                        .eq(AdoptionApply::getPetId, dto.getPetId())
                        .eq(AdoptionApply::getStatus, StatusConstant.ADOPTION_PENDING)
        );
        if (count > 0) {
            throw new BusinessException(ErrorCode.ADOPTION_ALREADY_APPLIED);
        }
        // 创建申请记录
        AdoptionApply apply = new AdoptionApply();
        BeanUtils.copyProperties(dto, apply);
        apply.setUserId(userId);
        apply.setStatus(StatusConstant.ADOPTION_PENDING);
        adoptionApplyMapper.insert(apply);
    }

    @Override
    @Transactional
    public void review(AdoptionReviewDTO dto) {
        AdoptionApply apply = adoptionApplyMapper.selectById(dto.getApplyId());
        if (apply == null) {
            throw new BusinessException(ErrorCode.ADOPTION_NOT_FOUND);
        }
        if (!StatusConstant.ADOPTION_PENDING.equals(apply.getStatus())) {
            throw new BusinessException(ErrorCode.ADOPTION_STATUS_ERROR);
        }
        apply.setStatus(dto.getStatus());
        apply.setReviewComment(dto.getReviewComment());
        apply.setReviewUserId(BaseContext.getCurrentId());
        apply.setReviewTime(LocalDateTime.now());
        adoptionApplyMapper.updateById(apply);

        // 若审核通过，更新宠物状态为已领养
        if (StatusConstant.ADOPTION_APPROVED.equals(dto.getStatus())) {
            Pet pet = petMapper.selectById(apply.getPetId());
            if (pet != null) {
                pet.setStatus(StatusConstant.PET_ADOPTED);
                petMapper.updateById(pet);
            }
        }
    }

    @Override
    public PageResult<AdoptionVO> pageQuery(Integer status, int page, int pageSize) {
        LambdaQueryWrapper<AdoptionApply> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(AdoptionApply::getStatus, status);
        }
        wrapper.orderByDesc(AdoptionApply::getCreateTime);
        Page<AdoptionApply> pageResult = adoptionApplyMapper.selectPage(new Page<>(page, pageSize), wrapper);
        List<AdoptionVO> vos = pageResult.getRecords().stream().map(a -> {
            AdoptionVO vo = new AdoptionVO();
            BeanUtils.copyProperties(a, vo);
            return vo;
        }).collect(Collectors.toList());
        return new PageResult<>(vos, pageResult.getTotal());
    }

    @Override
    public PageResult<AdoptionVO> myApplyList(int page, int pageSize) {
        Long userId = BaseContext.getCurrentId();
        Page<AdoptionApply> pageResult = adoptionApplyMapper.selectPage(
                new Page<>(page, pageSize),
                new LambdaQueryWrapper<AdoptionApply>()
                        .eq(AdoptionApply::getUserId, userId)
                        .orderByDesc(AdoptionApply::getCreateTime)
        );
        List<AdoptionVO> vos = pageResult.getRecords().stream().map(a -> {
            AdoptionVO vo = new AdoptionVO();
            BeanUtils.copyProperties(a, vo);
            return vo;
        }).collect(Collectors.toList());
        return new PageResult<>(vos, pageResult.getTotal());
    }
}

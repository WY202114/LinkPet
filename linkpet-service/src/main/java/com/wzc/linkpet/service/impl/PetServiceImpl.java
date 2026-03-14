package com.wzc.linkpet.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.linkpet.common.constant.StatusConstant;
import com.wzc.linkpet.common.context.BaseContext;
import com.wzc.linkpet.common.exception.BusinessException;
import com.wzc.linkpet.common.exception.ErrorCode;
import com.wzc.linkpet.common.result.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzc.linkpet.mapper.PetMapper;
import com.wzc.linkpet.mapper.PetTypeMapper;
import com.wzc.linkpet.model.dto.pet.PetDTO;
import com.wzc.linkpet.model.dto.pet.PetQueryDTO;
import com.wzc.linkpet.model.entity.Pet;
import com.wzc.linkpet.model.entity.PetType;
import com.wzc.linkpet.model.vo.PetVO;
import com.wzc.linkpet.service.PetService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 宠物服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetMapper petMapper;
    private final PetTypeMapper petTypeMapper;

    @Override
    public PageResult<PetVO> pageQuery(PetQueryDTO query) {
        Page<PetVO> page = new Page<>(query.getPage(), query.getPageSize());
        Page<PetVO> result = (Page<PetVO>) petMapper.selectPetPage(page, query);
        return new PageResult<>(result.getRecords(), result.getTotal());
    }

    @Override
    public PetVO getById(Long id) {
        PetVO vo = petMapper.selectPetById(id);
        if (vo == null) {
            throw new BusinessException(ErrorCode.PET_NOT_FOUND);
        }
        return vo;
    }

    @Override
    public void add(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        pet.setUserId(BaseContext.getCurrentId());
        pet.setStatus(StatusConstant.PET_AVAILABLE);
        // 如果用户填写了自定义品种名称，标记为待审核
        if (petDTO.getCustomTypeName() != null && !petDTO.getCustomTypeName().isBlank()) {
            pet.setCustomTypeName(petDTO.getCustomTypeName().trim());
            pet.setTypeReviewStatus(StatusConstant.TYPE_REVIEW_PENDING);
        }
        petMapper.insert(pet);
    }

    @Override
    public void update(Long id, PetDTO petDTO) {
        Pet pet = petMapper.selectById(id);
        if (pet == null) {
            throw new BusinessException(ErrorCode.PET_NOT_FOUND);
        }
        BeanUtils.copyProperties(petDTO, pet);
        pet.setId(id);
        petMapper.updateById(pet);
    }

    @Override
    public void delete(Long id) {
        Pet pet = petMapper.selectById(id);
        if (pet == null) {
            throw new BusinessException(ErrorCode.PET_NOT_FOUND);
        }
        pet.setStatus(StatusConstant.PET_OFFLINE);
        petMapper.updateById(pet);
    }

    @Override
    public PageResult<PetVO> myPets(int page, int pageSize) {
        PetQueryDTO query = new PetQueryDTO();
        query.setPage(page);
        query.setPageSize(pageSize);
        query.setUserId(BaseContext.getCurrentId());
        Page<PetVO> pageObj = new Page<>(page, pageSize);
        Page<PetVO> result = (Page<PetVO>) petMapper.selectPetPage(pageObj, query);
        return new PageResult<>(result.getRecords(), result.getTotal());
    }

    @Override
    public PageResult<PetVO> pendingTypeReviews(int page, int pageSize) {
        PetQueryDTO query = new PetQueryDTO();
        query.setPage(page);
        query.setPageSize(pageSize);
        query.setTypeReviewStatus(StatusConstant.TYPE_REVIEW_PENDING);
        Page<PetVO> pageObj = new Page<>(page, pageSize);
        Page<PetVO> result = (Page<PetVO>) petMapper.selectPetPage(pageObj, query);
        return new PageResult<>(result.getRecords(), result.getTotal());
    }

    @Override
    @Transactional
    public void reviewCustomType(Long petId, boolean approved) {
        Pet pet = petMapper.selectById(petId);
        if (pet == null) {
            throw new BusinessException(ErrorCode.PET_NOT_FOUND);
        }
        if (!StatusConstant.TYPE_REVIEW_PENDING.equals(pet.getTypeReviewStatus())) {
            throw new BusinessException(ErrorCode.PET_TYPE_REVIEW_NOT_PENDING);
        }

        if (approved) {
            // 检查是否已存在同名品种
            PetType existing = petTypeMapper.selectOne(
                    new LambdaQueryWrapper<PetType>()
                            .eq(PetType::getName, pet.getCustomTypeName())
                            .last("LIMIT 1")
            );
            Long newTypeId;
            if (existing != null) {
                newTypeId = existing.getId();
            } else {
                // 创建新品种
                PetType newType = new PetType();
                newType.setName(pet.getCustomTypeName());
                newType.setStatus(StatusConstant.ENABLE);
                newType.setSort(0);
                petTypeMapper.insert(newType);
                newTypeId = newType.getId();
            }
            pet.setTypeId(newTypeId);
            pet.setTypeReviewStatus(StatusConstant.TYPE_REVIEW_APPROVED);
        } else {
            pet.setTypeReviewStatus(StatusConstant.TYPE_REVIEW_REJECTED);
        }
        petMapper.updateById(pet);
    }
}

package com.wzc.linkpet.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.linkpet.common.constant.StatusConstant;
import com.wzc.linkpet.common.context.BaseContext;
import com.wzc.linkpet.common.exception.BusinessException;
import com.wzc.linkpet.common.exception.ErrorCode;
import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.mapper.PetMapper;
import com.wzc.linkpet.model.dto.pet.PetDTO;
import com.wzc.linkpet.model.dto.pet.PetQueryDTO;
import com.wzc.linkpet.model.entity.Pet;
import com.wzc.linkpet.model.vo.PetVO;
import com.wzc.linkpet.service.PetService;
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

    @Override
    public PageResult<PetVO> pageQuery(PetQueryDTO query) {
        Page<PetVO> page = new Page<>(query.getPage(), query.getPageSize());
        Page<PetVO> result = (Page<PetVO>) petMapper.selectPetPage(page, query);
        return new PageResult<>(result.getRecords(), result.getTotal());
    }

    @Override
    public PetVO getById(Long id) {
        PetQueryDTO query = new PetQueryDTO();
        // TODO: 可通过 selectPetPage 实现单条查询，或单独写一个 selectPetById
        Pet pet = petMapper.selectById(id);
        if (pet == null) {
            throw new BusinessException(ErrorCode.PET_NOT_FOUND);
        }
        PetVO vo = new PetVO();
        BeanUtils.copyProperties(pet, vo);
        return vo;
    }

    @Override
    public void add(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        pet.setUserId(BaseContext.getCurrentId());
        pet.setStatus(StatusConstant.PET_AVAILABLE);
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
}

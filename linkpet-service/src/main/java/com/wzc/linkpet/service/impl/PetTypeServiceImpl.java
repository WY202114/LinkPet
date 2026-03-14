package com.wzc.linkpet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzc.linkpet.common.constant.StatusConstant;
import com.wzc.linkpet.common.exception.BusinessException;
import com.wzc.linkpet.common.exception.ErrorCode;
import com.wzc.linkpet.mapper.PetTypeMapper;
import com.wzc.linkpet.model.entity.PetType;
import com.wzc.linkpet.model.vo.PetTypeVO;
import com.wzc.linkpet.service.PetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 宠物类型服务实现类
 */
@Service
@RequiredArgsConstructor
public class PetTypeServiceImpl implements PetTypeService {

    private final PetTypeMapper petTypeMapper;

    @Override
    public List<PetTypeVO> listAll() {
        List<PetType> types = petTypeMapper.selectList(
                new LambdaQueryWrapper<PetType>()
                        .eq(PetType::getStatus, StatusConstant.ENABLE)
                        .orderByAsc(PetType::getSort)
        );
        return types.stream().map(t -> {
            PetTypeVO vo = new PetTypeVO();
            BeanUtils.copyProperties(t, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(PetType petType) {
        petTypeMapper.insert(petType);
    }

    @Override
    public void update(PetType petType) {
        if (petTypeMapper.selectById(petType.getId()) == null) {
            throw new BusinessException(ErrorCode.PET_TYPE_NOT_FOUND);
        }
        petTypeMapper.updateById(petType);
    }

    @Override
    public void delete(Long id) {
        if (petTypeMapper.selectById(id) == null) {
            throw new BusinessException(ErrorCode.PET_TYPE_NOT_FOUND);
        }
        petTypeMapper.deleteById(id);
    }
}

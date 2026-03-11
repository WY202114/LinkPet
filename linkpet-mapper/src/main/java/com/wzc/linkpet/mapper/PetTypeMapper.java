package com.wzc.linkpet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzc.linkpet.model.entity.PetType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 宠物类型 Mapper
 */
@Mapper
public interface PetTypeMapper extends BaseMapper<PetType> {
}

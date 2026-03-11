package com.wzc.linkpet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.linkpet.model.dto.pet.PetQueryDTO;
import com.wzc.linkpet.model.entity.Pet;
import com.wzc.linkpet.model.vo.PetVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 宠物 Mapper
 */
@Mapper
public interface PetMapper extends BaseMapper<Pet> {

    /**
     * 分页查询宠物列表（关联宠物类型名称和发布者昵称）
     *
     * @param page  MyBatis-Plus 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<PetVO> selectPetPage(Page<PetVO> page, @Param("query") PetQueryDTO query);
}

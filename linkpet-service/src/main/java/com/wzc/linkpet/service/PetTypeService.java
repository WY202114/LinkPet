package com.wzc.linkpet.service;

import com.wzc.linkpet.model.entity.PetType;
import com.wzc.linkpet.model.vo.PetTypeVO;

import java.util.List;

/**
 * 宠物类型服务接口
 */
public interface PetTypeService {

    /** 查询所有启用的宠物类型列表 */
    List<PetTypeVO> listAll();

    /** 新增宠物类型（管理端） */
    void add(PetType petType);

    /** 修改宠物类型（管理端） */
    void update(PetType petType);

    /** 删除宠物类型（管理端） */
    void delete(Long id);
}

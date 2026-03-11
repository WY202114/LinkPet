package com.wzc.linkpet.service;

import com.wzc.linkpet.common.result.PageResult;
import com.wzc.linkpet.model.dto.pet.PetDTO;
import com.wzc.linkpet.model.dto.pet.PetQueryDTO;
import com.wzc.linkpet.model.vo.PetVO;

/**
 * 宠物服务接口
 */
public interface PetService {

    /**
     * 分页查询宠物列表
     */
    PageResult<PetVO> pageQuery(PetQueryDTO query);

    /**
     * 查询宠物详情
     */
    PetVO getById(Long id);

    /**
     * 发布宠物信息（用户端）
     */
    void add(PetDTO petDTO);

    /**
     * 修改宠物信息
     */
    void update(Long id, PetDTO petDTO);

    /**
     * 下架/删除宠物
     */
    void delete(Long id);
}

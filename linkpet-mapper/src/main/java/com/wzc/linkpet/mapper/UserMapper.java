package com.wzc.linkpet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzc.linkpet.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 * 继承 MyBatis-Plus BaseMapper 获得通用 CRUD 方法。
 * 自定义复杂查询在 UserMapper.xml 中用 XML 方式定义。
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

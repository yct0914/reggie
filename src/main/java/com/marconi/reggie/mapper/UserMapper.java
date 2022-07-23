package com.marconi.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marconi.reggie.pojo.DO.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Marconi
 * @date 2022/7/23
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

package com.marconi.reggie;

import com.marconi.reggie.common.SmsUtils;
import com.marconi.reggie.config.properties.CommonProperties;
import com.marconi.reggie.config.properties.JwtProperties;
import com.marconi.reggie.mapper.EmployeeMapper;
import com.marconi.reggie.service.CategoryService;
import com.marconi.reggie.service.EmployeeService;
import com.marconi.reggie.common.RedisUtil;
import com.marconi.reggie.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    CategoryService categoryService;

    @Autowired
    JwtProperties properties;

    @Autowired
    CommonProperties commonProperties;

    @Autowired
    SetMealService setMealService;

    @Test
    void contextLoads() {
    }
}

package com.marconi.reggie;

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

import java.util.ArrayDeque;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

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
    void contextLoads() throws ExecutionException, InterruptedException {
        ArrayDeque<Integer> q = new ArrayDeque<>();
        new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                System.out.println(111);
                return null;
            }
        }).get();
    }
}

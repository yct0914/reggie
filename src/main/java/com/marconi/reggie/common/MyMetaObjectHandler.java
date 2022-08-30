package com.marconi.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.marconi.reggie.pojo.DO.Employee;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.SocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MybatisPlus 公共字段自动填充实现类
 * @author Marconi
 * @date 2022/7/5
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    final PasswordEncoder passwordEncoder;
    final RedisUtil redisUtil;

    public MyMetaObjectHandler(PasswordEncoder passwordEncoder, RedisUtil redisUtil) {
        this.passwordEncoder = passwordEncoder;
        this.redisUtil = redisUtil;
    }

    /**
     * 插入操作自动填充处理
     * @param metaObject 元数据
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Class clazz = metaObject.getOriginalObject().getClass();
        if (clazz.equals(Employee.class)){
            String rowPassword = new String("123456");
            String password = passwordEncoder.encode(rowPassword);
            metaObject.setValue("password", password);
        }
        Long id = BaseContext.getCurrentId();
        metaObject.setValue("createUser", id);
        metaObject.setValue("updateUser", id);
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

    }

    /**
     * 更新操作自动填充处理
     * @param metaObject 元数据
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        long id = Long.parseLong((String) redisUtil.get("id"));
        metaObject.setValue("updateUser", id);
        metaObject.setValue("updateTime", LocalDateTime.now());

    }
}

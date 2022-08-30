package com.marconi.reggie.common;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Marconi
 * @date 2022/8/30
 */
@Slf4j
public class BaseContext {
    @SuppressWarnings("AlibabaConstantFieldShouldBeUpperCase")
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
    public static void remove(){
        threadLocal.remove();
    }
}

package com.marconi.rice.common;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marconi
 * @date 2022/8/30
 */
@Slf4j
public class BaseContext {
    public static void main(String[] args) {
        int a = -2147483645;
        List<int[]> s = new ArrayList<>();
        int b = 2147483647;
        System.out.println(a-b);
        System.out.println(Integer.compare(a, b));
    }
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

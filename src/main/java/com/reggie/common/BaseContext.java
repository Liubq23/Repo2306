package com.reggie.common;

/**
 * 基于Threadlocal，用于保存和获取登录用户id
 * @author 刘秉奇
 * @version 1.0
 */
public class BaseContext {
    private static  ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}

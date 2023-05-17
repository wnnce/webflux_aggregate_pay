package com.zeroxn.pay.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @Author: lisang
 * @DateTime: 2023/4/28 09:20
 * @Description:
 */
@Slf4j
public class BaseUtils {
    /**
     * 传入类对象和 多个类属性名字 判断这些属性中有没有NULL值
     * @param obj 类对象
     * @param fieldNames 一个或多个属性名
     * @return 存在null属性返回true 不存在返回false
     */
    public static boolean checkObjectFieldIsNull(Object obj, String ...fieldNames){
        Class objClass = obj.getClass();
        for (String filedName : fieldNames){
            try{
                Field objFiled = objClass.getDeclaredField(filedName);
                objFiled.setAccessible(true);
                if (objFiled.get(obj) == null){
                    return true;
                }
            }catch (NoSuchFieldException ns){
                log.warn("{}字段不存在", filedName);
            }catch (IllegalAccessException il){
                log.warn("获取{}类中不存在的属性{}报错", objClass.getName(), filedName);
            }
        }
        return false;
    }
}

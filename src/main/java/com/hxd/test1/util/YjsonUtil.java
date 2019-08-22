package com.hxd.test1.util;

import com.google.gson.Gson;

/**
 * 说明:
 * <br>@author lhx
 * <br>@date 2018/10/11 11:04
 * <p>
 * <br>UpdateNote:
 * <br>UpdateTime:
 * <br>UpdateUser:
 */
public class YjsonUtil {

    private static Gson gson = new Gson();

    /**
     * 将对象转换成为json字符串
     * @param obj 要转换的对象
     * @return json字符串
     */
    public static String toJson(Object obj){
        return gson.toJson(obj);
    }

    /**
     * 将json字符串转换为特定泛型的对象
     * @param json json字符串
     * @param clazz 要转换的对象类型
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json,Class<T> clazz){
        return gson.fromJson(json,clazz);
    }

}

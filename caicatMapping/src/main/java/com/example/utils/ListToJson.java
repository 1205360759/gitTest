package com.example.utils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by zhang on 2018/5/13.
 */
public class ListToJson<T> {

    public String listToJson(List<T> list) {

// 变量

        StringBuffer jsonStr = new StringBuffer();

// 前提条件

        if (null == list || 0 == list.size()) {

            return null;

        }

// class对象

        Class<?> classType = list.get(0).getClass();

// 得到javabean的名字

        String javaBeanClassName = classType.getSimpleName();



        jsonStr.append("{\"").append(javaBeanClassName.toLowerCase()).append("s").append("\":[");

// 此JavaBean中所声明的所有字段

        Field[] fields = classType.getDeclaredFields();

        for (int i = 0; i < list.size(); i++) {

            jsonStr.append("{");

            for (Field field : fields) {

// 得到字段名

                String fieldName = field.getName();

                field.setAccessible(true);

// 得到指定对象上此 Field 表示的字段的值

                Object fieldValue;

                try {

                    fieldValue = field.get(list.get(i));

                    jsonStr.append("\"").append(fieldName.toLowerCase()).append("\":").append("\"").append(fieldValue).append("\"").append(",");

                } catch (IllegalArgumentException e) {

                    e.printStackTrace();

                } catch (IllegalAccessException e) {

                    e.printStackTrace();

                }

            }

            jsonStr.deleteCharAt(jsonStr.length() - 1);

            jsonStr.append("},");

        }

        jsonStr.deleteCharAt(jsonStr.length() - 1);

        jsonStr.append("]}");

        return jsonStr.toString();

    }
}

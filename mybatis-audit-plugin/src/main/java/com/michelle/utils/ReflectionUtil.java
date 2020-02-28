package com.michelle.utils;


import java.lang.reflect.Field;

/**
 * @author michelle.min
 */
public class ReflectionUtil {
    private ReflectionUtil() {
    }

    public static Object getFieldValue(String filed, Object object) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(filed);
        return getFieldValue(field, object);
    }

    public static Object getFieldValue(Field field, Object object) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(object);
    }

    public static <T> T getFieldValue(String filed, Object object, Class<T> type) throws NoSuchFieldException, IllegalAccessException {
        Object value = getFieldValue(filed, object);
        return value != null && value.getClass() == type ? (T) value : null;
    }

    public static String getFieldString(String filed, Object object) throws IllegalAccessException, NoSuchFieldException {
        Object value = getFieldValue(filed, object);
        return value != null ? value.toString() : null;
    }

    public static Long getFieldLong(String filed, Object object) throws IllegalAccessException, NoSuchFieldException {
        Object value = getFieldValue(filed, object);
        return value != null ? Long.valueOf(value.toString()) : null;
    }

    public static Integer getFieldInteger(String filed, Object object) throws IllegalAccessException, NoSuchFieldException {
        Object value = getFieldValue(filed, object);
        return value != null ? Integer.valueOf(value.toString()) : null;
    }

}

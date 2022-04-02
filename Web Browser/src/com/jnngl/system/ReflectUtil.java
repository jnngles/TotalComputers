package com.jnngl.system;

import java.lang.reflect.InvocationTargetException;

public class ReflectUtil {

    public static Object call(Object obj, String name, Class<?>[] classes, Object...args) {
        try {
            return obj.getClass().getMethod(name, classes).invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            System.err.println("Failed to invoke method. ("+e.getClass().getSimpleName()+")");
            return null;
        }
    }

}

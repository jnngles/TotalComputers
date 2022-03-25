package com.jnngl.totalcomputers.system;

import java.util.HashMap;
import java.util.Map;

@RequiresAPI(apiLevel = 2)
public class SharedStorage {

    private static final Map<Class<?>, Map<String, Object>> storage = new HashMap<>();

    public static void put(Class<?> cls, String name, Object data) {
        Map<String, Object> localStorage = storage.getOrDefault(cls, null);
        if(localStorage == null) {
            localStorage = new HashMap<>();
            localStorage.put(name, data);
            storage.put(cls, localStorage);
        } else {
            localStorage.put(name, data);
        }
    }

    public static Object get(Class<?> cls, String name) {
        Map<String, Object> localStorage = storage.getOrDefault(cls, null);
        if(localStorage == null) return null;
        return localStorage.getOrDefault(name, null);
    }

}

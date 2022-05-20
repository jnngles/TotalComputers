package com.jnngl.totalcomputers.system;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores data shared between different operating systems/applications/application instances
 */
@RequiresAPI(apiLevel = 2)
public class SharedStorage {

    private final Map<String, Map<String, Object>> storage = new HashMap<>();

    /**
     * Stores data
     * @param cls Any class
     * @param name Any name
     * @param data Data
     */
    public void put(Class<?> cls, String name, Object data) {
        Map<String, Object> localStorage = storage.getOrDefault(cls.getName(), null);
        if(localStorage == null) {
            localStorage = new HashMap<>();
            localStorage.put(name, data);
            storage.put(cls.getName(), localStorage);
        } else {
            localStorage.put(name, data);
        }
    }

    /**
     * @param cls class
     * @param name Key name
     * @return null if not found
     */
    public Object get(Class<?> cls, String name) {
        Map<String, Object> localStorage = storage.getOrDefault(cls.getName(), null);
        if(localStorage == null) return null;
        return localStorage.getOrDefault(name, null);
    }

}

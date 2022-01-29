package com.invoicer.main.data;

import com.invoicer.sql.StoreableObject;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataManager {

    private final LinkedHashMap<Class<? extends StoreableObject>, Manager> hashMap;

    public DataManager() {
        this.hashMap = new LinkedHashMap<>();
    }

    public Manager getManager(Class<? extends StoreableObject> clazz) {
        return hashMap.get(clazz);
    }

    public void addManager(Class<? extends StoreableObject> clazz, Manager manager) {
        hashMap.put(clazz, manager);
    }

    public Map<Class<? extends StoreableObject>, Manager> getHashMap() {
        return Collections.unmodifiableMap(hashMap);
    }
}

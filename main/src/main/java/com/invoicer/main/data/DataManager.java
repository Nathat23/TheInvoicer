package com.invoicer.main.data;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataManager {

    private final LinkedHashMap<Class<? extends StoredObject>, Manager> hashMap;

    public DataManager() {
        this.hashMap = new LinkedHashMap<>();
    }

    public Manager getManager(Class<? extends StoredObject> clazz) {
        return hashMap.get(clazz);
    }

    public void addManager(Class<? extends StoredObject> clazz, Manager manager) {
        hashMap.put(clazz, manager);
    }

    public Map<Class<? extends StoredObject>, Manager> getHashMap() {
        return Collections.unmodifiableMap(hashMap);
    }
}

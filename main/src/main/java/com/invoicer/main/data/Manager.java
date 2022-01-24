package com.invoicer.main.data;

import com.invoicer.sql.Attribute;
import com.invoicer.sql.Config;
import com.invoicer.sql.StoreableObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

abstract class Manager implements IManager {

    private final HashMap<Integer, StoreableObject> hashSet;
    private final Config config;

    public Manager(Config config) {
        this.hashSet = new HashMap<>();
        this.config = config;
    }

    @Override
    public StoreableObject createAndStore() {
        StoreableObject storeableObject = createObject();
        addStoreableObject(storeableObject);
        return storeableObject;
    }

    @Override
    public StoreableObject createObject() {
        return createObject(hashSet.size());
    }

    @Override
    public Collection<StoreableObject> getStoreableObjects() {
        return hashSet.values();
    }

    @Override
    public StoreableObject getStoreableObject(int id) {
        return hashSet.get(id);
    }

    @Override
    public void addStoreableObject(StoreableObject storeableObject) {
        hashSet.put(hashSet.size(), storeableObject);
    }

    @Override
    public Collection<StoreableObject> getModified() {
        Set<StoreableObject> objects = new HashSet<>();
        for (StoreableObject stored : getStoreableObjects()) {
            for (Attribute attribute : stored.getAttributes()) {
                if (!attribute.isModified()) {
                    continue;
                }
                objects.add(stored);
                break;
            }
        }
        return objects;
    }

    public Config getConfig() {
        return config;
    }
}

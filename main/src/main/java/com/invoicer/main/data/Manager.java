package com.invoicer.main.data;

import com.invoicer.sql.Config;
import com.invoicer.sql.AttributeGroup;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class Manager implements IManager {

    private final HashMap<Integer, StoredObject> hashSet;
    private final Config config;
    private final DataManager dataManager;
    private int nextId;

    public Manager(DataManager dataManager, Config config) {
        this.hashSet = new HashMap<>();
        this.config = config;
        this.dataManager = dataManager;
    }

    @Override
    public StoredObject createAndStore() {
        StoredObject attributeGroup = createObject();
        addStoredObject(attributeGroup);
        return attributeGroup;
    }
    @Override
    public StoredObject createObject() {
        return createObject(new AttributeGroup(getNextId(), getConfig()));
    }

    @Override
    public Collection<StoredObject> getStoredObjects() {
        return hashSet.values();
    }

    @Override
    public StoredObject getStoredObject(int id) {
        return hashSet.get(id);
    }

    @Override
    public void addStoredObject(StoredObject storedObject) {
        hashSet.put(storedObject.getId(), storedObject);
    }

    @Override
    public int getNextId() {
        if (nextId == 0) {
            nextId = hashSet.size();
            return nextId;
        }
        nextId += 1;
        return nextId;
    }

    @Override
    public Collection<StoredObject> getModified() {
        Set<StoredObject> objects = new HashSet<>();
        for (StoredObject stored : getStoredObjects()) {
            if (!stored.isModified()) {
                continue;
            }
            objects.add(stored);
        }
        return objects;
    }

    public Config getConfig() {
        return config;
    }

    DataManager getDataManager() {
        return dataManager;
    }
}

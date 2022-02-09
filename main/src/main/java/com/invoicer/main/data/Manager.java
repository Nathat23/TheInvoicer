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

    /**
     * Create new object and add to storage.
     * @return object created
     */
    @Override
    public StoredObject createAndStore() {
        StoredObject attributeGroup = createObject();
        addStoredObject(attributeGroup);
        return attributeGroup;
    }

    /**
     * Create new object
     * @return new object
     */
    @Override
    public StoredObject createObject() {
        return createObject(new AttributeGroup(getNextId(), getConfig()));
    }

    /**
     * Get all stored objects
     * @return all stored objects
     */
    @Override
    public Collection<StoredObject> getStoredObjects() {
        return hashSet.values();
    }

    /**
     * Get the stored object with the specified id
     * @param id the id
     * @return the stored object with the specified id
     */
    @Override
    public StoredObject getStoredObject(int id) {
        return hashSet.get(id);
    }

    /**
     * Add stored object to map
     * @param storedObject object to add to map
     */
    @Override
    public void addStoredObject(StoredObject storedObject) {
        hashSet.put(storedObject.getId(), storedObject);
    }

    /**
     * Gets the value of next id
     * @return the value of next id
     */
    @Override
    public int getNextId() {
        if (nextId == 0) {
            for (StoredObject storedObject : getStoredObjects()) {
                if (storedObject.getId() >= nextId) {
                    nextId = storedObject.getId() + 1;
                }
            }
            System.out.println(nextId);
            return nextId;
        }
        nextId += 1;
        return nextId;
    }

    /**
     * Gets all the StoredObject that have been modified.
     * @return all the StoredObject that have been modified.
     */
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

    @Override
    public void delete(StoredObject storedObject) {
        hashSet.remove(storedObject.getId());
    }
}

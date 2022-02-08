package com.invoicer.main.data;

import com.invoicer.sql.AttributeGroup;

import java.util.Collection;

public interface IManager {

    StoredObject createObject(AttributeGroup attributeGroup);

    StoredObject createObject();

    StoredObject createAndStore();

    Collection<StoredObject> getStoredObjects();

    StoredObject getStoredObject(int id);

    void addStoredObject(StoredObject storedObject);

    Collection<StoredObject> getModified();

    int getNextId();

    void delete(StoredObject storedObject);

}

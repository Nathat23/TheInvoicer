package com.invoicer.main.data;

import com.invoicer.sql.Attribute;
import com.invoicer.sql.StoreableObject;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IManager {

    StoreableObject createObject(int id);

    StoreableObject create();

    Collection<StoreableObject> getStoreableObjects();

    StoreableObject getStoreableObject(int id);

    void addStoreableObject(StoreableObject storeableObject);

    Collection<StoreableObject> getModified();

}

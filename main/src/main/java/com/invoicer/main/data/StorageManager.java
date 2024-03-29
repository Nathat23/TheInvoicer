package com.invoicer.main.data;

import com.invoicer.sql.*;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class StorageManager {

    private final SqlHandler sqlHandler;
    private final DataManager dataManager;
    private final LinkedHashMap<Manager, SqlTable> hashMap;

    public StorageManager(SqlHandler sqlHandler, DataManager dataManager) {
        this.sqlHandler = sqlHandler;
        this.dataManager = dataManager;
        this.hashMap = new LinkedHashMap<>();
    }

    // get data manager
    private DataManager getDataManager() {
        return dataManager;
    }

    // get sql handler
    private SqlHandler getSqlHandler() {
        return sqlHandler;
    }

    // init
    public void init() {
        for (Map.Entry<Class<? extends StoredObject>, Manager> entry : getDataManager().getHashMap().entrySet()) {
            // create the sql table for managers
            SqlTable sqlTable = new SqlTable(getSqlHandler(), entry.getValue().getConfig());
            sqlTable.init();
            // load the AttributeGroups and create the associated stored objects
            for (AttributeGroup attributeGroup : sqlTable.getObjects()) {
                try {
                    StoredObject storedObject = entry.getKey().getConstructor(DataManager.class, AttributeGroup.class).newInstance(getDataManager(), attributeGroup);
                    entry.getValue().addStoredObject(storedObject);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            hashMap.put(entry.getValue(), sqlTable);
        }
    }

    // method to check if there have been any changes to the attributes
    public void commitChanges() {
        for (Map.Entry<Manager, SqlTable> entry : hashMap.entrySet()) {
            Manager manager = entry.getKey();
            SqlTable table = entry.getValue();
            // loop through all modified objects
            for (StoredObject storedObject : manager.getModified()) {
                table.updateObject(storedObject.getAttributeGroup());
            }
        }
    }

    // function to delete storedobject
    public void delete(StoredObject toDelete) {
        // get manager for this storedobject and its sqltable
        Manager manager = getDataManager().getManager(toDelete.getClass());
        SqlTable table = hashMap.get(manager);
        // delete this object
        table.deleteObject(toDelete.getAttributeGroup());
        manager.delete(toDelete);
    }
}

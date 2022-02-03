package com.invoicer.main.data;

import com.invoicer.sql.SqlHandler;
import com.invoicer.sql.SqlTable;
import com.invoicer.sql.AttributeGroup;

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

    private DataManager getDataManager() {
        return dataManager;
    }

    private SqlHandler getSqlHandler() {
        return sqlHandler;
    }

    public void init() {
        for (Map.Entry<Class<? extends StoredObject>, Manager> entry : getDataManager().getHashMap().entrySet()) {
            SqlTable sqlTable = new SqlTable(getSqlHandler(), entry.getValue().getConfig());
            sqlTable.init();
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

    public void commitChanges() {
        for (Map.Entry<Manager, SqlTable> entry : hashMap.entrySet()) {
            Manager manager = entry.getKey();
            SqlTable table = entry.getValue();
            for (StoredObject storedObject : manager.getModified()) {
                table.updateObject(storedObject.getAttributeGroup());
            }
        }
    }
}

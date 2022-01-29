package com.invoicer.main.data;

import com.invoicer.sql.SqlHandler;
import com.invoicer.sql.SqlTable;
import com.invoicer.sql.StoreableObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class StorageManager {

    private final SqlHandler sqlHandler;
    private final DataManager dataManager;
    private final LinkedHashMap<Manager, SqlTable<?>> hashMap;

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
        for (Map.Entry<Class<? extends StoreableObject>, Manager> entry : getDataManager().getHashMap().entrySet()) {
            SqlTable<?> sqlTable = new SqlTable<>(getSqlHandler(), entry.getValue().getConfig(), entry.getKey());
            sqlTable.init();
            for (StoreableObject storeableObject : sqlTable.getObjects()) {
                entry.getValue().addStoreableObject(storeableObject);
            }
            hashMap.put(entry.getValue(), sqlTable);
        }
    }

    public void commitChanges() {
        for (Map.Entry<Manager, SqlTable<?>> entry : hashMap.entrySet()) {
            Manager manager = entry.getKey();
            SqlTable<?> table = entry.getValue();
            for (StoreableObject storeableObject : manager.getModified()) {
                table.updateObject(storeableObject);
            }
        }
    }
}

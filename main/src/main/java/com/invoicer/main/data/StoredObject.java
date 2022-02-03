package com.invoicer.main.data;

import com.invoicer.sql.Attribute;
import com.invoicer.sql.AttributeGroup;

public class StoredObject {

    private final AttributeGroup attributeGroup;
    private final DataManager dataManager;

    public StoredObject(DataManager dataManager, AttributeGroup attributeGroup) {
        this.attributeGroup = attributeGroup;
        this.dataManager = dataManager;
    }

    public int getId() {
        return attributeGroup.getId();
    }

    DataManager getDataManager() {
        return dataManager;
    }

    public AttributeGroup getAttributeGroup() {
        return attributeGroup;
    }

    public boolean isModified() {
        for (Attribute attribute : getAttributeGroup().getAttributes()) {
            if (attribute.isModified()) {
                return true;
            }
        }
        return false;
    }
}

package com.invoicer.sql;

import java.util.Collection;

public class StoreableObject implements AbstractStorableObject {

    private int id;
    private Collection<Attribute<String>> attributes;

    public StoreableObject(int id, Collection<Attribute<String>> attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Collection<Attribute<String>> getAttributes() {
        return attributes;
    }
}

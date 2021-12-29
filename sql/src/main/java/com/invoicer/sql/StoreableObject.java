package com.invoicer.sql;

import java.util.Collection;
import java.util.Collections;

public class StoreableObject implements AbstractStorableObject {

    private final int id;
    private final Collection<Attribute> attributes;

    public StoreableObject(int id, Collection<Attribute> attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Collection<Attribute> getAttributes() {
        return Collections.unmodifiableCollection(attributes);
    }
}

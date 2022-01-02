package com.invoicer.sql;

import java.util.Collection;
import java.util.Collections;

public class StoreableObject implements AbstractStorableObject {

    private final int id;
    private Collection<Attribute> attributes;
    private final Config config;

    public StoreableObject(int id, Config config) {
        this.id = id;
        this.config = config;
    }

    public StoreableObject(int id, Config config, Collection<Attribute> attributes) {
        this(id, config);
        this.attributes = attributes;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public Collection<Attribute> getAttributes() {
        if (attributes == null) {
            attributes = getConfig().getStoredObjectConfig().load();
        }
        return Collections.unmodifiableCollection(attributes);
    }
}

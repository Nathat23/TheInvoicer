package com.invoicer.sql;

import java.util.Collections;
import java.util.List;

public class AttributeGroup implements IAttributeGroup {

    private final int id;
    private List<Attribute> attributes;
    private final Config config;

    public AttributeGroup(int id, Config config) {
        this.id = id;
        this.config = config;
    }

    public AttributeGroup(int id, Config config, List<Attribute> attributes) {
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
    public List<Attribute> getAttributes() {
        if (attributes == null) {
            attributes = getConfig().getStoredObjectConfig().load();
        }
        return Collections.unmodifiableList(attributes);
    }
}

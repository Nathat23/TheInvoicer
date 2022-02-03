package com.invoicer.sql;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class Config {

    private final Yaml yaml;
    private final AttributeGroupConfig attributeGroupConfig;

    public Config(InputStream file) {
        yaml = new Yaml(new Constructor(AttributeGroupConfig.class));
        attributeGroupConfig = yaml.load(file);
    }

    public AttributeGroupConfig getStoredObjectConfig() {
        return attributeGroupConfig;
    }
}

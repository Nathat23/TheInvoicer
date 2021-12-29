package com.invoicer.sql;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class Config {

    private final Yaml yaml;
    private final StoredObjectConfig storedObjectConfig;

    public Config(InputStream file) {
        yaml = new Yaml(new Constructor(StoredObjectConfig.class));
        storedObjectConfig = yaml.load(file);
    }

    public StoredObjectConfig getStoredObjectConfig() {
        return storedObjectConfig;
    }
}

package com.invoicer.sql;

public class Attribute<T> {

    private final String name;
    private boolean modified;
    private T value;

    public Attribute(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        this.modified = true;
    }

    void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isModified() {
        return modified;
    }
}

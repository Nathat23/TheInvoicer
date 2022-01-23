package com.invoicer.sql;

public class AttributeConfig {

    private DataType type;
    private String name;
    private String human;
    private String foreignKeyTable;
    private String foreignKeyColumn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return type;
    }

    public String getHuman() {
        return human;
    }

    public void setHuman(String human) {
        this.human = human;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public Class<?> getTypeClass() {
        return this.type.getType();
    }

    public String getForeignKeyColumn() {
        return foreignKeyColumn;
    }

    public String getForeignKeyTable() {
        return foreignKeyTable;
    }

    public void setForeignKeyColumn(String foreignKeyColumn) {
        this.foreignKeyColumn = foreignKeyColumn;
    }

    public void setForeignKeyTable(String foreignKeyTable) {
        this.foreignKeyTable = foreignKeyTable;
    }
}

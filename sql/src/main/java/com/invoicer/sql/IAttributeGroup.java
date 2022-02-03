package com.invoicer.sql;

import java.util.List;

public interface IAttributeGroup {

    int getId();

    List<Attribute> getAttributes();

    Config getConfig();
}

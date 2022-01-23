package com.invoicer.sql;

import java.util.Collection;
import java.util.List;

public interface AbstractStorableObject {

    int getId();

    List<Attribute> getAttributes();

    Config getConfig();
}

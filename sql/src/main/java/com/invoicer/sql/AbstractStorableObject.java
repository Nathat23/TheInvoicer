package com.invoicer.sql;

import java.util.Collection;

public interface AbstractStorableObject {

    int getId();

    Collection<Attribute<String>> getAttributes();
}

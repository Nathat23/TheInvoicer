package com.invoicer.sql;

import java.lang.reflect.InvocationTargetException;

public enum DataValidation {
    EMAIL(EmailValidation.class),
    POSTCODE(PostcodeValidation.class);


    private final Class<? extends Validation> validationClass;
    private Validation validation;
    DataValidation(Class<? extends Validation> validationClass) {
        this.validationClass = validationClass;
    }

    public Validation getValidation() {
        if (validation == null) {
            try {
                validation = validationClass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return validation;
    }


}

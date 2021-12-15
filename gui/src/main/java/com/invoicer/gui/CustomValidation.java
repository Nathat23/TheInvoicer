package com.invoicer.gui;

public abstract class CustomValidation implements AbstractCustomValidation {

    private ValidationResult validationResult;

    public ValidationResult validate() {
        validationResult = validatePage();
        return getValidationResult();
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }
}

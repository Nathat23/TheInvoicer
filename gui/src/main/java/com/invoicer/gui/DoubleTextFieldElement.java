package com.invoicer.gui;

public class DoubleTextFieldElement extends TextFieldElement {

    public DoubleTextFieldElement(String name) {
        super(name);
    }

    @Override
    public boolean validate() {
        try {
            Double.valueOf(getContent().getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Double getValue() {
        return Double.parseDouble(getContent().getText());
    }
}

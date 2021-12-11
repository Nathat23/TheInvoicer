package com.invoicer.theinvoicer.gui;

public abstract class DialogElement implements AbstractElement {

    private final String name;

    public DialogElement(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

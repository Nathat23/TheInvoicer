package com.invoicer.main;

import com.invoicer.gui.*;
import com.invoicer.main.data.Manager;
import com.invoicer.sql.Attribute;
import com.invoicer.sql.BooleanAttribute;
import com.invoicer.sql.StoreableObject;

public class GenericModifyDialog extends Dialog {

    private StoreableObject object;
    private final Manager manager;
    private DialogPage dialogPage;
    private boolean newObject;

    public GenericModifyDialog(Manager manager, StoreableObject object) {
        super("Modify ", DialogSize.MEDIUM);
        this.object = object;
        this.manager = manager;
    }

    @Override
    public void populate() {
        if (object == null) {
            object = manager.createObject();
            newObject = true;
        }
        dialogPage = new DialogPage("Modify");
        for (Attribute attribute : object.getAttributes()) {
            EditableElement dialogElement;
            switch (attribute.getAttributeConfig().getType()) {
                case STRING:
                    dialogElement = new StringTextFieldElement(attribute.getAttributeConfig().getHuman());
                    break;
                case BOOLEAN:
                    dialogElement = new CheckBoxElement(attribute.getAttributeConfig().getHuman());
                    break;
                case INTEGER:
                    dialogElement = new IntegerTextFieldElement(attribute.getAttributeConfig().getHuman());
                    break;
                case DOUBLE:
                    dialogElement = new DoubleTextFieldElement(attribute.getAttributeConfig().getHuman());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + attribute.getAttributeConfig().getType());
            }
            if (dialogElement instanceof TextFieldElement && attribute.getValue() != null) {
                ((TextFieldElement) dialogElement).getContent().setText(attribute.getValue().toString());
            }
            if (dialogElement instanceof CheckBoxElement) {
                ((CheckBoxElement) dialogElement).getContent().setSelected(((BooleanAttribute) attribute).getValue());
            }
            dialogPage.addElement((DialogElement) dialogElement);
        }
        addPage(dialogPage);
    }

    @Override
    public void onClosure() {
        for (DialogElement dialogElement : dialogPage.getElementList()) {
            for (Attribute attribute : object.getAttributes()) {
                if (!attribute.getAttributeConfig().getHuman().equals(dialogElement.getName())) {
                    continue;
                }
                if (dialogElement instanceof TextFieldElement) {
                    attribute.setValue(((TextFieldElement) dialogElement).getValue());
                }
                if (dialogElement instanceof CheckBoxElement) {
                    attribute.setValue(((CheckBoxElement) dialogElement).getValue());
                }
                break;
            }
        }
        if (newObject) {
            manager.addStoreableObject(object);
        }
    }

    public StoreableObject getObject() {
        return object;
    }
}

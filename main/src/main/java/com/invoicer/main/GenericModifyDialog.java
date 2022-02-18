package com.invoicer.main;

import com.invoicer.gui.*;
import com.invoicer.main.data.Manager;
import com.invoicer.main.data.StoredObject;
import com.invoicer.sql.Attribute;
import com.invoicer.sql.BooleanAttribute;
import com.invoicer.sql.AttributeGroup;
import com.invoicer.sql.DataValidation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenericModifyDialog extends Dialog {

    private StoredObject object;
    private final Manager manager;
    private DialogPage dialogPage;
    private boolean newObject;

    public GenericModifyDialog(Manager manager, StoredObject object) {
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
        Map<Attribute,DialogElement> dataValidationList = new LinkedHashMap<>();
        for (Attribute attribute : object.getAttributeGroup().getAttributes()) {
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
            if (attribute.getAttributeConfig().getDataValidation() != null) {
                dataValidationList.put(attribute, (DialogElement) dialogElement);
            }
        }
        dialogPage.setValidation(new CustomValidation() {
            @Override
            public ValidationResult validatePage() {
                for (Attribute attribute : dataValidationList.keySet()) {
                    DataValidation dataValidation = attribute.getAttributeConfig().getDataValidation();
                    DialogElement editableElement = dataValidationList.get(attribute);
                    EditableElement element = (EditableElement) editableElement;
                    if (!dataValidation.getValidation().validate(element.getValue().toString())) {
                        return new ValidationResult(false, "Invalid " + editableElement.getName());
                    }
                }
                return new ValidationResult(true, "");
            }
        });
        addPage(dialogPage);
    }

    @Override
    public void onClosure() {
        for (DialogElement dialogElement : dialogPage.getElementList()) {
            for (Attribute attribute : object.getAttributeGroup().getAttributes()) {
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
            manager.addStoredObject(object);
        }
    }

    public StoredObject getObject() {
        return object;
    }
}

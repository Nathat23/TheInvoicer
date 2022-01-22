package com.invoicer.main;

import com.invoicer.gui.Dialog;
import com.invoicer.gui.DialogElement;
import com.invoicer.gui.DialogPage;
import com.invoicer.gui.IntegerTextFieldElement;
import com.invoicer.gui.StringTextFieldElement;
import com.invoicer.gui.TextFieldElement;
import com.invoicer.sql.Attribute;
import com.invoicer.sql.StoreableObject;

public class ModifyDialog extends Dialog {

    private final StoreableObject object;
    private DialogPage dialogPage;

    public ModifyDialog(StoreableObject object) {
        super("Modify " + object.getClass().getSimpleName(), DialogSize.MEDIUM);
        this.object = object;
    }

    @Override
    public void populate() {
        dialogPage = new DialogPage("Modify");
        for (Attribute attribute : object.getAttributes()) {
            TextFieldElement dialogElement;
            switch (attribute.getAttributeConfig().getType()) {
                case STRING:
                    dialogElement = new StringTextFieldElement(attribute.getAttributeConfig().getHuman());
                    break;
                case INTEGER:
                    dialogElement = new IntegerTextFieldElement(attribute.getAttributeConfig().getHuman());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + attribute.getAttributeConfig().getType());
            }
            if (attribute.getValue() != null) {
                dialogElement.getContent().setText(attribute.getValue().toString());
            }
            dialogPage.addElement(dialogElement);
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
                attribute.setValue(((TextFieldElement) dialogElement).getValue());
                break;
            }
        }
    }
}

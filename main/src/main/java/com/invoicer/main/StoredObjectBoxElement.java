package com.invoicer.main;

import com.invoicer.gui.ComboBoxElement;
import com.invoicer.main.data.StoredObject;
import com.invoicer.sql.Attribute;
import com.invoicer.sql.AttributeGroup;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class StoredObjectBoxElement<T extends StoredObject> extends ComboBoxElement<T> {

    public StoredObjectBoxElement(String name) {
        super(name);
    }

    @Override
    public ComboBox<T> createElement() {
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setEditable(false);
        comboBox.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T object) {
                if (object == null) {
                    return null;
                }
                return generateString(object);
            }

            @Override
            public T fromString(String string) {
                return null;
            }
        });
        return comboBox;
    }

    private String generateString(StoredObject object) {
        StringBuilder builder = new StringBuilder();
        builder.append("ID: ").append(object.getId()).append(" (");
        for (Attribute attribute : object.getAttributeGroup().getAttributes()) {
            builder.append(attribute.getValue()).append(",");
        }
        builder.setCharAt(builder.length() - 1, ')');
        return builder.toString();
    }
}

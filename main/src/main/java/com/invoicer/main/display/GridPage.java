package com.invoicer.main.display;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class GridPage extends Page {

    private final int columns;

    public GridPage(String name, String iconPath,  int columns) {
        super(name, iconPath);
        this.columns = columns;
    }

    @Override
    public Node generate() {
        GridPane gridPane = new GridPane();
        int size = getPageElementList().size();
        int actualColumns = Math.min(size, getPreferredColumns());
        for (int i = 0; i < size; i++) {
            int row = i / actualColumns;
            int column = i % actualColumns;
            PageElement pageElement = getPageElementList().get(i);
            pageElement.generateElement();
            gridPane.add(getPageElementList().get(i).getContainer(), column, row);
        }
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100D / actualColumns);
        for (int i = 0; i < gridPane.getColumnCount(); i++) {
            gridPane.getColumnConstraints().add(columnConstraints);
        }
        return gridPane;
    }

    public int getPreferredColumns() {
        return columns;
    }
}

package com.invoicer.main;

import java.util.ArrayList;
import java.util.List;

public class Page {

    private final List<PageElement> pageElementList;
    private final String name;

    public Page(String name) {
        this.pageElementList = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addPageElement(PageElement element) {
        pageElementList.add(element);
    }

    public List<PageElement> getPageElementList() {
        return pageElementList;
    }

}

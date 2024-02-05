package com.example.bfirst.repo;

public class Categories {

    private String categoryName;
    private String icon;
    private long index;

    public Categories(String categoryName, String icon, long index) {
        this.categoryName = categoryName;
        this.icon = icon;
        this.index = index;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "categoryName='" + categoryName + '\'' +
                ", icon='" + icon + '\'' +
                ", index=" + index +
                '}';
    }
}

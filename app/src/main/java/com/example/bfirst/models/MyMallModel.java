package com.example.bfirst.models;

import java.util.List;

public class MyMallModel {


    private int type;

    private String backgroundColor;
    /////////////////////////////////////Banner Slider
    private List<SliderModel> sliderModelList;

    public MyMallModel(int type, List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }

    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

    /////////////////////////////////////Banner Slider


    /////////////////////////////////////strip ad/////////////////////////////
    private String resource;

    public MyMallModel(int type, String resource, String backgroundColor) {
        this.type = type;
        this.resource = resource;
        this.backgroundColor = backgroundColor;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    /////////////////////////////////////strip ad/////////////////////////////


    private String title;

    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    //////////////Horizontal product layout   //////////////////////////////////////

    private List<WishListModel> viewAllProductList;

    public MyMallModel(int type, String title, String backgroundColor, List<HorizontalProductScrollModel> horizontalProductScrollModelList, List<WishListModel> viewAllProductList) {
        this.type = type;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        this.viewAllProductList = viewAllProductList;
    }

    public List<WishListModel> getViewAllProductList() {
        return viewAllProductList;
    }

    public void setViewAllProductList(List<WishListModel> viewAllProductList) {
        this.viewAllProductList = viewAllProductList;
    }


    //////////////Horizontal product layout //////////////////////////////////////


    //////////////Grid product layout //////////////////////////////////////

    public MyMallModel(int type, String title, String backgroundColor, List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.type = type;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }


    //////////////Grid product layout //////////////////////////////////////


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HorizontalProductScrollModel> getHorizontalProductScrollModelList() {
        return horizontalProductScrollModelList;
    }

    public void setHorizontalProductScrollModelList(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }


    @Override
    public String toString() {
        return "MyMallModel{" +
                "type=" + type +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", sliderModelList=" + sliderModelList +
                ", resource='" + resource + '\'' +
                ", title='" + title + '\'' +
                ", horizontalProductScrollModelList=" + horizontalProductScrollModelList +
                ", viewAllProductList=" + viewAllProductList +
                '}';
    }
}

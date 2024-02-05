package com.example.bfirst.models;

import java.util.ArrayList;

public class WishListModel {
    private String productID;
    private String productImage;
    private String productTitle;
    private long freeCoupons;
    private String rating;
    private long totalRatings;
    private String productPrice;
    private String cuttingPrice;
    private boolean COD;
    private boolean inStock;
    private ArrayList<String> tags;


    public WishListModel( String productID,String productImage, String productTitle, long freeCoupons, String rating, long totalRatings, String productPrice, String cuttingPrice, boolean COD,boolean inStock) {
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.freeCoupons = freeCoupons;
        this.rating = rating;
        this.totalRatings = totalRatings;
        this.productPrice = productPrice;
        this.cuttingPrice = cuttingPrice;
        this.COD = COD;
        this.inStock = inStock;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public long getFreeCoupons() {
        return freeCoupons;
    }

    public void setFreeCoupons(long freeCoupons) {
        this.freeCoupons = freeCoupons;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(long totalRatings) {
        this.totalRatings = totalRatings;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCuttingPrice() {
        return cuttingPrice;
    }

    public void setCuttingPrice(String cuttingPrice) {
        this.cuttingPrice = cuttingPrice;
    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }

    @Override
    public String toString() {
        return "WishListModel{" +
                "productID='" + productID + '\'' +
                ", productImage='" + productImage + '\'' +
                ", productTitle='" + productTitle + '\'' +
                ", freeCoupons=" + freeCoupons +
                ", rating='" + rating + '\'' +
                ", totalRatings=" + totalRatings +
                ", productPrice='" + productPrice + '\'' +
                ", cuttingPrice='" + cuttingPrice + '\'' +
                ", COD=" + COD +
                ", inStock=" + inStock +
                ", tags=" + tags +
                '}';
    }
}
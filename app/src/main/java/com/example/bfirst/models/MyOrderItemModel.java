package com.example.bfirst.models;
import java.util.Date;

public class MyOrderItemModel {
    private String productId;
    private String productTitle;
    private String productImage;



    private String orderStatus;
    private String address;
    private String couponId;
    private String cuttedPrice;

    private Date orderedDate;
    private Date packedDate;
    private Date shippedDate;
    private Date deliveredDate;
    private Date cancelledDate;

    private String discountedPrice;
    private long freeCoupons;
    private String fullName;
    private String orderID;
    private String paymentMethod;
    private String pinCode;
    private String productPrice;
    private long productQuantity;
    private String userId;
    private int rating = 0;
    private String deliveryPrice;
    private boolean cancellationRequested;



    public MyOrderItemModel(String productId, String productTitle, String productImage, String orderStatus, String address, String couponId, String cuttedPrice, Date orderedDate, Date packedDate, Date shippedDate, Date deliveredDate, Date cancelledDate, String discountedPrice, long freeCoupons, String fullName, String orderID, String paymentMethod, String pinCode, String productPrice, long productQuantity, String userId,  String deliveryPrice,boolean  cancellationRequested) {
        this.productId = productId;
        this.productTitle = productTitle;
        this.productImage = productImage;
        this.orderStatus = orderStatus;
        this.address = address;
        this.couponId = couponId;
        this.cuttedPrice = cuttedPrice;
        this.orderedDate = orderedDate;
        this.packedDate = packedDate;
        this.shippedDate = shippedDate;
        this.deliveredDate = deliveredDate;
        this.cancelledDate = cancelledDate;
        this.discountedPrice = discountedPrice;
        this.freeCoupons = freeCoupons;
        this.fullName = fullName;
        this.orderID = orderID;
        this.paymentMethod = paymentMethod;
        this.pinCode = pinCode;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.userId = userId;
        this.deliveryPrice = deliveryPrice;
        this. cancellationRequested =  cancellationRequested;
    }

    public boolean isCancellationRequested() {
        return cancellationRequested;
    }

    public void setCancellationRequested(boolean cancellationRequested) {
        this.cancellationRequested = cancellationRequested;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Date getPackedDate() {
        return packedDate;
    }

    public void setPackedDate(Date packedDate) {
        this.packedDate = packedDate;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public Date getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public long getFreeCoupons() {
        return freeCoupons;
    }

    public void setFreeCoupons(long freeCoupons) {
        this.freeCoupons = freeCoupons;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    @Override
    public String toString() {
        return "MyOrderItemModel{" +
                "productId='" + productId + '\'' +
                ", productTitle='" + productTitle + '\'' +
                ", productImage='" + productImage + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", address='" + address + '\'' +
                ", couponId='" + couponId + '\'' +
                ", cuttedPrice='" + cuttedPrice + '\'' +
                ", orderedDate=" + orderedDate +
                ", packedDate=" + packedDate +
                ", shippedDate=" + shippedDate +
                ", deliveredDate=" + deliveredDate +
                ", cancelledDate=" + cancelledDate +
                ", discountedPrice='" + discountedPrice + '\'' +
                ", freeCoupons=" + freeCoupons +
                ", fullName='" + fullName + '\'' +
                ", orderID='" + orderID + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", productQuantity=" + productQuantity +
                ", userId='" + userId + '\'' +
                ", rating=" + rating +
                ", deliveryPrice='" + deliveryPrice + '\'' +
                ", cancellationRequested=" + cancellationRequested +
                '}';
    }
}

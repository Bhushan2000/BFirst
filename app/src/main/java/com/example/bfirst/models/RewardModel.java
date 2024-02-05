package com.example.bfirst.models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class RewardModel {
    private String type;
    private String lower_limit;
    private String upper_limit;
    private String disOrAmo;
    private String couponBody;
    private Date validity;
    private boolean allReadyUsed;
    private String couponId;

    public RewardModel( String couponId,String type, String lower_limit, String upper_limit, String disOrAmo, String couponBody, Date validity, boolean allReadyUsed) {
        this.couponId = couponId;
        this.type = type;
        this.lower_limit = lower_limit;
        this.upper_limit = upper_limit;
        this.disOrAmo = disOrAmo;
        this.couponBody = couponBody;
        this.validity = validity;
        this.allReadyUsed = allReadyUsed;
     }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLower_limit() {
        return lower_limit;
    }

    public void setLower_limit(String lower_limit) {
        this.lower_limit = lower_limit;
    }

    public String getUpper_limit() {
        return upper_limit;
    }

    public void setUpper_limit(String upper_limit) {
        this.upper_limit = upper_limit;
    }

    public String getDisOrAmo() {
        return disOrAmo;
    }

    public void setDisOrAmo(String disOrAmo) {
        this.disOrAmo = disOrAmo;
    }

    public String getCouponBody() {
        return couponBody;
    }

    public void setCouponBody(String couponBody) {
        this.couponBody = couponBody;
    }

    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }

    public boolean isAllReadyUsed() {
        return allReadyUsed;
    }

    public void setAllReadyUsed(boolean allReadyUsed) {
        this.allReadyUsed = allReadyUsed;
    }

    @Override
    public String toString() {
        return "RewardModel{" +
                "type='" + type + '\'' +
                ", lower_limit='" + lower_limit + '\'' +
                ", upper_limit='" + upper_limit + '\'' +
                ", disOrAmo='" + disOrAmo + '\'' +
                ", couponBody='" + couponBody + '\'' +
                ", validity=" + validity +
                ", allReadyUsed=" + allReadyUsed +
                ", couponId='" + couponId + '\'' +
                '}';
    }
}

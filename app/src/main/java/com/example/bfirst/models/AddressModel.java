package com.example.bfirst.models;

import android.app.Dialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

public class AddressModel {

    private boolean selected;



    private String city;
    private String locality;
    private String flatNo;
    private String pinCode;
    private String landmark;
    private String name;
    private String phoneNo;
    private String alternatePhoneNo;
    private String state;

    public AddressModel(boolean selected, String city, String locality, String flatNo, String pinCode, String landmark, String name, String phoneNo, String alternatePhoneNo, String state) {
        this.selected = selected;
        this.city = city;
        this.locality = locality;
        this.flatNo = flatNo;
        this.pinCode = pinCode;
        this.landmark = landmark;
        this.name = name;
        this.phoneNo = phoneNo;
        this.alternatePhoneNo = alternatePhoneNo;
        this.state = state;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAlternatePhoneNo() {
        return alternatePhoneNo;
    }

    public void setAlternatePhoneNo(String alternatePhoneNo) {
        this.alternatePhoneNo = alternatePhoneNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "AddressModel{" +
                "selected=" + selected +
                ", city='" + city + '\'' +
                ", locality='" + locality + '\'' +
                ", flatNo='" + flatNo + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", landmark='" + landmark + '\'' +
                ", name='" + name + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", alternatePhoneNo='" + alternatePhoneNo + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}

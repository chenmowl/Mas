package com.eme.mas.model.entity;

import java.io.Serializable;

/**
 * Created by dijiaoliang on 16/4/27.
 */
public class MyAddressBo2 implements Serializable{

    private String name;
    private String city;
    private String phone;
    private String address;
    private String address_detail;

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

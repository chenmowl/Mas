package com.eme.mas.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zulei on 16/8/1.
 */
public class MyAddressBo implements Parcelable{

    /**
     * distance : null
     * cityName : 北京市
     * areaName : 朝阳区
     * updateTime : 1467373694601
     * provinceId : 0
     * provinceName : null
     * address_id : 1936f0f178c74981813e50df6fb19234
     * area_id : 110105
     * name : 胡锦涛
     * mobile : 18644356678
     * city_id : 010
     * location : 116.46442,39.90756
     * address : 恭送
     * areaPath : 星巴克咖啡(建国路第二咖店)
     */

    private String distance;
    private String cityName;
    private String areaName;
    private String updateTime;
    private String provinceId;
    private String provinceName;
    private String address_id;
    private String area_id;
    private String name;
    private String mobile;
    private String city_id;
    private String location;
    private String address;
    private String areaPath;

    public MyAddressBo() {
    }

    protected MyAddressBo(Parcel in) {
        distance = in.readString();
        cityName = in.readString();
        areaName = in.readString();
        updateTime = in.readString();
        provinceId = in.readString();
        provinceName = in.readString();
        address_id = in.readString();
        area_id = in.readString();
        name = in.readString();
        mobile = in.readString();
        city_id = in.readString();
        location = in.readString();
        address = in.readString();
        areaPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(distance);
        dest.writeString(cityName);
        dest.writeString(areaName);
        dest.writeString(updateTime);
        dest.writeString(provinceId);
        dest.writeString(provinceName);
        dest.writeString(address_id);
        dest.writeString(area_id);
        dest.writeString(name);
        dest.writeString(mobile);
        dest.writeString(city_id);
        dest.writeString(location);
        dest.writeString(address);
        dest.writeString(areaPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyAddressBo> CREATOR = new Creator<MyAddressBo>() {
        @Override
        public MyAddressBo createFromParcel(Parcel in) {
            return new MyAddressBo(in);
        }

        @Override
        public MyAddressBo[] newArray(int size) {
            return new MyAddressBo[size];
        }
    };

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaPath() {
        return areaPath;
    }

    public void setAreaPath(String areaPath) {
        this.areaPath = areaPath;
    }

    public static Creator<MyAddressBo> getCREATOR() {
        return CREATOR;
    }
}

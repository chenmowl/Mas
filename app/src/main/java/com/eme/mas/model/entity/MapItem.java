package com.eme.mas.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simiao on 2015/5/20.
 */
public class MapItem implements Parcelable {
    private String name;
    private String address;
    private String location;
    private String strCity;
    private String cityCode;
    private String areaCode;

    public MapItem() {
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public MapItem(Parcel parcel) {
        setName(parcel.readString());
        setAddress(parcel.readString());
        setLocation(parcel.readString());
        setStrCity(parcel.readString());
        setCityCode(parcel.readString());
        setAreaCode(parcel.readString());
    }


    public String getStrCity() {
        return strCity;
    }

    public void setStrCity(String strCity) {
        this.strCity = strCity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(location);
        parcel.writeString(strCity);
        parcel.writeString(cityCode);
        parcel.writeString(areaCode);
    }

    public static final Creator<MapItem> CREATOR = new Creator<MapItem>() {
        @Override
        public MapItem createFromParcel(Parcel parcel) {
            return new MapItem(parcel);
        }

        @Override
        public MapItem[] newArray(int i) {
            return new MapItem[i];
        }
    };
}

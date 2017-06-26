package com.eme.mas.controller;

import android.text.TextUtils;

import com.eme.mas.connection.IActionListener;
import com.eme.mas.connection.WAction;
import com.eme.mas.controller.customeInterface.ILocation;

import java.util.HashMap;

/**
 * Created by zulei on 16/7/31.
 */
public class LocationImpl extends BaseImpl implements ILocation {

    public LocationImpl(IActionListener iActionListener) {
        super(iActionListener);
    }

    @Override
    public void addNewAddress(String province_id, String city_id, String area_id, String areaPath,
                              String address, String name, String mobile, String location,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("province_id", province_id);
        params.put("city_id", city_id);
        params.put("area_id", area_id);
        params.put("areaPath",areaPath);
        params.put("address",address);
        params.put("name",name);
        params.put("mobile",mobile);
        params.put("location",location);
        request(WAction.ADD_NEW_ADDRESS,params,tag);
    }

    @Override
    public void getMyAddressList(String location,String tag) {
        HashMap<String, String> params=null;
        if(!TextUtils.isEmpty(location)){
            params = new HashMap<>();
            params.put("location", location);
        }
        request(WAction.GET_ADDRESS,params,tag);
    }

    @Override
    public void deleteMyAddress(String address_id,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("address_id", address_id);
        request(WAction.DELETE_ADDRESS,params,tag);
    }

    @Override
    public void editMyAddress(String address_id, String province_id, String city_id, String area_id,
                              String areaPath, String address, String name, String mobile,
                              String location,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("address_id", address_id);
        params.put("province_id", province_id);
        params.put("city_id", city_id);
        params.put("area_id", area_id);
        params.put("areaPath",areaPath);
        params.put("address",address);
        params.put("name",name);
        params.put("mobile",mobile);
        params.put("location",location);
        request(WAction.EDIT_ADDRESS,params,tag);
    }

    @Override
    public void getBrotherLocation(String order_id,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("order_id", order_id);
        request(WAction.GET_BROTHER_LOCATION,params,tag);
    }
}

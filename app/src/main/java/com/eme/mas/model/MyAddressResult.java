package com.eme.mas.model;

import com.eme.mas.model.entity.MyAddressBo;

import java.util.List;

/**
 * 我的地址实体类
 * <p/>
 * Created by zulei on 16/8/15.
 */
public class MyAddressResult extends Result {


    private List<MyAddressBo> data;

    public List<MyAddressBo> getData() {
        return data;
    }

    public void setData(List<MyAddressBo> data) {
        this.data = data;
    }
}

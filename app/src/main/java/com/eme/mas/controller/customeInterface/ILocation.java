package com.eme.mas.controller.customeInterface;

/**
 * Created by zulei on 16/7/31.
 */
public interface ILocation {
    /**
     * 新增收货地址
     */
    void  addNewAddress(String province_id,String city_id,String area_id,String areaPath,String address,String name,
                        String mobile,String location,String tag);

    /**
     * 获取我的收货地址列表
     */
    void  getMyAddressList(String location,String tag);

    /**
     * 删除我的地址
     */
    void  deleteMyAddress(String address_id,String tag);
    /**
     * 编辑我的地址
     */
    void  editMyAddress(String address_id,String province_id,String city_id,String area_id,String areaPath,String address,String name,
                        String mobile,String location,String tag);

    /**
     * 获取小哥位置
     */
    void getBrotherLocation(String order_id,String tag);

}

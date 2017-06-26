package com.eme.mas.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zulei on 16/8/15.
 */
public class MyOrderBo implements Serializable{


    /**
     * order_number : 20160815172556742
     * product_list : [{"product_num":"1","product_image_url":"/upload/img/store/0/1469078419098.jpg","product_id":"fefa6c417e9d47b6a91de8699437d937","product_price":"899.00","product_name":"茅台酒","product_stock_percent":null,"old_product_integral":null,"product_integral":"11","is_coupon":null,"product_criticism_num":null,"old_product_price":null},{"product_num":"9","product_image_url":"/upload/img/store/0/1469079168307.png","product_id":"4822ef418b324426a2d76d58cdaadf59","product_price":"699.00","product_name":"五粮液","product_stock_percent":null,"old_product_integral":null,"product_integral":"11","is_coupon":null,"product_criticism_num":null,"old_product_price":null}]
     * totalPayPrice : 7190.00
     * order_time : 2016-25-15 17:25:56
     * address : {"provinceId":null,"area_id":null,"location":null,"updateTime":null,"cityName":"北京市","provinceName":null,"city_id":null,"distance":null,"areaPath":"???(????)","areaName":"","address_id":null,"address":"北京市朝阳区建国路靠近金鼎轩(建国路店)","name":null,"mobile":""}
     * invoice : 普通发票&nbsp;&nbsp;我拉&nbsp;&nbsp;不开发票
     * totalGoodsPrice : 7190.00
     * pay_way : 货到付款
     * freight : 0.00
     * servicer_phone : null
     * coupon_price : null
     * order_id : eaf3877783ff4e068a7a3ffe0ec82ec1
     * order_status : 50
     */

    private String order_number;
    private String totalPayPrice;
    private String order_time;
    /**
     * provinceId : null
     * area_id : null
     * location : null
     * updateTime : null
     * cityName : 北京市
     * provinceName : null
     * city_id : null
     * distance : null
     * areaPath : ???(????)
     * areaName :
     * address_id : null
     * address : 北京市朝阳区建国路靠近金鼎轩(建国路店)
     * name : null
     * mobile :
     */

    private AddressBean address;
    private String invoice;
    private String totalGoodsPrice;
    private String pay_way;
    private String pay_way_code;
    private String freight;
    private String evaluation_status;
    private String servicer_phone;
    private String coupon_price;
    private String order_id;
    private String order_status;
    private String shipper_name;
    private String mobPhone;
    private String order_shipper_time;
    private String create_time;
    private String finnshed_time;
    private String order_message;

    public String getOrder_message() {
        return order_message;
    }

    public void setOrder_message(String order_message) {
        this.order_message = order_message;
    }

    public String getShipper_name() {
        return shipper_name;
    }

    public void setShipper_name(String shipper_name) {
        this.shipper_name = shipper_name;
    }

    public String getMobPhone() {
        return mobPhone;
    }

    public void setMobPhone(String mobPhone) {
        this.mobPhone = mobPhone;
    }

    public String getOrder_shipper_time() {
        return order_shipper_time;
    }

    public void setOrder_shipper_time(String order_shipper_time) {
        this.order_shipper_time = order_shipper_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFinnshed_time() {
        return finnshed_time;
    }

    public void setFinnshed_time(String finnshed_time) {
        this.finnshed_time = finnshed_time;
    }

    /**
     * product_num : 1
     * product_image_url : /upload/img/store/0/1469078419098.jpg
     * product_id : fefa6c417e9d47b6a91de8699437d937
     * product_price : 899.00
     * product_name : 茅台酒
     * product_stock_percent : null
     * old_product_integral : null
     * product_integral : 11
     * is_coupon : null
     * product_criticism_num : null
     * old_product_price : null
     */

    private List<ProductListBean> product_list;

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getTotalPayPrice() {
        return totalPayPrice;
    }

    public void setTotalPayPrice(String totalPayPrice) {
        this.totalPayPrice = totalPayPrice;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getEvaluation_status() {
        return evaluation_status;
    }

    public void setEvaluation_status(String evaluation_status) {
        this.evaluation_status = evaluation_status;
    }

    public String getTotalGoodsPrice() {
        return totalGoodsPrice;
    }

    public void setTotalGoodsPrice(String totalGoodsPrice) {
        this.totalGoodsPrice = totalGoodsPrice;
    }

    public String getPay_way() {
        return pay_way;
    }

    public void setPay_way(String pay_way) {
        this.pay_way = pay_way;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getServicer_phone() {
        return servicer_phone;
    }

    public void setServicer_phone(String servicer_phone) {
        this.servicer_phone = servicer_phone;
    }

    public String getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(String coupon_price) {
        this.coupon_price = coupon_price;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPay_way_code() {
        return pay_way_code;
    }

    public void setPay_way_code(String pay_way_code) {
        this.pay_way_code = pay_way_code;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public List<ProductListBean> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(List<ProductListBean> product_list) {
        this.product_list = product_list;
    }

    public static class AddressBean implements Serializable{
        private String provinceId;
        private String area_id;
        private String location;
        private String updateTime;
        private String cityName;
        private String provinceName;
        private String city_id;
        private String distance;
        private String areaPath;
        private String areaName;
        private String address_id;
        private String address;
        private String name;
        private String mobile;

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getAreaPath() {
            return areaPath;
        }

        public void setAreaPath(String areaPath) {
            this.areaPath = areaPath;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAddress_id() {
            return address_id;
        }

        public void setAddress_id(String address_id) {
            this.address_id = address_id;
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }

    public static class ProductListBean implements Serializable{
        private String product_num;
        private String product_image_url;
        private String product_id;
        private String product_price;
        private String product_name;
        private String product_stock_percent;
        private String old_product_integral;
        private String product_integral;
        private String is_coupon;
        private String product_criticism_num;
        private String old_product_price;

        public String getProduct_num() {
            return product_num;
        }

        public void setProduct_num(String product_num) {
            this.product_num = product_num;
        }

        public String getProduct_image_url() {
            return product_image_url;
        }

        public void setProduct_image_url(String product_image_url) {
            this.product_image_url = product_image_url;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_stock_percent() {
            return product_stock_percent;
        }

        public void setProduct_stock_percent(String product_stock_percent) {
            this.product_stock_percent = product_stock_percent;
        }

        public String getOld_product_integral() {
            return old_product_integral;
        }

        public void setOld_product_integral(String old_product_integral) {
            this.old_product_integral = old_product_integral;
        }

        public String getProduct_integral() {
            return product_integral;
        }

        public void setProduct_integral(String product_integral) {
            this.product_integral = product_integral;
        }

        public String getIs_coupon() {
            return is_coupon;
        }

        public void setIs_coupon(String is_coupon) {
            this.is_coupon = is_coupon;
        }

        public String getProduct_criticism_num() {
            return product_criticism_num;
        }

        public void setProduct_criticism_num(String product_criticism_num) {
            this.product_criticism_num = product_criticism_num;
        }

        public String getOld_product_price() {
            return old_product_price;
        }

        public void setOld_product_price(String old_product_price) {
            this.old_product_price = old_product_price;
        }
    }
}

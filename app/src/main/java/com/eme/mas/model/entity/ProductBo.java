package com.eme.mas.model.entity;

import java.util.List;

/**
 * 商品实体类
 * <p>
 * Created by dijiaoliang on 16/6/12.
 */
public class ProductBo {

//
//            "product_id": "e6efb029e57a47809d719215dfaf8084",
//            "product_price": "39.00",
//            "web_url": null,
//            "product_name": "西风酒",
//            "all_spec": null,
//            "product_stock_percent": null,
//            "product_integral": "100",
//            "is_coupon": "0",
//            "specs_code": null,
//            "product_criticism_num": "1",
//            "old_product_price": null,
//            "integral": null,
//            "product_image_url": "/upload/img/store/0/1469522704720.png",
//            "goods_show": null,
//            "old_product_integral": null,
//            "product_images": [
//            "/upload/img/store/0/1469522704720.png"
//            ],
//            "coupon_info": null,
//            "product_info": null
//            "spec_id": "98804bee886d4bebb28631c998991a19",
//            "subtitle": null,

    private String product_id;
    private String product_price;
    private String web_url;
    private String product_name;
    private String all_spec;
    private String product_stock_percent;
    private String is_coupon;
    private String product_integral;
    private String specs_code;
    private String product_criticism_num;
    private String old_product_price;
    private String integral;
    private String product_image_url;
    private String goods_show;
    private String old_product_integral;
    private List<String> product_images;
    private String coupon_info;
    private String product_info;
    private String spec_id;
    private String product_channel;
    private String subtitle;

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getProduct_channel() {
        return product_channel;
    }

    public void setProduct_channel(String product_channel) {
        this.product_channel = product_channel;
    }

    public String getSpec_id() {
        return spec_id;
    }

    public void setSpec_id(String spec_id) {
        this.spec_id = spec_id;
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

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getAll_spec() {
        return all_spec;
    }

    public void setAll_spec(String all_spec) {
        this.all_spec = all_spec;
    }

    public String getProduct_stock_percent() {
        return product_stock_percent;
    }

    public void setProduct_stock_percent(String product_stock_percent) {
        this.product_stock_percent = product_stock_percent;
    }

    public String getIs_coupon() {
        return is_coupon;
    }

    public void setIs_coupon(String is_coupon) {
        this.is_coupon = is_coupon;
    }

    public String getProduct_integral() {
        return product_integral;
    }

    public void setProduct_integral(String product_integral) {
        this.product_integral = product_integral;
    }

    public String getSpecs_code() {
        return specs_code;
    }

    public void setSpecs_code(String specs_code) {
        this.specs_code = specs_code;
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

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getProduct_image_url() {
        return product_image_url;
    }

    public void setProduct_image_url(String product_image_url) {
        this.product_image_url = product_image_url;
    }

    public String getGoods_show() {
        return goods_show;
    }

    public void setGoods_show(String goods_show) {
        this.goods_show = goods_show;
    }

    public String getOld_product_integral() {
        return old_product_integral;
    }

    public void setOld_product_integral(String old_product_integral) {
        this.old_product_integral = old_product_integral;
    }

    public List<String> getProduct_images() {
        return product_images;
    }

    public void setProduct_images(List<String> product_images) {
        this.product_images = product_images;
    }

    public String getCoupon_info() {
        return coupon_info;
    }

    public void setCoupon_info(String coupon_info) {
        this.coupon_info = coupon_info;
    }

    public String getProduct_info() {
        return product_info;
    }

    public void setProduct_info(String product_info) {
        this.product_info = product_info;
    }
}
